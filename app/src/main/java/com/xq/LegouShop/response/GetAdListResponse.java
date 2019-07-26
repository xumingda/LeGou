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
public class GetAdListResponse {
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
        public String id;
        public String position;//		广告位置，1表示首页banner图，2付款成功之后，3分类banner图，4首页中间左边图片，5首页中间右上边图片，6首页中间右下边图片
        public String title;//	广告标题
        public String pic;//	图片
        public String goodsId;//	关联的商品id
        public String remark;//	备注
        public int type	;//0表示跳转到商品，1表示跳转到外链
        public String url;//	外链

        @Override
        public String toString() {
            return "DataList{" +
                    "id='" + id + '\'' +
                    ", position='" + position + '\'' +
                    ", title='" + title + '\'' +
                    ", pic='" + pic + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", remark='" + remark + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
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
