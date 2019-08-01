package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;
import com.xq.LegouShop.response.CreateOrderResponse;

public class CreateOrderProtocol extends BaseProtocol<CreateOrderResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public CreateOrderProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected CreateOrderResponse parseJson(String json) {
		CreateOrderResponse getAuthenticationInfoResponse = gson.fromJson(json, CreateOrderResponse.class);
		return getAuthenticationInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/createOrder";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
