package com.xq.LegouShop.response;

import com.xq.LegouShop.bean.UserBalanceLogBean;

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
public class GetUserBalanceLogListResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**
     * 描述
     */
    public String msg;

    public List<UserBalanceLogBean> dataList;

    public Data data;

    public class Data {
        public String incomeMoney;//收入金额
        public String expenditureMoney;//			支出金额


        @Override
        public String toString() {
            return "Data{" +
                    "incomeMoney='" + incomeMoney + '\'' +
                    ", expenditureMoney='" + expenditureMoney + '\'' +
                    '}';
        }
    }

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

    public List<UserBalanceLogBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<UserBalanceLogBean> dataList) {
        this.dataList = dataList;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetUserBalanceLogListResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", dataList=" + dataList +
                ", data=" + data +
                '}';
    }
}
