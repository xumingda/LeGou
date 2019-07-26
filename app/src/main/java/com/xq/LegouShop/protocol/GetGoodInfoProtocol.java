package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetGoodInfoResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;


public class GetGoodInfoProtocol extends BaseProtocol<GetGoodInfoResponse> {
	private Gson gson;

	public GetGoodInfoProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetGoodInfoResponse parseJson(String json) {
		GetGoodInfoResponse getRegionListResponse = gson.fromJson(json, GetGoodInfoResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getGoodsDesc";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
