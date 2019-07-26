package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.GetUserInfoResponse;

public class GetUserInfoProtocol extends BaseProtocol<GetUserInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetUserInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserInfoResponse parseJson(String json) {
		GetUserInfoResponse getUserInfoResponse = gson.fromJson(json, GetUserInfoResponse.class);
		return getUserInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getUserInfo";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
