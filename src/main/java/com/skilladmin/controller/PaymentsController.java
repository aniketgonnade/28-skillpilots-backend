package com.skilladmin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilladmin.dto.PaymentRequest;
import com.skilladmin.dto.PaymentResponse;
import com.skilladmin.model.PaymentHistory;
import com.skilladmin.model.User;
import com.skilladmin.repository.PayementHistoryRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.PhonePayService;
import com.skilladmin.service.UserService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "*" })

public class PaymentsController {

	private static final String CASHFREE_API_URL = "https://api.cashfree.com/pg/orders";
     //production
	 private static final String CLIENT_ID = "776689d42f191ff3e94d70f486986677";
 private static final String CLIENT_SECRET ="cfsk_ma_prod_f15b6a714c430ac9172c4ce29d49dd1a_b3217ffd";
	 

//	private static final String CLIENT_ID = "TEST10325813c45c848ac662fcaedc5531852301";
	//private static final String CLIENT_SECRET = "cfsk_ma_test_c68c8a5bd31d3a184f44e5cf3b12dc93_76c0dc26";

	private final OkHttpClient client = new OkHttpClient();
	@Autowired
	private PhonePayService paymentService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PayementHistoryRepository payementHistoryRepository;

	// Initiates the payment
	@PostMapping("/new")
	public PaymentResponse initiatePayment(@RequestBody PaymentRequest paymentRequest) {

		try {
			// Call the service to initiate the payment
			String paymentUrl = String.valueOf(paymentService.initiatePayment(paymentRequest));
			return new PaymentResponse("success", paymentUrl);
		} catch (Exception e) {
			return new PaymentResponse("error", e.getMessage());
		}
	}

	// Check the status of the payment
//    @GetMapping("/status/{txnId}")
//    public PaymentResponse checkPaymentStatus(@PathVariable String txnId) {
//        try {
//            String status = (String) paymentService.checkPaymentStatus(txnId);
//            return new PaymentResponse("success", status);
//        } catch (Exception e) {
//            return new PaymentResponse("error", e.getMessage());
//        }
//    }

	
	// CREATE ORDER API 
	@PostMapping("/createOrder")
	public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<String, String> orderData) {
		OkHttpClient client = new OkHttpClient();

		String orderAmount = orderData.getOrDefault("orderAmount", "10.00");
		String orderId = orderData.getOrDefault("orderId", "default_order_id");
		String customerName = orderData.getOrDefault("customerName", "John Doe");
		String email = orderData.get("email");
		String customerPhone = orderData.getOrDefault("customerPhone", "7705480780");
		String returnUrl = orderData.getOrDefault("returnUrl", "https://yourwebsite.com/return");
		String notifyUrl = orderData.getOrDefault("notifyUrl", "https://yourwebsite.com/notify");

		// 🔹 Validate required fields
		if (email == null || email.isEmpty() || orderAmount == null || orderId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", "Missing required parameters"));
		}

		// 🔹 Create JSON Request Body
		String jsonBody = "{" + "\"order_amount\": " + orderAmount + "," + "\"order_currency\": \"INR\","
				+ "\"customer_details\": {" + " \"customer_id\": \"" + orderId + "\"," + " \"customer_name\": \""
				+ customerName + "\"," + " \"customer_email\": \"" + email + "\"," + " \"customer_phone\": \""
				+ customerPhone + "\"" + "}," + "\"order_meta\": {" + " \"return_url\": \"" + returnUrl + "\","
				+ " \"notify_url\": \"" + notifyUrl + "\"" + "}" + "}";

		System.out.println("Request JSON: " + jsonBody); // Debugging log

		okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json"), jsonBody);
		Request request = new Request.Builder().url(CASHFREE_API_URL).post(body)
				.addHeader("Content-Type", "application/json").addHeader("x-client-id", CLIENT_ID)
				.addHeader("x-client-secret", CLIENT_SECRET).addHeader("x-api-version", "2022-09-01") // Ensure the
																										// correct
																										// version
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				String errorResponse = response.body().string();
				System.out.println("Cashfree API Error: " + errorResponse);
				return ResponseEntity.status(response.code())
						.body(Collections.singletonMap("error", "Payment API Error: " + errorResponse));
			}

			// 🔹 Parse JSON Response
			String responseBody = response.body().string();
			System.out.println("Cashfree Response: " + responseBody);
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
			});

			// 🔹 Extract important details
			String paymentSessionId = responseMap.getOrDefault("payment_session_id", "N/A").toString();
			String transactionId = responseMap.getOrDefault("transaction_id", "N/A").toString();
			String paymentType = responseMap.getOrDefault("payment_method", "N/A").toString();

			// 🔹 Construct Payment URL
			String paymentUrl = "https://payments.cashfree.com/session/" + paymentSessionId;

			// 🔹 Fetch user from DB
			User user = userRepository.findByEmail(email);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Collections.singletonMap("error", "User not found"));
			}

			// 🔹 Save payment details in DB
			PaymentHistory paymentHistory = new PaymentHistory();
			paymentHistory.setStatus("pending");
			paymentHistory.setUserId(user.getId());
			paymentHistory.setPaidAmount(Double.parseDouble(orderAmount));
			payementHistoryRepository.save(paymentHistory);

			// 🔹 Return the Payment URL
			Map<String, String> result = new HashMap<>();
			result.put("payment_url", paymentUrl);
			result.put("sessionId", paymentSessionId);
			result.put("transaction_id", transactionId);
			result.put("payment_type", paymentType);

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IOException e) {
			System.err.println("Exception while calling Cashfree API: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Internal server error"));
		}
	}

}
