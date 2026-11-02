//package org.example.group2siouxbackend.business;
//
//import com.postmarkapp.postmark.Postmark;
//import com.postmarkapp.postmark.client.ApiClient;
//import com.postmarkapp.postmark.client.data.model.message.Message;
//
//import com.postmarkapp.postmark.client.exception.PostmarkException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    private final ApiClient postmarkClient;
//
//    public EmailService(@Value("${postmark.api-key}") String apiKey) {
//        this.postmarkClient = Postmark.getApiClient(apiKey);
//    }
//
//    public boolean sendVerificationEmail(String recipient, String packageId) {
//        Message message = new Message(
//                "551252@student.fontys.nl",
//                recipient,
//                "Package Verification",
//                "Your package with ID " + packageId + " has been scanned."
//        );
//
//        message.setReplyTo("551252@student.fontys.nl");
//
//        try {
//            postmarkClient.deliverMessage(message);
//            return true;
//        } catch (Exception e) {  // catches PostmarkException or any other
//            e.printStackTrace();  // <- log it so you see why it failed
//            return false;
//        }
//    }
//}
//
