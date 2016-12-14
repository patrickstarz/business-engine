package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseTimerTask;

public class TipMeEverydayTask extends BaseTimerTask{
	public TipMeEverydayTask() {
		setDelay(24*60*60*1000l);
	}
	
	@Override
	public void exec() {
		System.out.println("TipMeEverydayTask..."+this.hashCode());
	}
}
