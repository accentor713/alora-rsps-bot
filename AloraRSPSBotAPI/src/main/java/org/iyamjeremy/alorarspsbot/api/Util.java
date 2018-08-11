package org.iyamjeremy.alorarspsbot.api;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Util {
	
	private HashMap<String, String> hooks = new HashMap<>();
	
	public Util() {
		
	}
	
	public void addHook(String hookName, String hookValue) {
		hooks.put(hookName, hookValue);
	}
	
	public Class<?> findClass(String classHookName) {
		try {
			return Class.forName(hooks.get(classHookName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getField(String classHookName, String fieldHookName, Object instance) {
		try {
			Field field = findClass(classHookName).getDeclaredField(hooks.get(fieldHookName));
			field.setAccessible(true);
			return field.get(instance);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object callMethod(String classHookName, String methodHookName, Class<?>[] paramTypes, Object instance, Object[] args) {
		try {
			Method method = findClass(classHookName).getDeclaredMethod(hooks.get(methodHookName), paramTypes);
			method.setAccessible(true);
			return method.invoke(instance, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

}
