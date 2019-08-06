package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetOrderDescResponse;


public class GetOderDescProtocol extends BaseProtocol<GetOrderDescResponse> {
	private Gson gson;

	public GetOderDescProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetOrderDescResponse parseJson(String json) {
		GetOrderDescResponse getRegionListResponse = gson.fromJson(json, GetOrderDescResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getOrderDesc";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
