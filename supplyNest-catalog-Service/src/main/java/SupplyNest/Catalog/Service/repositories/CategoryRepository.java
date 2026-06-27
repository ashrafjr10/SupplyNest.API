package SupplyNest.Catalog.Service.repositories;

import SupplyNest.Catalog.Service.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByBusinessId(UUID businessId);
}
