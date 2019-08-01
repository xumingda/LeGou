package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetUserBalanceLogListResponse;


public class GetUserBalanceLogListProtocol extends BaseProtocol<GetUserBalanceLogListResponse> {
	private Gson gson;

	public GetUserBalanceLogListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserBalanceLogListResponse parseJson(String json) {
		GetUserBalanceLogListResponse getRegionListResponse = gson.fromJson(json, GetUserBalanceLogListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getUserBalanceLogList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
