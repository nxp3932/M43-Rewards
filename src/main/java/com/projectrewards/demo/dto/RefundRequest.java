package com.projectrewards.demo.dto;

public class RefundRequest {
    private Long userId;
    private Long purchaseId;

    public RefundRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }
}
