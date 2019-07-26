package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.FindPwdResponse;
import com.xq.LegouShop.response.GetCodeResponse;

public class FindPwdProtocol extends BaseProtocol<FindPwdResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public FindPwdProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected FindPwdResponse parseJson(String json) {
		FindPwdResponse findPwdResponse = gson.fromJson(json, FindPwdResponse.class);
		return findPwdResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/findPwd";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
