package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.CartBean;

import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetOrderChangeGoodsDescResponse {
	
	/** 服务器响应码 */
	public String code;
	public Data data;

	public List<DataList> dataList;
	public OrderInfo orderInfo;
	public List<CartBean> orderGoodsList;

	/** 服务器返回消息 */
	public String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public class  OrderInfo{
		public int orderId;//	订单id
		public String shopName;//	店铺名称
		public String orderNo;//	订单编号
		public String createTime;//	订单时间
		public String payMoney;//	退款金额

		@Override
		public String toString() {
			return "OrderInfo{" +
					"orderId=" + orderId +
					", shopName='" + shopName + '\'' +
					", orderNo='" + orderNo + '\'' +
					", createTime='" + createTime + '\'' +
					", payMoney='" + payMoney + '\'' +
					'}';
		}
	}
	public class Data{
		public int id;//	退款id
		public String reason;//	原因
		public String pics;//	图片
		public int status;//	换货状态，1申请中，2商家同意=买家待发货，3商家拒绝，4买家已发货，5商家已收货=待发货，6商家已发货=买家待收货，7买家已收货=已完成，8买家取消换货
		public String updateTime;//	时间
		public int goodsStatus;//	货物状态 0未收到，1已收到
		public String logistics;//	物流公司
		public String logisticsNo;//	物流单号
		public String logisticsInfo;//	物流信息
		public String logistics1;//	物流公司
		public String logisticsNo1;//	物流单号
		public String logisticsInfo1;//	物流信息
		@Override
		public String toString() {
			return "Data{" +
					"id=" + id +
					", reason='" + reason + '\'' +
					", pics='" + pics + '\'' +
					", status=" + status +
					", updateTime='" + updateTime + '\'' +
					", goodsStatus=" + goodsStatus +
					", logistics='" + logistics + '\'' +
					", logisticsNo='" + logisticsNo + '\'' +
					", logisticsInfo='" + logisticsInfo + '\'' +
					'}';
		}
	}

	public class DataList{
		public int id;//	序号
		public String remark;//	备注
		public String createTime;//	时间

		@Override
		public String toString() {
			return "DataList{" +
					"id=" + id +
					", remark='" + remark + '\'' +
					", createTime='" + createTime + '\'' +
					'}';
		}
	}

	@Override
	public String
	toString() {
		return "GetOrderRefundDescResponse{" +
				"code='" + code + '\'' +
				", data=" + data +
				", dataList=" + dataList +
				", orderInfo=" + orderInfo +
				", orderGoodsList=" + orderGoodsList +
				", msg='" + msg + '\'' +
				'}';
	}
}
