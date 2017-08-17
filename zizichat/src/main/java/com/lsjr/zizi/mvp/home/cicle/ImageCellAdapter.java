package com.lsjr.zizi.mvp.home.cicle;

import android.view.View;
import android.view.ViewStub;

import com.lsjr.zizi.R;
import com.lsjr.zizi.mvp.chat.bean.PublicMessage;
import com.lsjr.zizi.mvp.circledemo.bean.PhotoInfo;
import com.lsjr.zizi.mvp.circledemo.widgets.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/16 14:29
 */

public class ImageCellAdapter extends ACicleAdapter {


    /** 图片*/
    public MultiImageView multiImageView;
    private PublicMessage mBody;
    public ImageCellAdapter(PublicMessage body) {
        super(body);
        mBody=body;
    }

    @Override
    public int getItemType() {
        return TYPE_IMG;
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if(multiImageView != null){
            this.multiImageView = multiImageView;
        }
        PublicMessage.Body body = mBody.getBody();

        if (body.getImages() != null) {
            multiImageView.setVisibility(View.VISIBLE);
            List<PhotoInfo> lists=new ArrayList<>();
            for (PublicMessage.Resource resource:body.getImages()){
                PhotoInfo photoInfo=new PhotoInfo();
                photoInfo.url=resource.getOriginalUrl();
                lists.add(photoInfo);
            }
            multiImageView.setList(lists);
        } else {
            multiImageView.setVisibility(View.GONE);
        }
    }
}
