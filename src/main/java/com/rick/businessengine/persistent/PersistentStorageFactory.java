package com.rick.businessengine.persistent;

public class PersistentStorageFactory {

	public static PersistentStorage createPersistentStorage() {
		return new PersistentStorageFile();
	}
}