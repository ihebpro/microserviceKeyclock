package com.logistics.customer.domain.model;

/**
 * Value Object représentant le type de client dans le système logistique.
 * 
 * PRESTATAIRE : L'entreprise qui fournit les services logistiques (vous).
 * CLIENT      : L'entreprise qui confie ses marchandises au prestataire.
 */
public enum CustomerType {
    PRESTATAIRE,
    CLIENT
}
