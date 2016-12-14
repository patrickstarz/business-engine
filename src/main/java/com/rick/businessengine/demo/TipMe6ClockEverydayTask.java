package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseTimerTask;

public class TipMe6ClockEverydayTask extends BaseTimerTask {

	public TipMe6ClockEverydayTask() {
		setDelay(nextDayDelay(6, 0, 0));
	}

	@Override
	public void exec() {
		System.out.println("TipMe6ClockEverydayTask..." + this.hashCode());
	}
}
