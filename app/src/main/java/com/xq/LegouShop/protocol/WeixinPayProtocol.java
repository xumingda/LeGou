package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.WeixinPayResponse;


public class WeixinPayProtocol extends BaseProtocol<WeixinPayResponse> {
	private Gson gson;

	public WeixinPayProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected WeixinPayResponse parseJson(String json) {
		WeixinPayResponse weixinPayResponse = gson.fromJson(json, WeixinPayResponse.class);
		return weixinPayResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "app/weixinPay.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
