/*
 *  Copyright 2018 Mohammad Taqi Soleimani
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package io.cassandana.silo.database;

import java.util.List;

import io.cassandana.silo.dispatcher.Message;

public interface DatabaseOperation {

	
	public void onConnected();

	public DatabaseOperation connect();

	public  void shutdown();

	public boolean isConnected();
	
	public void bulkInsert(List<Message> list);
	
	public void insert(Message message);
}
