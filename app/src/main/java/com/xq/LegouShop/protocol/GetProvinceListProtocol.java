package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetProvinceListResponse;
import com.xq.LegouShop.response.GetRegionListResponse;


public class GetProvinceListProtocol extends BaseProtocol<GetProvinceListResponse> {
	private Gson gson;

	public GetProvinceListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetProvinceListResponse parseJson(String json) {
		GetProvinceListResponse getRegionListResponse = gson.fromJson(json, GetProvinceListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/getProvinceList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
