package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.AddressBean;

import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetUserReceiveAddressListResponse {
	
	/** 服务器响应码 */
	public String code;
	/**验证码id*/

	/** 服务器返回消息 */
	public String msg;

	public List<AddressBean> dataList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<AddressBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<AddressBean> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return "GetUserReceiveAddressListResponse{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", dataList=" + dataList +
				'}';
	}
}
