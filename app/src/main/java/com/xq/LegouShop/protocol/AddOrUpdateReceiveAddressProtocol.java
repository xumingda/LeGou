package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.AddAuthenticationInfoResponse;

public class AddOrUpdateReceiveAddressProtocol extends BaseProtocol<AddAuthenticationInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public AddOrUpdateReceiveAddressProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected AddAuthenticationInfoResponse parseJson(String json) {
		AddAuthenticationInfoResponse getAuthenticationInfoResponse = gson.fromJson(json, AddAuthenticationInfoResponse.class);
		return getAuthenticationInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/addOrUpdateReceiveAddress";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
