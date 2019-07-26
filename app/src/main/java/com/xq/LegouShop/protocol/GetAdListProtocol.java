package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetAdListResponse;
import com.xq.LegouShop.response.GetRegionListResponse;


public class GetAdListProtocol extends BaseProtocol<GetAdListResponse> {
	private Gson gson;

	public GetAdListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetAdListResponse parseJson(String json) {
		GetAdListResponse getRegionListResponse = gson.fromJson(json, GetAdListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getAdList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
