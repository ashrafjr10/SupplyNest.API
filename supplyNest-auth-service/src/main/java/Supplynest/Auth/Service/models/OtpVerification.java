package Supplynest.Auth.Service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp_verifications")
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String mobileNumber;

    private String otp;

    private LocalDateTime expiresAt;
}
