package com.rick.businessengine.demo;

import com.rick.businessengine.task.BaseOnceTask;

public class TipMeOnceTask extends BaseOnceTask{
	@Override
	public void exec() {
		System.out.println("TipMeOnceTask..."+this.hashCode());
	}
}
