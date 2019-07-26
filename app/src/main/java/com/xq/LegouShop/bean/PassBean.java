package com.xq.LegouShop.bean;

import java.io.Serializable;

//商户实体
public class PassBean implements Serializable {
    public String id;//	序号
    public int type;//	类型，1表示200积分的pass卡，2表示500积分的pass卡，3表示1000积分的pass卡，4表示2000积分的pass卡
    public int count;//	pass卡数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PassBean{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", count=" + count +
                '}';
    }
}
