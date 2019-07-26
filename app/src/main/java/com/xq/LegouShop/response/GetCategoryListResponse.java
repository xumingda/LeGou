package com.xq.LegouShop.response;

import java.io.Serializable;
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
public class GetCategoryListResponse {
    /**
     * 服务器响应码
     */
    public String code;
    /**
     * 描述
     */
    public String msg;

    public List<DataList> dataList;

    public class DataList implements Serializable {
        public String id;    //类目id
        public String categoryName;//	  	类目名称
        public String parentId;// 上级类目id
        public String pic;//	类目图片

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        @Override
        public String toString() {
            return "DataList{" +
                    "id='" + id + '\'' +
                    ", categoryName='" + categoryName + '\'' +
                    ", parentId='" + parentId + '\'' +
                    ", pic='" + pic + '\'' +
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
