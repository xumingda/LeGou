package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class UserBalanceLogBean implements Serializable {
    public String id;//	序号
    public String contacts;//	联系人

    public  int type;//	类型 1充值，2提现，3购物，4退款，5转去购物积分，6转去转换积分，7转换积分转来，8拼团中奖
    public String orderId;//	订单id，type=3有值
    public String userWithdrawalId;//	提现id, type=2有值
    public String  money;//	涉及金额
    public String remark;//	备注
    public String createTime;//	时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserWithdrawalId() {
        return userWithdrawalId;
    }

    public void setUserWithdrawalId(String userWithdrawalId) {
        this.userWithdrawalId = userWithdrawalId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
