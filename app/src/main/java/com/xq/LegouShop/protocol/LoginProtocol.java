package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.LoginResponse;

public class LoginProtocol extends BaseProtocol<LoginResponse> {
	private Gson gson;

	public LoginProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected LoginResponse parseJson(String json) {
		LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
		return loginresponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/login";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
