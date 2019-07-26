package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class PlayRoomUserBean implements Serializable {
    public String createTime;//	收货地址id
    public int id;//	序号
    public String num;	//座位号
    public int userId;//	用户id 为0表示没人
    public String nickName;//	用户昵称
    public String headurl;//	用户头像

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PlayRoomUserBean{" +
                "createTime='" + createTime + '\'' +
                ", id=" + id +
                ", num='" + num + '\'' +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", headurl='" + headurl + '\'' +
                '}';
    }
}
