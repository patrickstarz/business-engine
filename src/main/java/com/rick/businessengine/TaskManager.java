package com.rick.businessengine;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.rick.businessengine.persistent.PersistentStorage;
import com.rick.businessengine.persistent.PersistentStorageFactory;
import com.rick.businessengine.task.BaseTask;

/**
 * 任务管理器
 * @author Rick
 *
 */
public class TaskManager {
	private Logger logger = Logger.getLogger(this.getClass());

	// get one once
	private Semaphore lock = new Semaphore(0);
	// here needs a thread safe container
	private Queue<BaseTask> taskQueue;
	// storage needs thread safe
	private PersistentStorage storage;

	public TaskManager() {
		lock = new Semaphore(0);
		taskQueue = new LinkedBlockingQueue<BaseTask>();
		storage = PersistentStorageFactory.createPersistentStorage();
	}

	public void restore() {
		List<?> all = storage.restoreAll();
		for (Object o : all) {
			add((BaseTask) o, false);
		}
	}

	public BaseTask getTask() {
		BaseTask task = null;
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		task = (BaseTask) taskQueue.poll();
		return task;
	}

	public void add(BaseTask task, boolean isStore) {
		if (!taskQueue.contains(task)) {
			taskQueue.add(task);
			if (isStore) {
				storage.store(task.getStoreName(), task);
			}
			lock.release();
			logger.info("++++++++ Add a new task: " + task.getStoreName());
		}
	}

	public void remove(BaseTask task) {
		storage.remove(task.getStoreName());
	}
}