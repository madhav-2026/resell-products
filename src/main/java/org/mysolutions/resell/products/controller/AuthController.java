package org.mysolutions.resell.products.controller;

import org.mysolutions.resell.products.entities.Users;
import org.mysolutions.resell.products.model.OtpRequest;
import org.mysolutions.resell.products.model.PhoneRequest;
import org.mysolutions.resell.products.model.RegisterRequest;
import org.mysolutions.resell.products.repositories.UserRepository;
import org.mysolutions.resell.products.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsService smsService;

    // 1. Check if phone exists
    @PostMapping("/check-phone")
    public boolean checkPhone(@RequestBody PhoneRequest request) {
        return userRepository.findByPhone(request.getPhone()).isPresent();
    }

    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone) {
        Optional<Users> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", user.getName());
            userInfo.put("address", user.getAddress());
            userInfo.put("phone", user.getPhone());
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // 2. Send OTP
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody PhoneRequest request) {
        Optional<Users> userOptional = userRepository.findByPhone(request.getPhone());
        if (userOptional.isEmpty()) return "User not found";
        String otp = "1230";
        Users user = userOptional.get();
        user.setOtp(otp);
        userRepository.save(user);
        //smsService.sendSms(user.getPhone(), "Your OTP is: " + otp);
        System.out.println("OTP for " + user.getPhone() + ": " + otp);
        return "OTP sent";
    }

    // 3. Register
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepository.findByPhone(request.getPhone()).isPresent()) return "User already exists";
        Users user = new Users();
        user.setPhone(request.getPhone());
        user.setName(request.getName());
        String otp = String.valueOf(new Random().nextInt(8999) + 1000);
        user.setOtp(otp);
        user.setVerified(false);
        userRepository.save(user);
        smsService.sendSms(user.getPhone(), "Your OTP is: " + otp);
        System.out.println("OTP for " + user.getPhone() + ": " + otp);
        return "Registered and OTP sent";
    }

    // 4. Verify OTP
    @PostMapping("/verify-otp")
    public boolean verifyOtp(@RequestBody OtpRequest request) {
//        Optional<Users> userOptional = userRepository.findByPhone(request.getPhone());
//        if (userOptional.isPresent() && userOptional.get().getOtp().equals(request.getOtp())) {
//            Users user = userOptional.get();
//            user.setVerified(true);
//            userRepository.save(user);
//            return true;
//        }
//        return false;
        return true;
    }
}

