package com.airAd.yaqinghui.util;

import java.lang.reflect.Method;

public class MethodObject {
	private Object target;//
	private String method_name;

	public MethodObject() {
	}

	public MethodObject(Object target, String methodName) {
		super();
		this.target = target;
		this.method_name = methodName;
	}

	public MethodObject(Object target) {
		super();
		this.target = target;
	}

	public static MethodObject function(Object target, String methodName,
			Object... objects) {
		MethodObject mo = new MethodObject(target, methodName);
		// mo.invoke(objects);
		return mo;
	}

	public static MethodObject function(Object target, Object... objects) {
		MethodObject mo = new MethodObject(target);
		// mo.invoke(objects);
		return mo;
	}

	public Object invoke(Object... objects) {
		Class clazz = target.getClass();
		try {
			Method[] ms = clazz.getDeclaredMethods();

			Method targetMethod = null;
			if (method_name == null && ms.length == 1) {
				targetMethod = ms[0];
			} else if (method_name != null && ms.length >= 1) {
				for (Method m : ms) {
					if (method_name.equals(m.getName())) {
						targetMethod = m;
						break;
					}
				}

			} else {
				return null;
			}
			targetMethod.setAccessible(true);
			return targetMethod.invoke(target, objects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void rebund(Object anothertarget) {
		target = anothertarget;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

}