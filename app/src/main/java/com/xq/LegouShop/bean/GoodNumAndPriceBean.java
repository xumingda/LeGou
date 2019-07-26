package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class GoodNumAndPriceBean implements Serializable {
    public String goodsId;//	商品id
    public String goodsCaseId;//		商品实际库存id，当商品的isUserSet=1（是否使用规格 0否1是）的时候，才会有这个值
    public String pic	;//图片
    public String salePrice	;//售价
    public String costPrice;//	成本价
    public String originalPrice;//	原价
    public int num;//	库存
    public String  goodsNo;//	货号


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCaseId() {
        return goodsCaseId;
    }

    public void setGoodsCaseId(String goodsCaseId) {
        this.goodsCaseId = goodsCaseId;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    @Override
    public String toString() {
        return "GoodNumAndPriceBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsCaseId='" + goodsCaseId + '\'' +
                ", pic='" + pic + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", costPrice='" + costPrice + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", num=" + num +
                ", goodsNo='" + goodsNo + '\'' +
                '}';
    }
}
