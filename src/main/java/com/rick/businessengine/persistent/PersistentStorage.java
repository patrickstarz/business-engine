package com.rick.businessengine.persistent;

import java.util.List;

import com.rick.businessengine.task.BaseTask;
/**
 * task持久化,防止宕机
 * 可能是文件的形式,或数据库,如mongoDB或MySQL
 * @author Rick
 *
 */
public interface PersistentStorage {
	// return Id
	public boolean store(String key, Object obj);

	public void remove(String id);

	List<BaseTask> restoreAll();
}