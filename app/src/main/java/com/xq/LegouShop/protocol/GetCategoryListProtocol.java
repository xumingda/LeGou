package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCategoryListResponse;
import com.xq.LegouShop.response.GetRegionListResponse;


public class GetCategoryListProtocol extends BaseProtocol<GetCategoryListResponse> {
	private Gson gson;

	public GetCategoryListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetCategoryListResponse parseJson(String json) {
		GetCategoryListResponse getRegionListResponse = gson.fromJson(json, GetCategoryListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getCategoryList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
