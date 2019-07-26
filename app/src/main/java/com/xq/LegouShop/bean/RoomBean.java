package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class RoomBean implements Serializable {
    public String createTime;//	收货地址id
    public int id;//	联系人
    public int score;//		联系人电话
    public String title;//		省id

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "createTime='" + createTime + '\'' +
                ", id=" + id +
                ", score=" + score +
                ", title='" + title + '\'' +
                '}';
    }
}
