package org.iyamjeremy.alorarspsbot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class ClientTransformer {

	private static final String CLIENT_ZIP_URL = "http://localhost:8080/client.zip";//"https://www.alora.io/downloads/client.zip";
	private static final String BOT_DIR = System.getProperty("user.home") + "/.alora-rsps-bot";
	private static final String LOCAL_CLIENT_ZIP = BOT_DIR + "/client.zip";
	private static final String LOCAL_CLIENT_JAR = BOT_DIR + "/client.jar";
	private static final String LOCAL_BOT_CLIENT_JAR = BOT_DIR + "/bot-client.jar";

	public static void startGame() {
		Process p = null;
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", LOCAL_CLIENT_JAR);
		pb.directory(new File(BOT_DIR));
		pb.inheritIO();
		try {
			p = pb.start();
			p.waitFor();
			pb = new ProcessBuilder("rm", "-r", BOT_DIR);
			p = pb.start();
			p.waitFor();
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void transformClient(List<Hook> hooks) {
		makeBotDir();
		//if (!new File(LOCAL_CLIENT_ZIP).exists() || !new File(LOCAL_CLIENT_JAR).exists()) {
			downloadClientZip();
			unzipClient();
		//}
		ZipFile clientJar = loadClientJar();
		modifyClientJar(clientJar, hooks);
	}

	private static void modifyClientJar(ZipFile clientJar, List<Hook> hooks) {
		boolean globalHookAdded = true;
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(LOCAL_BOT_CLIENT_JAR));

			ClassPool cp = ClassPool.getDefault();
			List<Hook> hooksToRemove = new ArrayList<Hook>();

			for (Hook hook : hooks) {
				if (hook.isCreator()) {
					hook.create(out);
					hooksToRemove.add(hook);
				}
			}

			for (Hook hook : hooksToRemove) {
				hooks.remove(hook);
			}
			hooksToRemove.clear();
			
			Enumeration<? extends ZipEntry> entries = clientJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
				try {
					InputStream in = clientJar.getInputStream(entry);
					byte[] b = new byte[1];
					while (in.read(b, 0, 1) != -1) {
						bytesOut.write(b);
					}
					byte[] bytes = bytesOut.toByteArray();
					out.putNextEntry(new ZipEntry(entry.getName()));
					out.write(bytes);
					out.closeEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			out.close();
			new File(LOCAL_CLIENT_JAR).delete();
			new File(LOCAL_BOT_CLIENT_JAR).renameTo(new File(LOCAL_CLIENT_JAR));

			while (hooks.size() > 0) {
				out = new ZipOutputStream(new FileOutputStream(LOCAL_BOT_CLIENT_JAR));
				clientJar = new ZipFile(LOCAL_CLIENT_JAR);
				entries = clientJar.entries();

				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
					try {
						InputStream in = clientJar.getInputStream(entry);
						byte[] b = new byte[1];
						while (in.read(b, 0, 1) != -1) {
							bytesOut.write(b);
						}
						byte[] bytes = bytesOut.toByteArray();
						if (entry.getName().endsWith(".class")) {
							String className = entry.getName().replace("/", ".").substring(0, entry.getName().length()-".class".length());
							cp.insertClassPath(new ByteArrayClassPath(className, bytes));
							CtClass cc = cp.get(className);
							
							for (Hook hook : hooks) {
								if (hook.canApply(cc)) {
									cc.defrost();
									try {
										hook.apply(cc, cp);
										hooksToRemove.add(hook);
									} catch (CannotCompileException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							
							for (Hook hook : hooksToRemove) {
								hooks.remove(hook);
							}
							hooksToRemove.clear();
							
							if (!globalHookAdded) {
								System.out.println("Class: " + cc.getName());
								if (!cc.isInterface()) {
									for (CtMethod m : cc.getMethods()) {
										System.out.println(m.getName() + m.getSignature());
										if (!m.getName().equals("move") && !(m.getName().equals("I") && cc.getName().equals("YZ"))) {
											try {
												m.insertBefore("{ String s = \"" + cc.getName() + "." + m.getName() + ":" + m.getSignature() + "\"; if (Bot.globalHookMethods.indexOf(s) == -1) { Bot.globalHookMethods.add(s); System.out.println(s); } }");
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}
							}
							
							
							try {
								bytes = cc.toBytecode();
							} catch (CannotCompileException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						ZipEntry newEntry = new ZipEntry(entry.getName());
						out.putNextEntry(newEntry);
						out.write(bytes, 0, bytes.length);
						out.closeEntry();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("Hooks Left: " + hooks.size());
				if (hooks.size() == 1) {
					System.out.println(hooks.get(0).toString());
				}
				
				globalHookAdded = true;
				
				out.close();
				new File(LOCAL_CLIENT_JAR).delete();
				new File(LOCAL_BOT_CLIENT_JAR).renameTo(new File(LOCAL_CLIENT_JAR));
				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void makeBotDir() {
		File botDir = new File(BOT_DIR);
		if (!botDir.exists()) {
			botDir.mkdir();
		}
	}

	private static void downloadClientZip() {
		try {
			URL url = new URL(CLIENT_ZIP_URL);
			ReadableByteChannel rbc = Channels.newChannel(url.openStream());
			FileOutputStream fos = new FileOutputStream(LOCAL_CLIENT_ZIP);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void unzipClient() {
		Process p = null;
		ProcessBuilder pb = new ProcessBuilder("unzip", LOCAL_CLIENT_ZIP);
		pb.directory(new File(BOT_DIR));
		try {
			p = pb.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ZipFile loadClientJar() {
		try {
			return new ZipFile(LOCAL_CLIENT_JAR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
