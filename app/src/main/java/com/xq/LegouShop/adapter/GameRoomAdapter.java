package com.xq.LegouShop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xq.LegouShop.R;
import com.xq.LegouShop.bean.OrderBean;
import com.xq.LegouShop.bean.ScoreRoomBean;
import com.xq.LegouShop.util.UIUtils;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class GameRoomAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<ScoreRoomBean> scoreRoomBeanList;
    private Context mContext;
    private int type;

    private Gson gson;
    public GameRoomAdapter(Context context, List<ScoreRoomBean> scoreRoomBeanList, int type){
        mContext=context;
        this.scoreRoomBeanList=scoreRoomBeanList;
        this.type=type;
        gson = new Gson();
    }

    public void setDate(List<ScoreRoomBean> scoreRoomBeanList){
        this.scoreRoomBeanList=scoreRoomBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return scoreRoomBeanList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.room_item, null);
            vh = new ViewHolder();
            vh.tv_people = (TextView) view.findViewById(R.id.tv_people);
            vh.tv_room = (TextView) view.findViewById(R.id.tv_room);
            vh.rl_bg= (RelativeLayout) view.findViewById(R.id.rl_bg);
            vh.iv_statue= (ImageView) view.findViewById(R.id.iv_statue);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ScoreRoomBean scoreRoomBean=scoreRoomBeanList.get(pos);
        switch (type){
            case 200:{
                vh.rl_bg.setBackgroundColor(Color.parseColor("#10c777"));
                break;
            }
            case 500:{
                vh.rl_bg.setBackgroundColor(Color.parseColor("#1c7df2"));
                break;
            }
            case 1000:{
                vh.rl_bg.setBackgroundColor(Color.parseColor("#e8ee10"));
                break;
            }
            case 2000:{
                vh.rl_bg.setBackgroundColor(Color.parseColor("#8103bc"));
                break;
            }
        }
        if(scoreRoomBean.status==1){
            vh.iv_statue.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_playing));
            vh.iv_statue.setVisibility(View.VISIBLE);
        }else  if(scoreRoomBean.status==3){
            vh.iv_statue.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_ontime));
            vh.iv_statue.setVisibility(View.VISIBLE);
        }else{
            vh.iv_statue.setVisibility(View.INVISIBLE);
        }
        vh.tv_room.setText(scoreRoomBean.getRoomName());
        vh.tv_people.setText(scoreRoomBean.getPlayNum()+"/"+scoreRoomBean.getLimitNum());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return scoreRoomBeanList.size() ;
    }


    class ViewHolder {
        ImageView iv_statue;
        TextView tv_people;
        TextView tv_room;
        RelativeLayout rl_bg;
    }

}
