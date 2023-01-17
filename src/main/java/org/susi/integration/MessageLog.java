package org.susi.integration;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;

@Container(containerName = "${cosmos.ContainerName}")
public class MessageLog {

	@Id
	private String id;
	@PartitionKey
	private String destination;
	private JsonNode message;
	private String correlationId;
	private boolean isReply;
	private long timeStamp;
	
	public MessageLog(){
		
	}
	

	public MessageLog(String id, String destination, JsonNode message, String correlationId, boolean isReply, long timeStamp) {
		super();
		this.id = id;
		this.destination = destination;
		this.message = message;
		this.correlationId = correlationId;
		this.isReply = isReply;
		this.timeStamp = timeStamp;
	}


	@Override
	public String toString() {
		return "MessageLog [id=" + id + ", destination=" + destination + ", message=" + message + ", correlationId="
				+ correlationId + ", isReply=" + isReply + "]";
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public Object getMessage() {
		return message;
	}
	public void setMessage(JsonNode message) {
		this.message = message;
	}
	
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	public boolean isReply() {
		return isReply;
	}
	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}


}
