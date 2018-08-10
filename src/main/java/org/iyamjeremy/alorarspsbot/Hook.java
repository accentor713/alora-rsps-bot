package org.iyamjeremy.alorarspsbot;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Hook {

	private String[] attributes;
	private String sourceBody;

	public Hook(String[] attributes, String sourceBody) {
		this.attributes = attributes;
		this.sourceBody = sourceBody;
	}
	
	@Override
	public String toString() {
		String s = "::";
		for (int i = 0; i < this.attributes.length; i++) {
			if (i > 0) {
				s += ":";
			}
			s += this.attributes[i];
		}
		s += "\n" + this.sourceBody;
		
		return s;
	}

	public boolean canApply(CtClass cc) {
		if (isCreator()) {
			return false;
		}
		String name = cc.getName();
		if (cc.getPackageName() != null) {
			name = cc.getPackageName() + "." + name;
		}
		return name.equals(this.attributes[1]);
	}

	public void apply(CtClass cc, ClassPool cp) throws NotFoundException, CannotCompileException {
		String action = this.attributes[0];
		
		switch (action) {
		case "insert":
			this.actionInsert(cc);
			break;
		case "classInstanceTracker":
			this.actionClassInstanceTracker(cc);
			break;
		case "addInterface":
			this.actionAddInterface(cc, cp);
			break;
		case "addMethod":
			this.actionAddMethod(cc);
			break;
		case "addField":
			this.actionAddField(cc);
			break;
		default:
			throw new RuntimeException("Hook action invalid " + action);
		}

	}

	private void actionAddField(CtClass cc) throws CannotCompileException {
		cc.addField(CtField.make(this.sourceBody, cc));
	}

	private void actionAddMethod(CtClass cc) throws CannotCompileException {
		cc.addMethod(CtMethod.make(this.sourceBody, cc));
	}

	private void actionAddInterface(CtClass cc, ClassPool cp) throws NotFoundException {
		cc.addInterface(cp.get(this.attributes[2]));
	}

	private void actionClassInstanceTracker(CtClass cc) throws CannotCompileException {
		try {
			CtField instancesField = CtField.make("public static java.util.List instances = new java.util.ArrayList();", cc);
			instancesField.setGenericSignature(cc.getName());
			cc.addField(instancesField);
		} catch (CannotCompileException e) {}
		for (CtConstructor c : cc.getConstructors()) {
			c.insertAfter("{ instances.add(this); }");
		}
	}

	private void actionInsert(CtClass cc) throws NotFoundException, CannotCompileException {
		System.out.println("HELLO WORLD");
		String method = this.attributes[2].split("\\(")[0];
		String[] paramTypeNames = this.attributes[2].split("\\(")[1].split("\\)")[0].split(",");

		for (CtMethod m : cc.getMethods()) {
			if (m.getName().equals(method)) {
				CtClass[] paramTypes = m.getParameterTypes();
				if (paramTypes.length == paramTypeNames.length) {
					boolean match = true;
					for (int i = 0; i < paramTypes.length; i++) {
						System.out.println("HI");
						if (!paramTypes[i].getName().equals(paramTypeNames[i])) {
							System.out.println(paramTypes[i].getName() + " == " + paramTypeNames[i]);
							match = false;
							break;
						}
					}
					if (match) {
						try {
							System.out.println(sourceBody);
							m.insertBefore("{" + this.sourceBody + "}");
							//m.insertAt(Integer.parseInt(this.attributes[3]), "{" + this.sourceBody + "}");
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	public boolean isCreator() {
		String action = this.attributes[0];
		return action.equals("new");
	}

	public void create(ZipOutputStream out) {
		try {
			ZipEntry entry = new ZipEntry(this.attributes[2].replace(".", "/") + ".class");
			out.putNextEntry(entry);
			CtClass cc = null;
			ClassPool cp = ClassPool.getDefault();
			switch (this.attributes[1]) {
			case "interface":
				cc = createInterface(cp);
				break;
			case "class":
				cc = createClass(cp);
				break;
			default:
				throw new RuntimeException("Invalid create action " + this.attributes[1]);
			}
			out.write(cc.toBytecode());
			out.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private CtClass createClass(ClassPool cp) {
		return cp.makeClass(this.attributes[2]);
	}

	private CtClass createInterface(ClassPool cp) {
		CtClass cc = cp.makeInterface(this.attributes[2]);
		for (String methodDeclaration : this.sourceBody.split("\n")) {
			if (!methodDeclaration.trim().equals("")) {
				try {
					cc.addMethod(CtMethod.make(methodDeclaration, cc));
				} catch (CannotCompileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return cc;
	}

}
