package com.example.demo.controller;

import com.example.demo.payload.OtpGenerator;
import com.example.demo.payload.OtpRequest;
import com.example.demo.payload.OtpVerificationRequest;
import com.example.demo.service.TwilioOtpSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class OtpController {

    private final TwilioOtpSmsService twilioSmsService;
    private final Map<String, String> otpStore = new HashMap<>(); // Map to store phone number and OTP

    @Autowired
    public OtpController(TwilioOtpSmsService twilioSmsService) {
        this.twilioSmsService = twilioSmsService;
    }
  //http://localhost:8080/api/send-otp
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest otpRequest) {
        String phoneNumber = otpRequest.getPhoneNumber();
        String otp = OtpGenerator.generateOtp();
        otpStore.put(phoneNumber, otp); // Store OTP in the map
        twilioSmsService.sendOtpSms(phoneNumber, otp);
        return ResponseEntity.ok("OTP sent successfully"+otp);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest verificationRequest) {
        String phoneNumber = verificationRequest.getPhoneNumber();
        String otp = verificationRequest.getOtp();

        // Retrieve the stored OTP for the given phone number
        String storedOtp = otpStore.get(phoneNumber);

        if (storedOtp.equals(otp)) {
            otpStore.remove(phoneNumber); // OTP verified, remove it from the store
            return ResponseEntity.ok("OTP verification successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP verification failed");
        }
    }
}
