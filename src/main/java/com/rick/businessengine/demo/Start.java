package com.rick.businessengine.demo;

import com.rick.businessengine.BusinessEngine;

public class Start {
	static{
		new Thread(new Runnable() {
			public void run() {
				BusinessEngine engine = BusinessEngine.getInstance();
				engine.start();
			}
		}).start();
	}
	
	public static void main(String[] args) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		BusinessEngine engine = BusinessEngine.getInstance();
		engine.addTask(new TipMeOnceTask());
		engine.addTask(new TipMeEverydayTask());
		System.out.println(engine);
		engine.addTask(new TipMe10MinLaterTask());
		engine.addTask(new TipMe6ClockEverydayTask());
	}
}
