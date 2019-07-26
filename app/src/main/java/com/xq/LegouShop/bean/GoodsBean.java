package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class GoodsBean implements Serializable {
    public String id;//	商品id
    public String goodsName;//		商品名称

    public String pic	;//商品缩略图
    public String salePrice	;//售价
    public String costPrice;//	成本价
    public String originalPrice;//	原价
    public int salesVolume;//	销量

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "id='" + id + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", pic='" + pic + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", costPrice='" + costPrice + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", salesVolume=" + salesVolume +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }
}
