package Supplynest.Auth.Service.models;

import Supplynest.Auth.Service.enums.modelEnums;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_logs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User groupUser;

    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;

    private String loginIp;
    private String loginDevice;
    private String loginBrowser;

    private String logoutIp;
    private String logoutDevice;
    private String logoutBrowser;

    @Enumerated(EnumType.STRING)
    private modelEnums.LoginStatus status;

    private String failureReason;

    @PrePersist
    protected void onCreate() {
        if (loginTime == null) {
            loginTime = LocalDateTime.now();
        }
    }
}

