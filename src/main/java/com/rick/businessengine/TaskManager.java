package com.rick.businessengine;

import java.util.List;
import java.util.concurrent.DelayQueue;

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

	// here needs a thread safe container
	private DelayQueue<BaseTask> taskQueue;
	// storage needs thread safe
	private PersistentStorage storage;

	public TaskManager() {
		taskQueue = new DelayQueue<BaseTask>();
		storage = PersistentStorageFactory.createPersistentStorage();
	}

	public synchronized void restore() {
		List<?> all = storage.restoreAll();
		for (Object o : all) {
			add((BaseTask) o, false);
		}
	}

	public BaseTask getTask() throws InterruptedException {
		return (BaseTask) taskQueue.take();
	}

	public synchronized void add(BaseTask task, boolean isStore) {
		if (!taskQueue.contains(task)) {
			taskQueue.add(task);
			if (isStore) {
				storage.store(task.getStoreName(), task);
			}
			logger.info("++++++++ Add a new task: " + task.getStoreName());
		}
	}

	public synchronized void remove(BaseTask task) {
		storage.remove(task.getStoreName());
	}
}