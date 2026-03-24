package com.logistics.customer.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * Entité Service Level Agreement (SLA) définissant les engagements d'un contrat logistique.
 */
@Entity
@Table(name = "slas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceLevelAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Délai de livraison maximal garanti en jours. */
    @Column(nullable = false)
    private int guaranteedDeliveryDays;

    /** Taux de pénalité par jour de retard (en pourcentage). */
    @Column(nullable = false)
    private double penaltyRate;

    @OneToOne(mappedBy = "sla")
    private Contract contract;
}
