package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.RoomBean;
import com.xq.LegouShop.bean.ScoreRoomBean;

import java.io.Serializable;
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
public class ScoreRoomResponse implements Serializable {
	
	/** 服务器响应码 */
	public String code;
	/**验证码id*/
	public List<ScoreRoomBean> dataList;

	/** 服务器返回消息 */
	public String msg;
	public int action;
	public int num;

	@Override
	public String toString() {
		return "ScoreRoomResponse{" +
				"code='" + code + '\'' +
				", dataList=" + dataList +
				", msg='" + msg + '\'' +
				", action=" + action +
				", num=" + num +
				'}';
	}
}
