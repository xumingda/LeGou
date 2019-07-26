package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCodeResponse;

public class ChangeScoreToBalanceProtocol extends BaseProtocol<GetCodeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public ChangeScoreToBalanceProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetCodeResponse parseJson(String json) {
		GetCodeResponse getcoderesponse = gson.fromJson(json, GetCodeResponse.class);
		return getcoderesponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/changeScoreToBalance";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
