package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseOnceTask;

public class TipMeOnceTask extends BaseOnceTask{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7072044200533189221L;

	@Override
	public void exec() {
		System.out.println("TipMeOnceTask..."+this.hashCode());
	}
}
