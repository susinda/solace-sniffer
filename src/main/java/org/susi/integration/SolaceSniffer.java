package org.susi.integration;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.annotation.JmsListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;  


@SpringBootApplication
@DependsOn("expressionResolver")
public class SolaceSniffer {

	@Value("${spring.cloud.azure.cosmos.endpoint}")
	private String cosmosHost;

	@Value("${spring.cloud.azure.cosmos.database}")
	private String cosmosDatabase;

	@Value("${cosmos.ContainerName}")
	private String cosmosContainer;
	
	@Autowired
	private LogRepository logRepository;

	public static void main(String[] args) {
		SpringApplication.run(SolaceSniffer.class, args);
	}

	@PostConstruct
	private void runAfterLoaded() {
		System.out.println(
				"Connecting to host:" + cosmosHost + " database: " + cosmosDatabase + " container: " + cosmosContainer);
	}

	@JmsListener(destination = "${log.destination}")
	public void handle(Message message) {

		Date receiveTime = new Date();

		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			MessageLog mLog = null;
			JsonNode dataMsg =  null;
			try {
				dataMsg = new ObjectMapper().readTree(tm.getText());
			} catch (Exception e0) {
				
			}
			
			try {
				if (dataMsg == null) {
					JSONObject jObj = new JSONObject();
					jObj.put("payload", tm.getText());
					String jString = jObj.toString();
					dataMsg = new ObjectMapper().readTree(jString);
				}
				String destination = tm.getJMSDestination().toString();
				String correlationId = tm.getJMSCorrelationID();
				long timeStamp = tm.getJMSExpiration();
				boolean isReply = tm.getBooleanProperty("Solace_JMS_Prop_IS_Reply_Message");
				String id = correlationId.replace("ID:Solace-", "");
				if (isReply) {
					id = "RES_" + id;
				} else {
					id = "REQ_" + id;
				}
				mLog = new MessageLog(id, destination, dataMsg, correlationId, isReply, timeStamp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				System.out.println(
						"Message Received at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
								+ " with message content of: " + tm.getText());
				if (mLog != null) {
					 logRepository.save(mLog).then().block();
					 System.out.println("log saved");
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(message.toString());
		}
	}

}
