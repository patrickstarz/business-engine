package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseTask;
import com.rick.businessengine.task.BaseTimerTask;

public class TipMe10MinLaterTask extends BaseTimerTask{
	
	@Override
	public long getDelay() {
		return 10*60*1000;
	}
	
	@Override
	public void exec() {
		System.out.println("TipMe10MinLaterTask..."+this.hashCode());
	}
	
	@Override
	public BaseTask getNext() {
		return null;   //exec just once
	}
}
