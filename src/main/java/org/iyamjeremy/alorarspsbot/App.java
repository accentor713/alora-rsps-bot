package org.iyamjeremy.alorarspsbot;

import javassist.*;


/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
	try {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("java.awt.Rectangle");
	} catch (javassist.NotFoundException e) {
		
	}
        System.out.println( "Hello World!" );
    }
}
