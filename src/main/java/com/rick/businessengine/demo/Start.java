package com.rick.businessengine.demo;

import com.rick.businessengine.BusinessEngine;

public class Start {
	public static void main(String[] args) {
		BusinessEngine engine = BusinessEngine.getInstance();
		engine.start();
		
		engine.addTask(new TipMeOnceTask());
		engine.addTask(new TipMeEverydayTask());
		engine.addTask(new TipMe10MinLaterTask());
		engine.addTask(new TipMe6ClockEverydayTask());
	}
}
