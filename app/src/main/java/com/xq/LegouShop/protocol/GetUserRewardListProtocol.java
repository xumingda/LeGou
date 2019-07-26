package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetUserRewardListResponse;
import com.xq.LegouShop.response.UpdatePhoneResponse;

public class GetUserRewardListProtocol extends BaseProtocol<GetUserRewardListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetUserRewardListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserRewardListResponse parseJson(String json) {
		GetUserRewardListResponse getcoderesponse = gson.fromJson(json, GetUserRewardListResponse.class);
		return getcoderesponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getUserRewardList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
