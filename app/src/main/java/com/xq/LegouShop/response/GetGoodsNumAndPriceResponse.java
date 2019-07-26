package com.xq.LegouShop.response;

import com.xq.LegouShop.bean.GoodInfoBean;
import com.xq.LegouShop.bean.GoodNumAndPriceBean;
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
public class GetGoodsNumAndPriceResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**
     * 描述
     */
    public String msg;
    public GoodNumAndPriceBean data;


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

    public GoodNumAndPriceBean getData() {
        return data;
    }

    public void setData(GoodNumAndPriceBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetGoodsNumAndPriceResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
