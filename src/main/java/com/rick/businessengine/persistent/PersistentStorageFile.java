package com.rick.businessengine.persistent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import com.rick.businessengine.task.BaseTask;

public class PersistentStorageFile implements PersistentStorage {

	String path = "./store/";

	public PersistentStorageFile() {
		File f = new File(path);
		if (f.exists() != true) {
			f.mkdir();
		}
	}

	@Override
	public boolean store(String key, Object obj) {
		File f = new File(path + key);
		ObjectOutputStream out = null;
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			out = new ObjectOutputStream(new FileOutputStream(f));
			out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out!=null){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public void remove(String key) {
		File f = new File(path + key);
		if (f.exists()) {
			f.delete();
		}
	}

	@Override
	public List<BaseTask> restoreAll() {
		List<BaseTask> list = new LinkedList<BaseTask>();
		File dir = new File(path);
		if (dir.exists() != true) {
			dir.mkdir();
		} else {
			File[] files = dir.listFiles();
			InputStream in = null;
			ObjectInputStream ois = null;
			for (File f : files) {
				try {
					in = new FileInputStream(f);
					ois = new ObjectInputStream(in);
					// 为对象输出流实例化
					Object obj = ois.readObject();
					// 保存对象到文件
					list.add((BaseTask) obj);
					// 关闭输出
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (in != null) {
							in.close();
						}
						if (ois != null) {
							ois.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}
}