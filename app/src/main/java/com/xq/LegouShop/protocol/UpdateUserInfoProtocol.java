package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCodeResponse;
import com.xq.LegouShop.response.UpdateUserInfoResponse;

public class UpdateUserInfoProtocol extends BaseProtocol<UpdateUserInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public UpdateUserInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdateUserInfoResponse parseJson(String json) {
		UpdateUserInfoResponse updateUserInfoResponse = gson.fromJson(json, UpdateUserInfoResponse.class);
		return updateUserInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/updateUserInfo";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
