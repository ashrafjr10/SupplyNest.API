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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Business customer;

}
