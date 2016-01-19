package com.rick.businessengine.task;
/**
 * Task which will be executed many times by returning itself when getNext() is invoked. 
 * The interval between last task and this is decided by delay.
 * @author Rick
 *
 */
public abstract class BaseTimerTask extends BaseTask {
	private static final long serialVersionUID = 1L;
	protected long delay = 0;  //seconds

	public long getDelay() {
		return this.delay;
	}
	
	@Override
	public BaseTask getNext() {
		return this;
	}
}