package com.xq.LegouShop.protocol;

import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;
import com.xq.LegouShop.response.GetCartListsResponse;
import com.xq.LegouShop.response.GetOrderListResponse;


public class GetOrderListProtocol extends BaseProtocol<GetOrderListResponse> {
	private Gson gson;

	public GetOrderListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetOrderListResponse parseJson(String json) {
		GetOrderListResponse getRegionListResponse = gson.fromJson(json, GetOrderListResponse.class);
		return getRegionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getOrderList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
