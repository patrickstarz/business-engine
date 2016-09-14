package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseTimerTask;

public class TipMeEverydayTask extends BaseTimerTask{
	
	@Override
	public long getDelay() {
		return 24*60*60*1000l;  //1day
	}
	
	@Override
	public void exec() {
		System.out.println("TipMeEverydayTask..."+this.hashCode());
	}
}
