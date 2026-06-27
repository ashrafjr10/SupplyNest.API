package SupplyNest.Catalog.Service.repositories;

import SupplyNest.Catalog.Service.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
