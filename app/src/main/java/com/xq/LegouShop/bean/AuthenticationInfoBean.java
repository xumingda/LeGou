package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class AuthenticationInfoBean implements Serializable {
    public String realName;//	真实姓名
    public String idCard;//	身份证号码
    public String pic1;//	身份证正面
    public String pic2;//	身份证反面
    public String pic3;//	手持身份证
    public int  status;//	是否通过审核 0待审核，1通过，2不通过


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AuthenticationInfoBean{" +
                "realName='" + realName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", pic1='" + pic1 + '\'' +
                ", pic2='" + pic2 + '\'' +
                ", pic3='" + pic3 + '\'' +
                ", status=" + status +
                '}';
    }
}
