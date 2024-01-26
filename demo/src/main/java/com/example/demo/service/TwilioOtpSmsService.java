package com.example.demo.service;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioOtpSmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void sendOtpSms(String toPhoneNumber, String otp) {
        System.out.println("otp="+otp);
        System.out.println("toPhoneNumber="+toPhoneNumber);
        try{
            System.out.println("otp="+otp);
            System.out.println("toPhoneNumber="+toPhoneNumber);

            Twilio.init(accountSid, authToken);

            String messageBody = "Your OTP for verification: " + otp;
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();  // Add a semicolon here
        } catch (ApiException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}
