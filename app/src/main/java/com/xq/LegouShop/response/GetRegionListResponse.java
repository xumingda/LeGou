package com.xq.LegouShop.response;

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
public class GetRegionListResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**
     * 描述
     */
    public String msg;

    public List<DataList> dataList;

    public class DataList {
        public String id;    //地区id
        public String name;//	  	省名称
        public String code;//	省编码，之后的接口，有用到provincecode

        @Override
        public String toString() {
            return "DataList{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", code='" + code + '\'' +
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

    public List<DataList> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataList> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "GetRegionListResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
