package com.skilladmin.serviceImpl;

import com.phonepe.sdk.pg.common.PhonePeClient;
import com.phonepe.sdk.pg.common.http.PhonePeException;
import com.phonepe.sdk.pg.common.http.PhonePeResponse;
import com.phonepe.sdk.pg.payments.v1.models.request.PgPayRequest;
import com.phonepe.sdk.pg.payments.v1.models.response.PayPageInstrumentResponse;
import com.phonepe.sdk.pg.payments.v1.models.response.PgPayResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import com.skilladmin.dto.PaymentRequest;
import com.skilladmin.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.skilladmin.service.PhonePayService;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import java.util.Base64;

@Slf4j
@Service
public class PhonePayServiceImpl implements PhonePayService {


    @Value("${phonepe.merchant-id}")
    private String merchantId;

    @Value("${phonepe.callback-url}")
    private String callbackUrl;


    // Assume you have a PhonePe client initialized


    @Value("${phonepe.salt-key}")
    private String saltKey;

    private final RestTemplate restTemplate;


    public PhonePayServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

  

    public Object initiatePayment(PaymentRequest paymentRequest) {
        try {
            String merchantTransactionId = "M" + System.currentTimeMillis();
            String payload = createPayload(paymentRequest, merchantTransactionId);
            String checksum = generateChecksum(payload, saltKey, 1);
            log.info("xverify" + checksum);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-VERIFY", checksum);

            String url = "https://api.phonepe.com/apis/hermes/pg/v1/pay";
            String payloadBase64 = Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            String requestData = "{\"request\":\"" + payloadBase64 + "\"}";

            HttpEntity<String> entity = new HttpEntity<>(requestData, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Return redirect URL for payment gateway
            return response.getBody();
        } catch (Exception e) {
            return new PaymentResponse("error", e.getMessage());
        }
    }

//    public Object checkPaymentStatus(String txnId) {
//        try {
//            String checksum = generateChecksum("/pg/v1/status/" + merchantId + "/" + txnId);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("X-VERIFY", checksum);
//            headers.add("X-MERCHANT-ID", merchantId);
//
//            String url = "https://api.phonepe.com/apis/hermes/pg/v1/status/" + merchantId + "/" + txnId;
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//            return response.getBody();
//        } catch (Exception e) {
//            return new PaymentResponse("error", e.getMessage());
//        }
//    }

    private String createPayload(PaymentRequest paymentRequest, String merchantTransactionId) {
        // Create the payment payload as per the PhonePe API specifications
        return "{"
                + "\"merchantId\":\"" + merchantId + "\","
                + "\"merchantTransactionId\":\"" + merchantTransactionId + "\","
                + "\"merchantUserId\":\"MUID" + System.currentTimeMillis() + "\","
                + "\"amount\":" + (paymentRequest.getAmount() * 100) + ","
                + "\"redirectUrl\":\"" + paymentRequest.getUrl() + "\","
                + "\"redirectMode\":\"POST\","
                + "\"paymentInstrument\": {\"type\": \"PAY_PAGE\"}"
                + "}";
    }

//    private String generateChecksum(String payload) throws Exception {
//        String string = payload + "/pg/v1/pay" + saltKey;
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
//        String checksum = new String(Base64.getEncoder().encode(hash));
//        return checksum + "###" + 1;
//    }


    // Method to generate checksum
    public static String generateChecksum(String payload, String saltKey, int saltIndex) throws Exception {
        // Step 1: Base64 encode the payload
        String base64Payload = Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // Step 2: Concatenate base64Payload + "/pg/v1/pay" + saltKey
        String stringToHash = base64Payload + "/pg/v1/pay" + saltKey;

        // Step 3: Generate SHA-256 hash of the concatenated string
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));

        // Convert hash bytes to hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        // Step 4: Append "###" + saltIndex to the hash
        return hexString.toString() + "###" + saltIndex;
    }

	@Override
	public Object checkPaymentStatus(String txnId) {
		// TODO Auto-generated method stub
		return null;
	}
}

