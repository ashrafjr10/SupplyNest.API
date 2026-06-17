package Supplynest.Auth.Service.repositories;

import Supplynest.Auth.Service.models.UserLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserLogsRepository extends JpaRepository<UserLogs, UUID> {
}
