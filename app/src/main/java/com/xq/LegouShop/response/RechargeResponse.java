package com.xq.LegouShop.response;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class RechargeResponse {
	
	/** 服务器响应码 */
	public String code;
	/**验证码id*/
	public Data data;
//	public class RegisterData{
//		/** 用户token */
//		public String sid;
//	}
	/** 服务器返回消息 */
	public String msg;

	public class Data{

		public String appid;//	选择了微信支付，有值
		public String partnerid;//	选择了微信支付，有值
		public String prepayid;//	选择了微信支付，有值
		public String packageValue;//	选择了微信支付，有值
		public String noncestr;//	随机字符串
		public String timestamp;//	时间戳
		public String sign	;//签名

		@Override
		public String toString() {
			return "Data{" +
					"appid='" + appid + '\'' +
					", partnerid='" + partnerid + '\'' +
					", prepayid='" + prepayid + '\'' +
					", packageValue='" + packageValue + '\'' +
					", noncestr='" + noncestr + '\'' +
					", timestamp='" + timestamp + '\'' +
					", sign='" + sign + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "CreateOrderResponse{" +
				"code='" + code + '\'' +
				", data=" + data +
				", msg='" + msg + '\'' +
				'}';
	}
}
