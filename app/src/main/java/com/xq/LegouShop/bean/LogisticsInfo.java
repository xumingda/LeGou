package com.xq.LegouShop.bean;

import java.io.Serializable;
import java.util.List;

public class LogisticsInfo implements Serializable {
    public String logistics;//	物流公司
    public String logisticsNo;//	物流单号
    public String info;	//物流信息

    public class Info{
        public String  ftime;//	时间点
        public String context	;//地点信息
        public String time;

        @Override
        public String toString() {
            return "Info{" +
                    "ftime='" + ftime + '\'' +
                    ", context='" + context + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LogisticsInfo{" +
                "logistics='" + logistics + '\'' +
                ", logisticsNo='" + logisticsNo + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
