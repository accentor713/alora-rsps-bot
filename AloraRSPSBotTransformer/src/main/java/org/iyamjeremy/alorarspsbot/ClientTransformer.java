package org.iyamjeremy.alorarspsbot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
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
import javassist.CtConstructor;
import javassist.CtField;
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
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", LOCAL_BOT_CLIENT_JAR);
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

	public static void transformClient() {
		makeBotDir();
		//if (!new File(LOCAL_CLIENT_ZIP).exists() || !new File(LOCAL_CLIENT_JAR).exists()) {
			downloadClientZip();
			unzipClient();
		//}
		ZipFile clientJar = loadClientJar();
		modifyClientJar(clientJar);
	}

	private static void modifyClientJar(ZipFile clientJar) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(LOCAL_BOT_CLIENT_JAR));

			ClassPool cp = ClassPool.getDefault();
			
			String thisJarPath = URLDecoder.decode(ClientTransformer.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
			ZipFile thisJar = new ZipFile(thisJarPath);
			Enumeration<? extends ZipEntry> thisJarEntries = thisJar.entries();
			while (thisJarEntries.hasMoreElements()) {
				ZipEntry entry = thisJarEntries.nextElement();
				if (entry.getName().startsWith("org/iyamjeremy/alorarspsbot/api/") || entry.getName().equals("hook-file.txt")) {
					ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
					try {
						InputStream in = thisJar.getInputStream(entry);
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
			}
			thisJar.close();
			
			
			Enumeration<? extends ZipEntry> entries = clientJar.entries();
			List<String> classNames = new ArrayList<>();
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
						classNames.add(className);
					}
					else {
						out.putNextEntry(new ZipEntry(entry.getName()));
						out.write(bytes);
						out.closeEntry();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for (String className : classNames) {
				try {
					CtClass cc = cp.get(className);

					if (cc.getName().equals("MJ")) {
						for (CtMethod m : cc.getMethods()) {
							if (m.getName().equals("I") && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].getName().equals("java.lang.String")) {
								m.insertBefore("{ if (org.iyamjeremy.alorarspsbot.api.Bot.runCommand($1)) { return; } }");
							}
						}
					}
					
					if (cc.getName().equals("MG")) {
						for (CtMethod m : cc.getMethods()) {
							if (m.getName().equals("I") && m.getParameterTypes().length == 8) {
								System.out.println("Found it");
								m.insertBefore("{ System.out.println($1 + \", \" + $2 + \", \" + $3 + \", \" + $4 + \", \" + $5 + \", \" + $6 + \", \" + $7 + \", \" + $8); }");
							}
						}
					}
					
					if (cc.getName().equals("FI")) {
						for (CtMethod m : cc.getDeclaredMethods()) {
							if (m.getParameterTypes().length == 11) {
								m.insertBefore("{ long hash = 0x40000000 | $8 | $9 << 7 | $7 << 14 | $10 << 20; hash |= ((long)$6) << 32; org.iyamjeremy.alorarspsbot.api.Bot.trackGameObject(hash); }");
							}
						}
					}

					for (CtConstructor constructor : cc.getDeclaredConstructors()) {
						constructor.insertAfter("{ org.iyamjeremy.alorarspsbot.api.Bot.trackInstance(\"" + cc.getName() + "\", (Object)this); }");
					}

					if (cc.getName().equals("KA")) {
						CtMethod m = cc.getDeclaredMethod("Z", new CtClass[]{CtClass.intType, CtClass.intType, CtClass.intType, CtClass.intType});
						m.insertBefore("{ System.out.println(\"Drew line\"); }");
					}

					byte[] bytes = cc.toBytecode();
					out.putNextEntry(new ZipEntry(className.replace(".", "/") + ".class"));
					out.write(bytes);
					out.closeEntry();
				} catch (NotFoundException | CannotCompileException e) {
					e.printStackTrace();
				}
			}
			
			out.close();
			

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
