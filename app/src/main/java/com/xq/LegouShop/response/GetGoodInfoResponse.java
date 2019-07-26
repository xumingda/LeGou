package com.xq.LegouShop.response;

import com.xq.LegouShop.bean.GoodInfoBean;
import com.xq.LegouShop.bean.GoodsBean;
import com.xq.LegouShop.bean.SpecificationsBean;

import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-3-23下午15:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetGoodInfoResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**
     * 描述
     */
    public String msg;
    public GoodInfoBean data;
    public List<SpecificationsBean> dataList;


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

    public GoodInfoBean getData() {
        return data;
    }

    public void setData(GoodInfoBean data) {
        this.data = data;
    }

    public List<SpecificationsBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SpecificationsBean> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "GetGoodInfoResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", dataList=" + dataList +
                '}';
    }
}
