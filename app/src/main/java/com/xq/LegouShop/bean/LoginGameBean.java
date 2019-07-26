package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class LoginGameBean implements Serializable {
    public String authorization;//
    public int position;//
    public int scoreId;//
    public int scoreRoomId;//
    public String trusteeship;
    public String   trusteeshipScoreId;
    public String  userId;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getScoreRoomId() {
        return scoreRoomId;
    }

    public void setScoreRoomId(int scoreRoomId) {
        this.scoreRoomId = scoreRoomId;
    }

    public String getTrusteeship() {
        return trusteeship;
    }

    public void setTrusteeship(String trusteeship) {
        this.trusteeship = trusteeship;
    }

    public String getTrusteeshipScoreId() {
        return trusteeshipScoreId;
    }

    public void setTrusteeshipScoreId(String trusteeshipScoreId) {
        this.trusteeshipScoreId = trusteeshipScoreId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "LoginGameBean{" +
                "authorization='" + authorization + '\'' +
                ", position=" + position +
                ", scoreId=" + scoreId +
                ", scoreRoomId=" + scoreRoomId +
                ", trusteeship='" + trusteeship + '\'' +
                ", trusteeshipScoreId='" + trusteeshipScoreId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
