package com.jsp.quiz_application.service;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    public FirebaseToken verifyToken(String token) throws Exception {

        return FirebaseAuth.getInstance().verifyIdToken(token);

    }

}
