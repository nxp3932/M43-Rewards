package com.projectrewards.demo.dto;

public class EarnRequest {
    private Long userId;
    private Integer pointsEarned;
    private Long purchaseId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(Integer points) { this.pointsEarned = points; }

    public Long getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }
}
