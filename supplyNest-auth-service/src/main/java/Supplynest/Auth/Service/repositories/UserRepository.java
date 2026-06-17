package Supplynest.Auth.Service.repositories;

import Supplynest.Auth.Service.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.mobileNumber = :phoneOrEmail OR u.email = :phoneOrEmail")
    Optional<User> findByMobileNumberOrEmail(@NotBlank(message = "Phone Number or Email or Username is required") String phoneOrEmail);
}
