package com.lsjr.zizi.mvp.home.cicle;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizi.R;
import com.lsjr.zizi.chat.bean.PublicMessage;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/16 11:52
 */

public class URLCellAdapter extends ACicleAdapter {

    public LinearLayout urlBody;
    /** 链接的图片 */
    public ImageView urlImageIv;
    /** 链接的标题 */
    public TextView urlContentTv;
    private PublicMessage mBody;

    public URLCellAdapter(PublicMessage body) {
        super(body);
        mBody=body;
    }

    @Override
    public int getItemType() {
        return TYPE_URL;
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_urlbody);
        View subViw  = viewStub.inflate();
        LinearLayout urlBodyView = (LinearLayout) subViw.findViewById(R.id.urlBody);
        if(urlBodyView != null){
            urlBody = urlBodyView;
            urlImageIv = (ImageView) subViw.findViewById(R.id.urlImageIv);
            urlContentTv = (TextView) subViw.findViewById(R.id.urlContentTv);
        }
        viewStub.setVisibility(View.GONE);

    }

}
