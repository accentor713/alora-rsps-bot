package org.iyamjeremy.alorarspsbot;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Hook {

	private String[] attributes;
	private String sourceBody;
	
	public Hook(String[] attributes, String sourceBody) {
		this.attributes = attributes;
		this.sourceBody = sourceBody;
	}
	
	public boolean canApply(CtClass clazz) {
		String name = clazz.getName();
		if (clazz.getPackageName() != null) {
			name = clazz.getPackageName() + "." + name;
		}
		return name.equals(this.attributes[1]);
	}
	
	public void apply(CtClass clazz) {
		String action = this.attributes[0];
		
		switch (action) {
			case "insert":
				this.actionInsert(clazz);
				break;
			default:
				throw new RuntimeException("Hook action invalid " + action);
		}
		
	}
	
	private void actionInsert(CtClass clazz) {
		String method = this.attributes[2].split("\\(")[0];
		String[] paramTypeNames = this.attributes[2].split("\\(")[1].split("\\)")[0].split(",");
		
		for (CtMethod m : clazz.getMethods()) {
			if (m.getName().equals(method)) {
				try {
					CtClass[] paramTypes = m.getParameterTypes();
					if (paramTypes.length == paramTypeNames.length) {
						boolean match = true;
						for (int i = 0; i < paramTypes.length; i++) {
							if (!paramTypes[i].getName().equals(paramTypeNames[i])) {
								System.out.println(paramTypes[i].getName() + " == " + paramTypeNames[i]);
								match = false;
								break;
							}
						}
						if (match) {
							try {
								m.insertBefore("{" + this.sourceBody + "}");
								//m.insertAt(Integer.parseInt(this.attributes[3]), "{" + this.sourceBody + "}");
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CannotCompileException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
