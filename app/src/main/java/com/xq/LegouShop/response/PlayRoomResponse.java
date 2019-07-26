package com.xq.LegouShop.response;


import com.xq.LegouShop.bean.PlayRoomUserBean;
import com.xq.LegouShop.bean.RoomBean;
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
public class PlayRoomResponse {
	
	/** 服务器响应码 */
	public String code;
	/**验证码id*/
	public List<PlayRoomUserBean> dataList;
	public ScoreRoomBean data;
	public SelfData selfData;
	public int action;

	public class SelfData{
		/** 用户token */
		public String userId;//	用户id;
		public String  nickName	;//用户昵称
		public String  headurl	;//用户头像
		public String passcardNum;//	Passcard数量
		public int passcardType	;//1表示200积分的pass卡，2表示500积分的pass卡，3表示1000积分的pass卡，4表示2000积分的pass卡
		public String num	;//座位号

		@Override
		public String toString() {
			return "SelfData{" +
					"userId='" + userId + '\'' +
					", nickName='" + nickName + '\'' +
					", headurl='" + headurl + '\'' +
					", passcardNum='" + passcardNum + '\'' +
					", passcardType=" + passcardType +
					", num='" + num + '\'' +
					'}';
		}
	}
	/** 服务器返回消息 */
	public String msg;

	@Override
	public String toString() {
		return "PlayRoomResponse{" +
				"code='" + code + '\'' +
				", dataList=" + dataList +
				", data=" + data +
				", selfData=" + selfData +
				", action=" + action +
				", msg='" + msg + '\'' +
				'}';
	}
}
