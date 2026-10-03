package com.nic.svpnpa.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class SmsService_old{

    @Value("${sms.provider.url}")
    private String url;

    @Value("${sms.provider.user}")
    private String user;

    @Value("${sms.provider.password}")
    private String password;

    @Value("${sms.provider.sid}")
    private String sid;

    public void sendSms(String to, String message) {
        String apiUrl = url + "?user=" + user +
                "&passwd=" + password +                
                "&mobilenumber=" + to ;
//                "&message=" + message + 
//                "&sid=" + smsProviderSid;
        
        String apiUrl1 = url + "?user=" + user +
                "&passwd=" + password ;
        
//      System.out.println("apiUrl=="+apiUrl.replaceAll("\\%20", " "));         
//      String encodedUri = UriComponentsBuilder.fromHttpUrl(apiUrl).build().toUriString();
      
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
}
