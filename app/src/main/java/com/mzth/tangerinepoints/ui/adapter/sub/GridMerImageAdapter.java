package com.mzth.tangerinepoints.ui.adapter.sub;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mzth.tangerinepoints.R;
import com.mzth.tangerinepoints.common.Constans;
import com.mzth.tangerinepoints.ui.activity.sub.git.PhotoPreviewActivity;
import com.mzth.tangerinepoints.ui.adapter.base.BaseInfoAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class GridMerImageAdapter extends BaseInfoAdapter<String> {
    public GridMerImageAdapter(Context context, List<String> list, int resId) {
        super(context, list, resId);
    }

    @Override
    public View dealView(Context context, List<String> list, int resId, int position, View convertView) {
        ViewHolder vh=null;
        if(convertView==null){
            convertView=View.inflate(context,resId,null);
            vh=new ViewHolder();
            vh.initView(convertView);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        vh.initData(list,position);
        return convertView;
    }

    class ViewHolder{
        private ImageView iv_image;
        private void initView(View v){
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
        }
        private void initData(final List<String> list, final int position){
            String image = list.get(position);
            Glide.with(_context).load(Constans.PHOTO_PATH+image+"/300/300").placeholder(R.mipmap.ic_launcher).into(iv_image);
            //查看大图
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(_context,PhotoPreviewActivity.class);
                    intent.putExtra("list", (Serializable) list);
                    intent.putExtra("position",position+"");
                    _context.startActivity(intent);
                }
            });
        }
    }
}
