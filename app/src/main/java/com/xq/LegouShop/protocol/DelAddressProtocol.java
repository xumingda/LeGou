package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.UpdatePhoneResponse;

public class DelAddressProtocol extends BaseProtocol<UpdatePhoneResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public DelAddressProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdatePhoneResponse parseJson(String json) {
		UpdatePhoneResponse getcoderesponse = gson.fromJson(json, UpdatePhoneResponse.class);
		return getcoderesponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/delReceiveAddress";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
