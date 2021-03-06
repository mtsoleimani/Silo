/*
 *  Copyright 2018 Mohammad Taqi Soleimani
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */

package io.cassandana.silo.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import io.cassandana.silo.client.MqttConf;
import io.cassandana.silo.database.DatabaseEngine;

public class Configuration {
	
	private final String confFilePath = "./silo.yaml";
	
	private static Configuration instance;

	public static Configuration getInstance() throws Exception {
		if(instance == null)
			instance = new Configuration();
		return instance;
	}


	private Configuration() throws Exception {
		if(!parse()) throw new Exception("bad conf file");
		mqttConf = new MqttConf();
		mqttConf.setUsername(mqttUsername);
		mqttConf.setPassword(mqttPassword);
		mqttConf.setClientId(mqttClientId);
		mqttConf.setHost(mqttHost);
		mqttConf.setPort(mqttPort);
		mqttConf.setTopics(topics);
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean parse() {
		
		Yaml yaml = new Yaml();
		
		try (InputStream input = new FileInputStream(new File(confFilePath))) {
			
			Map<String, Object> parser = (Map<String, Object>) yaml.load(input);
			
			threads = (int) parser.get(Constants.THREADS);
			if(threads <= 0)
				threads = Runtime.getRuntime().availableProcessors();
			
			Map<String, Object> bulk = (Map<String, Object>) parser.get(Constants.BULK);
			if(bulk != null) {
				bulkIntervalSeconds = (int) bulk.get(Constants.INTERVAL);
				bulkCount = (int) bulk.get(Constants.COUNT);
			} else {
				bulkIntervalSeconds = 5;
				bulkCount = 100;
			}
			
			Map<String, Object> mqtt = (Map<String, Object>) parser.get(Constants.MQTT);
			if(mqtt != null) {
				mqttHost = mqtt.get(Constants.HOST).toString();
				mqttUsername = mqtt.get(Constants.USERNAME).toString();
				mqttClientId = mqtt.get(Constants.CLIENT_ID).toString();
				mqttPassword = mqtt.get(Constants.PASSWORD).toString();
				mqttPort = (int) mqtt.get(Constants.PORT);
				if(mqttPort <= 0 || mqttPort >= 65535)
					return false;
				
				topics = (List<String>) mqtt.get(Constants.TOPICS);
				if(topics == null)
					topics = Arrays.asList("#");
				
			} else {
				return false;
			}
			
			Map<String, Object> database = (Map<String, Object>) parser.get(Constants.DATABASE);
			if(database != null) {
				dbHost = database.get(Constants.HOST).toString();
				if(database.get(Constants.USERNAME) != null)
					dbUsername = database.get(Constants.USERNAME).toString();
				
				if(database.get(Constants.PASSWORD) != null)
					dbPassword = database.get(Constants.PASSWORD).toString();
				
				dbName = database.get(Constants.NAME).toString();
				
				dbPort = (int) database.get(Constants.PORT);
				if(dbPort <= 0 || dbPort >= 65535)
					return false;
				
				
				String engine = database.get(Constants.ENGINE).toString();
				if(engine.equalsIgnoreCase(Constants.MYSQL))
					dbEngine = DatabaseEngine.MYSQL;
				else if(engine.equalsIgnoreCase(Constants.POSTGRES))
					dbEngine = DatabaseEngine.POSTGRES;
				else if(engine.equalsIgnoreCase(Constants.MONGODB))
					dbEngine = DatabaseEngine.MONGODB;
				else if(engine.equalsIgnoreCase(Constants.CASSANDRA)) {
					dbEngine = DatabaseEngine.CASSANDRA;
					hasBulk = false;
				} else
					return false;
				
			} else {
				return false;
			}
			
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	
	public int threads = Runtime.getRuntime().availableProcessors();
	
	public int bulkIntervalSeconds = 5;
	public int bulkCount = 100;
	
	
	public String dbHost = Constants.LOCAL_HOST;
	public int dbPort = Constants.DEFAULT_MYSQL_PORT;
	public String dbUsername;
	public String dbPassword;
	public String dbName;
	
	
	public MqttConf mqttConf;
	
	public DatabaseEngine dbEngine = DatabaseEngine.UNKNOWN;
	public String mqttUsername;
	public String mqttPassword;
	public String mqttClientId;
	public String mqttHost = Constants.LOCAL_HOST;
	public int mqttPort = Constants.DEFAULT_MQTT_PORT;
	public List<String> topics;
	
	public boolean hasBulk = true;

}
