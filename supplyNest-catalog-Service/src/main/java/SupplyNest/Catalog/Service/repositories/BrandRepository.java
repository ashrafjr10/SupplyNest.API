package SupplyNest.Catalog.Service.repositories;

import SupplyNest.Catalog.Service.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Optional<Brand> findByBusinessId(UUID businessId);
}
