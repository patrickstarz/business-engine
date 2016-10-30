package com.rick.businessengine.demo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueDemo {
	public static void main(String[] args) {
		BlockingQueue<String> queue = new LinkedBlockingQueue<>();
//		queue.take(); //block
		queue.poll(); //not block
	}
}
