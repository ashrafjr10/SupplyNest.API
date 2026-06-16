package SupplyNest.Business.Service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "subscribe_business")
public class SubscribeBusiness extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID subscribeBusinessId;

    @NotNull(message = "Business Id is required")
    private UUID businessId;

    @NotNull(message = "Customer Id is required")
    private UUID customerId;
}
