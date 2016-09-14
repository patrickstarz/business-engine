package com.rick.businessengine;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.rick.businessengine.task.BaseOnceTask;
import com.rick.businessengine.task.BaseTask;
import com.rick.businessengine.task.BaseTimerTask;

/**
 * 事务引擎
 * @author Rick
 * 
 */
public class BusinessEngine extends Thread {
	private Logger logger = Logger.getLogger(this.getClass());

	private static BusinessEngine businessEngine;                 // 单例模式
	private static ScheduledThreadPoolExecutor executor = null;   // 线程池
	private static TaskManager taskManager = null;            // 任务管理器
	private static boolean shutdown;

	private BusinessEngine() {}
	private BusinessEngine(int poolSize) {
		executor = new ScheduledThreadPoolExecutor(poolSize);
		taskManager = new TaskManager();
	}

	/**
	 * 单例模式,只创建一个引擎
	 * @param poolSize
	 * @return
	 */
	public static synchronized BusinessEngine getInstance() {
		if (businessEngine == null) {
			businessEngine = new BusinessEngine(500);
		}
		return businessEngine;
	}

	/**
	 * 关闭事务引擎
	 */
	public static void shutdown(){
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
	public void runOnceTask(final BaseOnceTask task) {
		// ask thread pool to run a new task
		executor.execute(new Runnable() {
			public void run() {
				runTask(task);
			}
		});
	}

	/**
	 * 执行定时任务
	 * @param task
	 */
	public void runTimerTask(final BaseTimerTask task) {
		executor.schedule(new TimerTask() {
			public void run() {
				runTask(task);
			}
		}, task.getDelay(), TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		logger.info("bussiness-engine has start successfully...");
		// restore from manager
		taskManager.restore();

		while (true) {
			if(shutdown){break;}
			// get task from manager
			BaseTask task = (BaseTask) taskManager.getTask();
			if (task instanceof BaseOnceTask) {
				runOnceTask((BaseOnceTask) task);
			} else if (task instanceof BaseTimerTask) {
				runTimerTask((BaseTimerTask) task);
			}
		}
	}
}