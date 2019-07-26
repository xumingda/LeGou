package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.RegisterResponse;

public class RegisterProtocol extends BaseProtocol<RegisterResponse> {
	private Gson gson;

	public RegisterProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected RegisterResponse parseJson(String json) {
		RegisterResponse registerResponse = gson.fromJson(json, RegisterResponse.class);
		return registerResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/reg";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
