package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.Payment;
import com.jsp.quiz_application.entity.PaymentVerifyRequest;
import com.jsp.quiz_application.entity.Quiz;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.repository.PaymentRepository;
import com.jsp.quiz_application.repository.QuizRepository;
import com.jsp.quiz_application.repository.UserRepository;
import com.jsp.quiz_application.service.EmailService;
import com.jsp.quiz_application.service.PdfService;
import com.jsp.quiz_application.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    @Value("${razorpay.key.secret}")
    private String secret;

    // 1️⃣ Create Razorpay Order
    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount,
                              @RequestParam Long userId,
                              @RequestParam Long quizId) throws Exception {

        Order order = razorpayService.createOrder(amount);

        User user = userRepository.findById(userId).orElseThrow();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        Payment payment = new Payment();
        payment.setOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setStatus("PENDING");

        payment.setUser(user);
        payment.setQuiz(quiz);

        paymentRepository.save(payment);

        return order.toString();
    }

    // 2️⃣ Verify Payment Signature

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody PaymentVerifyRequest request,
                                @RequestParam Long userId,
                                @RequestParam Long quizId) throws Exception {

        System.out.println("Verify API called");

        JSONObject attributes = new JSONObject();

        attributes.put("razorpay_order_id", request.getRazorpay_order_id());
        attributes.put("razorpay_payment_id", request.getRazorpay_payment_id());
        attributes.put("razorpay_signature", request.getRazorpay_signature());

        boolean isValid = Utils.verifyPaymentSignature(attributes, secret);

        System.out.println("Signature Valid: " + isValid);

        Payment payment = paymentRepository
                .findByOrderId(request.getRazorpay_order_id());

        if(payment == null){
            throw new RuntimeException("Payment not found");
        }

        payment.setPaymentId(request.getRazorpay_payment_id());
        payment.setSignature(request.getRazorpay_signature());

        if(isValid){

            payment.setStatus("SUCCESS");

            paymentRepository.save(payment);

            System.out.println("Payment saved");

            byte[] pdf = pdfService.generatePaymentReceipt(payment);

            System.out.println("PDF Generated");
            String html =
                    "<html>" +
                            "<body style='font-family:Arial;background:#f4f6f8;padding:40px'>" +

                            "<table align='center' width='600' style='background:white;border-radius:10px;padding:30px;text-align:center'>" +

                            "<tr>" +
                            "<td style='background:#1976d2;color:white;padding:20px;font-size:22px'>" +
                            "<b>PAYMENT SUCCESS</b>" +
                            "</td>" +
                            "</tr>" +

                            "<tr>" +
                            "<td style='padding:30px'>" +

                            "<h2>Payment Successful</h2>" +

                            "<p>Hello " + payment.getUser().getUserName() + "</p>" +

                            "<p>Order ID: " + payment.getOrderId() + "</p>" +

                            "<p>Amount Paid: ₹" + payment.getAmount() + "</p>" +

                            "<p>Your payment receipt is attached.</p>" +

                            "</td>" +
                            "</tr>" +

                            "</table>" +
                            "</body>" +
                            "</html>";
            emailService.sendEmailWithAttachment(
                    payment.getUser().getEmail(),
                    "Payment Successful",
                    html,
                    pdf,
                    "payment-receipt.pdf"
            );

            System.out.println("Email Sent");

        }
        else{

            payment.setStatus("FAILED");
            paymentRepository.save(payment);

        }

        return "Payment Processed";
    }
    @PostMapping("/retry")
    public String retryPayment(@RequestParam Long paymentId) throws Exception {

        Payment oldPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // create new razorpay order
        Order order = razorpayService.createOrder(oldPayment.getAmount());

        Payment newPayment = new Payment();

        newPayment.setOrderId(order.get("id"));
        newPayment.setAmount(oldPayment.getAmount());
        newPayment.setStatus("CREATED");

        newPayment.setUser(oldPayment.getUser());
        newPayment.setQuiz(oldPayment.getQuiz());

        newPayment.setCreatedBy(oldPayment.getUser());
        newPayment.setUpdatedBy(oldPayment.getUser());

        paymentRepository.save(newPayment);

        return order.toString();
    }
}