package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodsListResponse;


public class GetGoodListProtocol extends BaseProtocol<GetGoodsListResponse> {
	private Gson gson;

	public GetGoodListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetGoodsListResponse parseJson(String json) {
		GetGoodsListResponse getRegionListResponse = gson.fromJson(json, GetGoodsListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getGoodsList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
