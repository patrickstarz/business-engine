package com.rick.businessengine.task;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Task which will be executed many times by returning itself when getNext() is invoked. 
 * The interval between last task and this is decided by delay.
 * @author Rick
 *
 */
public abstract class BaseTimerTask extends BaseTask {
	private static final long serialVersionUID = 1L;
	protected long delay;  		//milliseconds
	private long triggerTime;	//milliseconds
	
	@Override
	public boolean isStore() {
		return false;   //avoid repeat store on starting engine everytime
	}
	
	/**
	 * setDelay to count out triggerTime 
	 * @param delay milliseconds
	 */
	public void setDelay(long delay) {
		this.delay = delay;
		refresh();
	}
	
	@Override
	public BaseTask getNext() {
		refresh();
		return this;
	}
	
	public void refresh(){
		triggerTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delay, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(triggerTime - System.nanoTime(), TimeUnit.NANOSECONDS);
	}
	
	/**
	 * 对于指定每日时分秒执行的任务，计算出下个(可能是今天或明天)delay
	 * @param hour24 24小时制
	 * @param minute
	 * @param second
	 * @return
	 */
	protected long nextDayDelay(int hour24, int minute, int second){
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		c.set(Calendar.HOUR_OF_DAY, hour24);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND,0);
		Date d = c.getTime();
		
		long delay= d.getTime() - date.getTime();
		if(delay < 0){         //已经过点了
			c.add(Calendar.DAY_OF_YEAR, 1);
			delay = c.getTimeInMillis() - date.getTime(); 
		}
		return delay;
	}
}