package com.logistics.customer.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Agrégat Racine (Aggregate Root) représentant une entreprise cliente ou prestataire.
 * 
 * Liée à un utilisateur Keycloak via keycloakUserId.
 */
@Entity
@Table(name = "companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String siren;

    @Column(unique = true)
    private String vat;

    private String address;

    @Column(nullable = false)
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType;

    /** 
     * Référence vers l'identifiant unique de l'utilisateur dans Keycloak.
     * Permet de lier le profil métier au compte d'authentification.
     */
    @Column(nullable = false, unique = true)
    private String keycloakUserId;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contract> contracts = new ArrayList<>();

    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setCompany(this);
    }
}
