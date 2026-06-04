package com.projectrewards.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "\"transaction\"")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "tx_points", nullable = false)
    private Integer txPoints;

    @Column(name = "available_points", nullable = false)
    private Integer availablePoints = 0;

    @Column(name = "expired", nullable = false)
    private boolean expired = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(name = "purchase_id", unique = true)
    private Long purchaseId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Transaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getTxPoints() { return txPoints; }
    public void setTxPoints(Integer txPoints) { this.txPoints = txPoints; }

    public Integer getAvailablePoints() { return availablePoints; }
    public void setAvailablePoints(Integer availablePoints) { this.availablePoints = availablePoints; }

    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
