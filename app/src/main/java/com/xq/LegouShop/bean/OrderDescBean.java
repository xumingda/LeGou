package com.xq.LegouShop.bean;

import java.io.Serializable;
import java.util.List;

//商户实体
public class OrderDescBean implements Serializable {
    public int orderId;//订单Id
    public String shopName	;//店铺名称
    public String orderNo;//	订单号
    public String orderMoney;//	订单金额
    public int payStatus;//	订单状态 0待付款（可以取消，这里取消不用退款），1待发货（可以取消，这里取消订单就要退款），2待收货，3完成
    public String createTime;//	下单时间
    public List<CartBean> orderGoodsList;//	订单的商品列表
    public String buyerMessage;//	买家留言
    public String payTime;//	付款时间
    public String sendTime	;//发货时间
    public String receiveTime	;//收货时间
    public LogisticsInfo logisticsInfo;//	物流信息
    public UserReceiveAddress userReceiveAddress;//	收货地址，联系方式

    public class UserReceiveAddress{
        public String contacts;//	收货人
        public String contactsPhoneNumber	;//联系电话
        public String address	;//详细地址

        @Override
        public String toString() {
            return "UserReceiveAddress{" +
                    "contacts='" + contacts + '\'' +
                    ", contactsPhoneNumber='" + contactsPhoneNumber + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderDescBean{" +
                "orderId=" + orderId +
                ", shopName='" + shopName + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", payStatus=" + payStatus +
                ", createTime='" + createTime + '\'' +
                ", orderGoodsList=" + orderGoodsList +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", payTime='" + payTime + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                ", logisticsInfo=" + logisticsInfo +
                ", userReceiveAddress=" + userReceiveAddress +
                '}';
    }
}
