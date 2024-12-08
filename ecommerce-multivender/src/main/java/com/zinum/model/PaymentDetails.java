package com.zinum.model;

import com.zinum.enums.PaymentStatus;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {

    String paymentId;
    String razorpayPaymentLink;
    String razorpayPaymentLinkReferenceId;
    String razorpayPaymentLinkStatus;
    String razorpayPaymentIdZWSP;
    PaymentStatus paymentStatus;
}
