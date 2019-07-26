package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class UserCollectionBean implements Serializable {
    public String userCollectionId;//	收藏id，删除要用
    public String goodsId;//	商品id
    public String goodsName;
    public String pic;//		商品缩略图
    public String goodsCaseId;//		商品库存id，为0表示没有规格组
    public int goodsGroupValueId1;//	属性值id1，为0表示没有
    public String goodsGroupNameValue1;//	属性名称1
    public int goodsGroupValueId2;	//属性值id2，为0表示没有
    public String goodsGroupNameValue2	;//属性名称2
    public int goodsGroupValueId3;	//属性值id3，为0表示没有
    public String goodsGroupNameValue3;//	属性名称3

    @Override
    public String toString() {
        return "UserCollectionBean{" +
                "userCollectionId='" + userCollectionId + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", pic='" + pic + '\'' +
                ", goodsCaseId='" + goodsCaseId + '\'' +
                ", goodsGroupValueId1='" + goodsGroupValueId1 + '\'' +
                ", goodsGroupNameValue1='" + goodsGroupNameValue1 + '\'' +
                ", goodsGroupValueId2='" + goodsGroupValueId2 + '\'' +
                ", goodsGroupNameValue2='" + goodsGroupNameValue2 + '\'' +
                ", goodsGroupValueId3='" + goodsGroupValueId3 + '\'' +
                ", goodsGroupNameValue3='" + goodsGroupNameValue3 + '\'' +
                '}';
    }

    public String getUserCollectionId() {
        return userCollectionId;
    }

    public void setUserCollectionId(String userCollectionId) {
        this.userCollectionId = userCollectionId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGoodsCaseId() {
        return goodsCaseId;
    }

    public void setGoodsCaseId(String goodsCaseId) {
        this.goodsCaseId = goodsCaseId;
    }



    public String getGoodsGroupNameValue1() {
        return goodsGroupNameValue1;
    }

    public void setGoodsGroupNameValue1(String goodsGroupNameValue1) {
        this.goodsGroupNameValue1 = goodsGroupNameValue1;
    }



    public String getGoodsGroupNameValue2() {
        return goodsGroupNameValue2;
    }

    public void setGoodsGroupNameValue2(String goodsGroupNameValue2) {
        this.goodsGroupNameValue2 = goodsGroupNameValue2;
    }


    public String getGoodsGroupNameValue3() {
        return goodsGroupNameValue3;
    }

    public void setGoodsGroupNameValue3(String goodsGroupNameValue3) {
        this.goodsGroupNameValue3 = goodsGroupNameValue3;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsGroupValueId1() {
        return goodsGroupValueId1;
    }

    public void setGoodsGroupValueId1(int goodsGroupValueId1) {
        this.goodsGroupValueId1 = goodsGroupValueId1;
    }

    public int getGoodsGroupValueId2() {
        return goodsGroupValueId2;
    }

    public void setGoodsGroupValueId2(int goodsGroupValueId2) {
        this.goodsGroupValueId2 = goodsGroupValueId2;
    }

    public int getGoodsGroupValueId3() {
        return goodsGroupValueId3;
    }

    public void setGoodsGroupValueId3(int goodsGroupValueId3) {
        this.goodsGroupValueId3 = goodsGroupValueId3;
    }
}
