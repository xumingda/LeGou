package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetAuthenticationInfoResponse;
import com.xq.LegouShop.response.GetUserInfoResponse;

public class GetAuthenticationInfoProtocol extends BaseProtocol<GetAuthenticationInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetAuthenticationInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetAuthenticationInfoResponse parseJson(String json) {
		GetAuthenticationInfoResponse getAuthenticationInfoResponse = gson.fromJson(json, GetAuthenticationInfoResponse.class);
		return getAuthenticationInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getAuthenticationInfo";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
