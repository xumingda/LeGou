package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.RechargeResponse;
import com.xq.LegouShop.response.WithdrawalResponse;

public class WithdrawalProtocol extends BaseProtocol<WithdrawalResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public WithdrawalProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected WithdrawalResponse parseJson(String json) {
		WithdrawalResponse rechargeResponse = gson.fromJson(json, WithdrawalResponse.class);
		return rechargeResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/withdrawal";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
