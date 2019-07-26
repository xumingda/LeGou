package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.LoginGameBean;
import com.xq.LegouShop.bean.PlayRoomUserBean;
import com.xq.LegouShop.bean.ScoreRoomBean;

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
public class LoginGameResponse {
	
	/** 服务器响应码 */
	public String code;
	/**验证码id*/
	public LoginGameBean data;
	public int action;
	/** 服务器返回消息 */
	public String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LoginGameBean getData() {
		return data;
	}

	public void setData(LoginGameBean data) {
		this.data = data;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "LoginGameResponse{" +
				"code='" + code + '\'' +
				", data=" + data +
				", action=" + action +
				", msg='" + msg + '\'' +
				'}';
	}
}
