package com.xq.LegouShop.protocol;


import com.google.gson.Gson;
import com.xq.LegouShop.base.BaseProtocol;

import com.xq.LegouShop.response.GetUserReceiveAddressListResponse;

public class GetUserReceiveAddressListProtocol extends BaseProtocol<GetUserReceiveAddressListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetUserReceiveAddressListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserReceiveAddressListResponse parseJson(String json) {
		GetUserReceiveAddressListResponse getAuthenticationInfoResponse = gson.fromJson(json, GetUserReceiveAddressListResponse.class);
		return getAuthenticationInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/user/getUserReceiveAddressList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
