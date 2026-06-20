package SupplyNest.Business.Service.repositories;

import SupplyNest.Business.Service.models.BusinessGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessGroupRepository extends JpaRepository<BusinessGroup, UUID> {
    boolean existsByBusinessGroupCode(String s);
}
