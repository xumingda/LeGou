package com.xq.LegouShop.bean;

import java.io.Serializable;
import java.util.List;

//商户实体
public class OrderBean implements Serializable {
    public int id;//订单Id
    public int shopId	;//店铺id
    public String shopName	;//店铺名称
    public String orderNo;//	订单号
    public String orderMoney;//	订单金额
    public String expressFee;//	快递费
    public String needMoney;//	需要支付金额
    public String payMoney;//	实际支付金额，已付款的订单才有
    public int payStatus;//	订单状态 0待付款（可以取消，这里取消不用退款），1待发货（可以取消，这里取消订单就要退款），2待收货，3完成
    public String createTime;//	下单时间
    public int buyScorePay;//	是否使用购物积分支付 0否1是
    public int changeScorePay;//	是否使用转换积分支付 0否1是
    public int balancePay;//	是否使用余额支付 0否1是
    public int weixinPay;//	是否使用微信支付 0否1是
    public int zhifubaoPay;//	是否使用支付宝支付 0否1是
    public List<CartBean> orderGoodsList;//	订单的商品列表


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(String expressFee) {
        this.expressFee = expressFee;
    }

    public String getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(String needMoney) {
        this.needMoney = needMoney;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBuyScorePay() {
        return buyScorePay;
    }

    public void setBuyScorePay(int buyScorePay) {
        this.buyScorePay = buyScorePay;
    }

    public int getChangeScorePay() {
        return changeScorePay;
    }

    public void setChangeScorePay(int changeScorePay) {
        this.changeScorePay = changeScorePay;
    }

    public int getBalancePay() {
        return balancePay;
    }

    public void setBalancePay(int balancePay) {
        this.balancePay = balancePay;
    }

    public int getWeixinPay() {
        return weixinPay;
    }

    public void setWeixinPay(int weixinPay) {
        this.weixinPay = weixinPay;
    }

    public int getZhifubaoPay() {
        return zhifubaoPay;
    }

    public void setZhifubaoPay(int zhifubaoPay) {
        this.zhifubaoPay = zhifubaoPay;
    }

    public List<CartBean> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<CartBean> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", expressFee='" + expressFee + '\'' +
                ", needMoney='" + needMoney + '\'' +
                ", payMoney='" + payMoney + '\'' +
                ", payStatus=" + payStatus +
                ", createTime='" + createTime + '\'' +
                ", buyScorePay=" + buyScorePay +
                ", changeScorePay=" + changeScorePay +
                ", balancePay=" + balancePay +
                ", weixinPay=" + weixinPay +
                ", zhifubaoPay=" + zhifubaoPay +
                ", orderGoodsList=" + orderGoodsList +
                '}';
    }
}
