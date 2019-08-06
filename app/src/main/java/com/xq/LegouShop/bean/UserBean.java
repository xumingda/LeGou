package com.xq.LegouShop.bean;

import java.io.Serializable;

//商户实体
public class UserBean implements Serializable {
    public String id;//	用户id
    public String headurl;//	头像，注意区分微信头像和自己修改过的头像，自己修改过的头像是不以http开头的
    public String phoneNumber;//	手机号码
    public String nickName;//	昵称
    public String userName;//	用户名
    public String sex;//	性别  男/女
    public String email;//	邮箱
    public String balanceMoney;//	余额
    public int changeScore;//	转换积分
    public int buyScore;//	购物积分
    public String createTime;//	注册时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public int getChangeScore() {
        return changeScore;
    }

    public void setChangeScore(int changeScore) {
        this.changeScore = changeScore;
    }

    public int getBuyScore() {
        return buyScore;
    }

    public void setBuyScore(int buyScore) {
        this.buyScore = buyScore;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", headurl='" + headurl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", email='" + email + '\'' +
                ", balanceMoney='" + balanceMoney + '\'' +
                ", changeScore='" + changeScore + '\'' +
                ", buyScore='" + buyScore + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
