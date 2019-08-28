package com.xq.LegouShop.bean;

import java.io.Serializable;
import java.util.List;

//商户实体
public class AfterSaleOrderBean implements Serializable {
    public int id;//订单Id
    public int shopId	;//店铺id
    public String shopName	;//店铺名称
    public String orderNo;//	订单号
    public String orderMoney;//	订单金额
    public String expressFee;//	快递费
    public String needMoney;//	需要支付金额
    public String payMoney;//	实际支付金额，已付款的订单才有
    public String createTime;//	下单时间
    public List<CartBean> orderGoodsList;//	订单的商品列表
    public int refundStatus;//	退款状态，0表示未申请，10申请退款中，11商家同意退款
    public int userRefundId	;//退款id，退款状态不为0，可以查看详情
    public int  returnGoodsStatus;//	退货状态，0未申请，10申请中，11商家同意=买家待发货，13买家已发货，14商家已收货=已完成，已退款，
    public int  userReturnGoodsId;//	退货id，退货状态不为0，可以查看详情
    public int  changeGoodsStatus;//	换货状态，0未申请，10申请中，11商家同意=买家待发货，13买家已发货，14商家已收货=待发货，15商家已发货=买家待收货，16买家已收货=已完成
    public int userChangeGoodsId;//	换货id，换货状态不为0，可以查看详情

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



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public int getUserRefundId() {
        return userRefundId;
    }

    public void setUserRefundId(int userRefundId) {
        this.userRefundId = userRefundId;
    }

    public int getReturnGoodsStatus() {
        return returnGoodsStatus;
    }

    public void setReturnGoodsStatus(int returnGoodsStatus) {
        this.returnGoodsStatus = returnGoodsStatus;
    }

    public int getUserReturnGoodsId() {
        return userReturnGoodsId;
    }

    public void setUserReturnGoodsId(int userReturnGoodsId) {
        this.userReturnGoodsId = userReturnGoodsId;
    }

    public int getChangeGoodsStatus() {
        return changeGoodsStatus;
    }

    public void setChangeGoodsStatus(int changeGoodsStatus) {
        this.changeGoodsStatus = changeGoodsStatus;
    }

    public int getUserChangeGoodsId() {
        return userChangeGoodsId;
    }

    public void setUserChangeGoodsId(int userChangeGoodsId) {
        this.userChangeGoodsId = userChangeGoodsId;
    }

    public List<CartBean> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<CartBean> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    @Override
    public String toString() {
        return "AfterSaleOrderBean{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", expressFee='" + expressFee + '\'' +
                ", needMoney='" + needMoney + '\'' +
                ", payMoney='" + payMoney + '\'' +
                ", createTime='" + createTime + '\'' +
                ", orderGoodsList=" + orderGoodsList +
                ", refundStatus=" + refundStatus +
                ", userRefundId=" + userRefundId +
                ", returnGoodsStatus=" + returnGoodsStatus +
                ", userReturnGoodsId=" + userReturnGoodsId +
                ", changeGoodsStatus=" + changeGoodsStatus +
                ", userChangeGoodsId=" + userChangeGoodsId +
                '}';
    }
}
