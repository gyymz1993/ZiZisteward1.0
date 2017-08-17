package com.lsjr.zizi.mvp.home.cicle;

import android.view.View;
import android.view.ViewStub;

import com.lsjr.zizi.R;
import com.lsjr.zizi.chat.bean.PublicMessage;
import com.lsjr.zizi.mvp.circledemo.widgets.CircleVideoView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/16 14:29
 */

public class VideoCellAdapter extends ACicleAdapter {

    public CircleVideoView videoView;
    private PublicMessage mBody;

    public VideoCellAdapter(PublicMessage body) {
        super(body);
        this.mBody=body;
    }

    @Override
    public int getItemType() {
        return TYPE_VIDEO;
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_videobody);
        View subView = viewStub.inflate();
        CircleVideoView videoBody = (CircleVideoView) subView.findViewById(R.id.videoView);
        if(videoBody!=null){
            this.videoView = videoBody;
        }
    }

}
