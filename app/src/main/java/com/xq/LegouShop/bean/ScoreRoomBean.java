package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class ScoreRoomBean implements Serializable {
    public String roomName;//	房间名称
    public int id;//	scoreRoomId积分专区房间id
    public int scoreId;// 	积分专区id
    public String playNum;//	当前房间人数
    public int limitNum;//	房间限制人数
    public int status;//	房间状态 0空闲，1游戏中（可进可出），2倒计时15秒（可进不可出），3倒计时5秒（不可进不可出），4开奖中
    public String leftSecond;//	开奖剩余秒数

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public String getPlayNum() {
        return playNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLeftSecond() {
        return leftSecond;
    }

    public void setLeftSecond(String leftSecond) {
        this.leftSecond = leftSecond;
    }

    @Override
    public String toString() {
        return "ScoreRoomBean{" +
                "roomName='" + roomName + '\'' +
                ", id=" + id +
                ", scoreId=" + scoreId +
                ", playNum='" + playNum + '\'' +
                ", limitNum=" + limitNum +
                ", status=" + status +
                ", leftSecond='" + leftSecond + '\'' +
                '}';
    }
}
