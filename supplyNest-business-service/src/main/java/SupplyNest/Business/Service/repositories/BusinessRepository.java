package SupplyNest.Business.Service.repositories;

import SupplyNest.Business.Service.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    boolean existsByBusinessCode(String businessCode);
}
