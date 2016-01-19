package com.rick.businessengine;

import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.rick.businessengine.task.BaseOnceTask;
import com.rick.businessengine.task.BaseTask;
import com.rick.businessengine.task.BaseTimerTask;

/**
 * 事务引擎
 * @author Rick
 *
 */
public class BusinessEngine{
	private static Executor threadPool;		     //线程池
	private static TaskManager taskManager;	 //任务管理器
	private static int poolSize=1000;            //线程池容量
	
	static{
		threadPool = Executors.newScheduledThreadPool(poolSize);
		taskManager = new TaskManager();
	}

	/**
	 * 向事务引擎递交任务
	 * @param task
	 */
	public static void addTask(BaseTask task) {
		taskManager.add(task, true);
	}

	/**
	 * 在同一个进程中执行任务
	 * @param task
	 */
	public static void runTask(BaseTask task) {
		task.exec();
		BaseTask next = task.getNext();
		if (next != null) {
			addTask(next);             //将下个任务递交给事务引擎
		}
		if (next != task){
			taskManager.remove(task);  //删除持久化文件
		}
	}

	/**
	 * 执行一次性任务
	 * @param task
	 */
	public static void runOnceTask(final BaseTask task) {
		threadPool.execute(new Runnable() {
			public void run() {
				runTask(task);
			}
		});
	}

	/**
	 * 执行定时任务
	 * @param task
	 */
	public static void runTimerTask(final BaseTimerTask task) {
		((ScheduledThreadPoolExecutor)threadPool).schedule(new TimerTask() {
			@Override
			public void run() {
				runTask(task);
			}
		}, task.getDelay(),TimeUnit.MILLISECONDS);
	}

	public static void start() {
		System.out.println("start engine");
		taskManager.restore();                 //restore from manager

		//添加
		while (true) {
			// get task from manager
			BaseTask task =taskManager.getTask();
			if (task instanceof BaseOnceTask) {
				runOnceTask(task);
			} else if (task instanceof BaseTimerTask) {
				runTimerTask((BaseTimerTask) task);
			}
		}
	}
}