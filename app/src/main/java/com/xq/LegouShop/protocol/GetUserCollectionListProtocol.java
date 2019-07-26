package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetGoodsListResponse;
import com.xq.LegouShop.response.GetUserCollectionListResponse;


public class GetUserCollectionListProtocol extends BaseProtocol<GetUserCollectionListResponse> {
	private Gson gson;

	public GetUserCollectionListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserCollectionListResponse parseJson(String json) {
		GetUserCollectionListResponse getRegionListResponse = gson.fromJson(json, GetUserCollectionListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getUserCollectionList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
