package com.xq.LegouShop.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class CartBean implements Serializable{
    private String id;//购物车id，删除要用到
    private int goodsId;//	商品的id
    private String goodsCaseId;//	库存id
    private String goodsName;//	商品名称
    private String pic;//	商品缩略图
    private String salePrice;//	售价
    private String originalPrice;//	原价
    private String goodsGroupValues;//	商品属性值，直接显示，比如蓝色 36码
    private String buyCount;//	要购买的数量
    private boolean selected;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }



    public String getGoodsCaseId() {
        return goodsCaseId;
    }

    public void setGoodsCaseId(String goodsCaseId) {
        this.goodsCaseId = goodsCaseId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getGoodsGroupValues() {
        return goodsGroupValues;
    }

    public void setGoodsGroupValues(String goodsGroupValues) {
        this.goodsGroupValues = goodsGroupValues;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id='" + id + '\'' +
                ", goodsId=" + goodsId +
                ", goodsCaseId='" + goodsCaseId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", pic='" + pic + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", goodsGroupValues='" + goodsGroupValues + '\'' +
                ", buyCount='" + buyCount + '\'' +
                '}';
    }
}
