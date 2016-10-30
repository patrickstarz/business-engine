package com.rick.businessengine.demo;

import com.rick.businessengine.BusinessEngine;

public class Demo2 {
	public static void main(String[] args) {
		BusinessEngine engine = BusinessEngine.getInstance();
		System.out.println(engine);
		engine.addTask(new TipMeOnceTask());
	}
}
