package com.xq.LegouShop.wxapi;

import java.util.List;
import java.util.Map;

/**
 * @version 创建时间：2018年9月10日 上午9:49:43
 * 类说明
 */
public class WeixinBean {
	private String access_token;
	private String openid;
	private String nickname;//用户昵称
	private int sex;//1是男2是女
	private String headimgurl;
	private String language;
	private String city;
	private String province;
	private String country;
	private	List<String> privilege;
	private String unionid;
	
	
	@Override
	public String toString() {
		return "WeixinBean [openid="
				+ openid + ", nickname=" + nickname + ", sex=" + sex
				+ ", headimgurl=" + headimgurl + ", language=" + language
				+ ", city=" + city + ", province=" + province + ", country="
				+ country + ", privilege=" + privilege + ", unionid=" + unionid + "]";
	}
	public List<String> getData() {
		return privilege;
	}
	public void setData(List<String> privilege) {
		this.privilege = privilege;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}


	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	
	 
}
