package com.projectrewards.demo.dto;

public class RedeemRequest {
    private Long userId;
    private Integer pointsRedeemed;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getPointsRedeemed() { return pointsRedeemed; }
    public void setPointsRedeemed(Integer pointsRedeemed) { this.pointsRedeemed = pointsRedeemed; }     
}
