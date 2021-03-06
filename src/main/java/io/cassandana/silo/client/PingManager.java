/*
 *  Copyright 2018 Mohammad Taqi Soleimani
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package io.cassandana.silo.client;

import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingReq;

import io.cassandana.silo.scheduler.IScheduler;
import io.cassandana.silo.scheduler.Scheduler;

public class PingManager implements MqttPingSender, IScheduler {

	private ClientComms clientComms;
	
	private Scheduler timer;
	
	private String clientId;
	
	
	@Override
	public void init(ClientComms clientComms) {
		this.clientComms = clientComms;
	}

	@Override
	public void schedule(long delayInMilliseconds) {

	}
	
	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public void pingreq() {
		try {
	        MqttDeliveryToken token = new MqttDeliveryToken(clientId);
	        MqttPingReq pingMsg = new MqttPingReq();
	        clientComms.sendNoWait(pingMsg, token);
		} catch (Exception e) {

		}
    }
	
	
	
	public void startPingScheduler(int interval) {
		timer = new Scheduler(interval, 1, TimeUnit.SECONDS);
		timer.addToSchedulerList(this);
	}
	
	public void stopPingScheduler() {
		timer.stopTimer();
	}


	@Override
	public void onTimer() {
		pingreq();
	}

}
