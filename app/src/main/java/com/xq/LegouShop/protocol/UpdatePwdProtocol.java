package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.UpdatePhoneResponse;
import com.xq.LegouShop.response.UpdatePwdResponse;

public class UpdatePwdProtocol extends BaseProtocol<UpdatePwdResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public UpdatePwdProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdatePwdResponse parseJson(String json) {
		UpdatePwdResponse getcoderesponse = gson.fromJson(json, UpdatePwdResponse.class);
		return getcoderesponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/updateLoginPwd";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
