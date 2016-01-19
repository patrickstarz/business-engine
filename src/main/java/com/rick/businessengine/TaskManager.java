package com.rick.businessengine;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import com.rick.businessengine.persistent.PersistentStorage;
import com.rick.businessengine.persistent.PersistentStorageFactory;
import com.rick.businessengine.task.BaseTask;

/**
 * manager of task mainly manage the
 * order,add,remove,persistence,anti-persistence of the task queue
 * @author Rick
 *
 */
public class TaskManager {
	// here needs a thread safe container
	private Queue<BaseTask> taskQueue;
	// storage needs thread safe
	private PersistentStorage storage;
	//
	private Semaphore lock;

	public TaskManager() {
		taskQueue = new LinkedBlockingQueue<BaseTask>();
		storage = PersistentStorageFactory.createPersistentStorage();
		lock = new Semaphore(0);
	}

	/**
	 * restore tasks from persistence
	 */
	public void restore() {
		List<?> all = storage.restoreAll();
		for (Object o : all) {
			add((BaseTask) o, false);
		}
	}

	/**
	 * poll a task from task queue 
	 * @return
	 */
	public BaseTask getTask() {
		BaseTask task = null;
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		task = (BaseTask) taskQueue.poll();
		System.out.println("get from taskmanager: " + task.getStoreName());
		return task;
	}

	/**
	 * 
	 * @param task
	 * @param isStore
	 */
	public void add(BaseTask task, boolean isStore) {
		if (!taskQueue.contains(task)) {
			taskQueue.add(task);
			if (isStore) {
				storage.store(task.getStoreName(), task);
			}
			lock.release();
			System.out.println("add to taskmanager: " + task.getStoreName());
		}
	}

	public void remove(BaseTask task) {
		storage.remove(task.getStoreName());
	}
}