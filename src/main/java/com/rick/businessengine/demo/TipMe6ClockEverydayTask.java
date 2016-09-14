package com.rick.businessengine.demo;

import java.util.Calendar;
import java.util.Date;

import com.rick.businessengine.task.BaseTimerTask;

public class TipMe6ClockEverydayTask extends BaseTimerTask {

	@Override
	public long getDelay() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, 6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date date8 = c.getTime();

		long delay = date8.getTime() - date.getTime();
		if (delay < 0) {
			c.add(Calendar.DAY_OF_YEAR, 1);
			delay = c.getTimeInMillis() - date.getTime();
		}
		return delay;
	}

	@Override
	public void exec() {
		System.out.println("TipMe6ClockEverydayTask..." + this.hashCode());
	}
}
