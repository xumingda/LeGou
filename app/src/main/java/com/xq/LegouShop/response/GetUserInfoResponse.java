package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.PassBean;
import com.xq.LegouShop.bean.UserBean;

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
public class GetUserInfoResponse {
	
	/** 服务器响应码 */
	public String code;
	public UserBean data;
	public List<PassBean> dataList;

	/** 服务器返回消息 */
	public String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UserBean getData() {
		return data;
	}

	public void setData(UserBean data) {
		this.data = data;
	}

	public List<PassBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<PassBean> dataList) {
		this.dataList = dataList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "GetUserInfoResponse{" +
				"code=" + code +
				", data=" + data +
				", dataList=" + dataList +
				", msg='" + msg + '\'' +
				'}';
	}
}
