package com.cjc.laptophub.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Integer orderId;

    private Double amount;

    private String paymentMethod;     // COD, UPI, CARD

    private String paymentStatus;     // PENDING, SUCCESS, FAILED

    private String transactionId;

    private LocalDateTime paidAt;
}