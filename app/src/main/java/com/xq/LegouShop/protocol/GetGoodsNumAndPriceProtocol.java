package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetGoodsNumAndPriceResponse;


public class GetGoodsNumAndPriceProtocol extends BaseProtocol<GetGoodsNumAndPriceResponse> {
	private Gson gson;

	public GetGoodsNumAndPriceProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetGoodsNumAndPriceResponse parseJson(String json) {
		GetGoodsNumAndPriceResponse getRegionListResponse = gson.fromJson(json, GetGoodsNumAndPriceResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getGoodsNumAndPrice";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
