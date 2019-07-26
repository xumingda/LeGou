package com.xq.LegouShop.bean;

import java.io.Serializable;

//奖励
public class RewardBean implements Serializable {
    public String id;//	id
    public int type;//	类型，1pass卡，2转换积分，3购物积分
    public int count;//	奖励pass卡数量，type=1有值
    public String score;//	type=2,3有值，奖励积分
    public String remark;//	备注
    public String createTime;//	时间

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RewardBean{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", count=" + count +
                ", score='" + score + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
