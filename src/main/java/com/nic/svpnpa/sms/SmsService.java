package com.nic.svpnpa.sms;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;


@Service
public class SmsService{

    @Value("${sms.provider.url}")
    private String url;

    @Value("${sms.provider.user}")
    private String user;

    @Value("${sms.provider.password}")
    private String password;

    @Value("${sms.provider.sid}")
    private String sid;
    
    final String res = "";

    public void sendSms(String to, String message) {
        String apiUrl = url + "?user=" + user +
                "&passwd=" + password +                
                "&mobilenumber=" + to ;
//                "&message=" + message + 
//                "&sid=" + smsProviderSid;
        
        String apiUrl1 = url + "?user=" + user +
                "&passwd=" + password ;        
      
      try {
        WebClient.create()
                .get()
                .uri(apiUrl + "&message={message}" + "&sid={sid}",message,sid)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        response -> {
                            // Handle the response as needed
                            System.out.println("SMS sent successfully. Response: " + response);                            
                           String result = response.toString();
                        },
                        error -> {
                            // Handle the error
                            System.out.println("Failed to send SMS. Error: " + error.getMessage());
                            String result = error.getMessage();                            
                        }
                );
        
	    }catch(Exception e) {
	    }          
      
    }               

    
    public String sendSms1(String text) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String apiUrl = "https://panini.ai.nic.in/translation" ;   
        
        String sourceLanguage = "eng_Latn";
        String targetLanguage = "hin_Deva";
     // Request body parameters
        String requestBody = String.format("{\"text\": \"%s\", \"src_lan\": \"%s\", \"tar_lan\": \"%s\"}",
                text, sourceLanguage, targetLanguage);

        System.out.println("requestBody==="+requestBody);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
      
//      try {
//        WebClient.create()
//                .get()
//                .uri(apiUrl, HttpMethod.POST, requestEntity, String.class)
//                .retrieve()
//                .bodyToMono(String.class)
//                .subscribe(
//                        response -> {
//                            // Handle the response as needed                            
//                           String result = response.toString();                           
//                        },
//                        error -> {
//                            // Handle the error                            
//                            String result = error.getMessage();
//                        }
//                );
//        
//	    }catch(Exception e) {
//	      e.printStackTrace();
//	    } 
        
        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        RequestEntity request = RequestEntity
        	     .post(URI.create("https://panini.ai.nic.in/translation"))
        	     .accept(MediaType.APPLICATION_JSON)
        	     .body(requestBody);
        	 ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
        	 
        	 if (responseEntity.getStatusCode().is2xxSuccessful()) {
                 return responseEntity.getBody();
             } else {
                 // Handle error response
                 return "Error: " + responseEntity.getStatusCodeValue();
             }      
      
    }
}
