package com.rick.businessengine.task;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * base class of task
 * @author Rick
 * 
 */
public abstract class BaseTask implements Serializable, Delayed{
	private static final long serialVersionUID = 1L;
	
	protected boolean isStore;    // is need store,default yes
	protected String storename;   // key to store and used to compare
	protected TimeUnit unit = TimeUnit.NANOSECONDS;

	public BaseTask() {
		isStore=true;
		storename = getClass().getName() + System.nanoTime();
	}
	
	public boolean isStore() {
		return isStore;
	}
	
	public String getStoreName() {
		return storename;
	}
	
	/**
	 * specific service
	 */
	public abstract void exec();

	/**
	 * get next task
	 * @return
	 */
	public abstract BaseTask getNext();

	/**
	 * make task can be compared
	 */
	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object other) {
		BaseTask task = (BaseTask) other;
		return this.storename.equalsIgnoreCase(task.storename);
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		return 0;
	}
	
	@Override
	public int compareTo(Delayed o) {
		long minus = o.getDelay(unit) - this.getDelay(unit);
		return minus > 0 ? -1 : 1;
	}
}
