package com.rick.businessengine.task;
/**
 * task which will be executed only once 
 * @author Rick
 *
 */
public abstract class BaseOnceTask extends BaseTask {
	private static final long serialVersionUID = 1L;

	@Override
	public BaseTask getNext() {
		return null;
	}
}