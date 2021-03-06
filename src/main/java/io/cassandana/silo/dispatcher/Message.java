/*
 *  Copyright 2018 Mohammad Taqi Soleimani
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */


package io.cassandana.silo.dispatcher;


public class Message {

	public Message() {

	}

	public Message(String topic, String message) {
		this.topic = topic;
		this.message = message;
	}

	public String topic;

	public String message;

	public long receivedAt = System.currentTimeMillis();

}
