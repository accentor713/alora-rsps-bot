package org.iyamjeremy.alorarspsbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ClientTransformer {

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
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void transformClient(List<Hook> hooks) {
		makeBotDir();
		if (!new File(LOCAL_CLIENT_ZIP).exists()) {
			downloadClientZip();
			unzipClient();
		}
		ZipFile clientJar = loadClientJar();
		modifyClientJar(clientJar, hooks);
	}

	private static void modifyClientJar(ZipFile clientJar, List<Hook> hooks) {
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(LOCAL_BOT_CLIENT_JAR));
			Enumeration<? extends ZipEntry> entries = clientJar.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				byte[] bytes = new byte[(int) entry.getSize()];
				try {
					clientJar.getInputStream(entry).read(bytes);
					if (entry.getName().endsWith(".class")) {
						String className = entry.getName().replace("/", ".").substring(0, entry.getName().length()-".class".length());
						ClassPool cp = ClassPool.getDefault();
						cp.insertClassPath(new ByteArrayClassPath(className, bytes));
						CtClass cc = cp.get(className);
						
						for (Hook hook : hooks) {
							if (hook.canApply(cc)) {
								hook.apply(cc);
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
			URL url = new URL("https://www.alora.io/downloads/client.zip");
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
