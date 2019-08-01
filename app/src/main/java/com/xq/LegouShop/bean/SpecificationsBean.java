package com.xq.LegouShop.bean;

import java.io.Serializable;
import java.util.List;

//实名认证
public class SpecificationsBean implements Serializable {
    public String id;//	商品规格组id
    public String goodsGroupName;//	商品规格组名称
    public List<AttrBean> attrList;//属性值列表，每个规格组都有多个属性值

    public class AttrBean{
        public boolean selected;
        public String id;//	属性值id,  goodsGroupValueId，这个参数在下一个接口2.1.15中用到，用来查询库存和商品单价
        public String goodsGroupNameValue;//	属性值

        @Override
        public String toString() {
            return "AttrBean{" +
                    "selected=" + selected +
                    ", id='" + id + '\'' +
                    ", goodsGroupNameValue='" + goodsGroupNameValue + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SpecificationsBean{" +
                "id='" + id + '\'' +
                ", goodsGroupName='" + goodsGroupName + '\'' +
                ", attrList=" + attrList +
                '}';
    }
}
