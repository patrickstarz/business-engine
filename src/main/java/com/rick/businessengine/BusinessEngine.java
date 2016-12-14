package com.rick.businessengine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.rick.businessengine.task.BaseTask;

/**
 * 事务引擎
 * @author Rick
 * 
 */
public class BusinessEngine extends Thread{
	private Logger logger = Logger.getLogger(this.getClass());

	private static BusinessEngine businessEngine;                 	// 单例模式
	private ExecutorService executor = null;          				// 线程池
	private TaskManager taskManager = null;                       	// 任务管理器
	private boolean shutdown;

	private BusinessEngine() {
		executor = Executors.newCachedThreadPool();
		taskManager = new TaskManager();
	}

	/**
	 * 单例模式,只创建一个引擎
	 * @param poolSize
	 * @return
	 */
	public static synchronized BusinessEngine getInstance() {
		if (businessEngine == null) {
			businessEngine = new BusinessEngine();
		}
		return businessEngine;
	}

	/**
	 * 关闭事务引擎
	 */
	public void shutdown(){
		shutdown = true;
	}
	
	/**
	 * 向事务引擎递交任务
	 * @param task
	 */
	public void addTask(BaseTask task) {
		taskManager.add(task, task.isStore());
	}

	/**
	 * 在同一个进程中执行任务
	 * @param task
	 */
	public void runTask(BaseTask task) {
		task.exec();
		BaseTask next = task.getNext();
		if (next != null) {
			addTask(next);// 将下个任务递交给事务引擎
		}
		// 删除持久化文件,避免BaseTimerTask的持久化文件被删除
		if (next != task) {
			taskManager.remove(task);
		}
	}

	/**
	 * 执行一次性任务
	 * @param task
	 */
	public void runOnceTask(final BaseTask task) {
		// ask thread pool to run a new task
		executor.execute(new Runnable() {
			public void run() {
				runTask(task);
			}
		});
	}

	@Override
	public void start() {
		logger.info("bussiness-engine has start successfully...");
		// restore from manager
		taskManager.restore();

		while (true) {
			if(shutdown){break;}
			// get task from manager
			BaseTask task = null;
			try {
				task = (BaseTask) taskManager.getTask();
			} catch (InterruptedException e) {
				logger.error("taskManager.getTask is interuputed unexcepted...", e);
				break;
			}
			runOnceTask(task);
		}
		executor.shutdown();
	}
}