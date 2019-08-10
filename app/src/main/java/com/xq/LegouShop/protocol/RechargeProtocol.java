package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.CreateOrderResponse;
import com.xq.LegouShop.response.RechargeResponse;

public class RechargeProtocol extends BaseProtocol<RechargeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public RechargeProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected RechargeResponse parseJson(String json) {
		RechargeResponse rechargeResponse = gson.fromJson(json, RechargeResponse.class);
		return rechargeResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/recharge";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
