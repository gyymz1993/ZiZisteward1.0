package com.lsjr.zizisteward.mvp.recycle;

import android.support.annotation.IntDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/22.
 */

public class RecycleBean {

    public static final int TYPE_1=0X11;
    public static final int TYPE_2=0X22;
    public static final int TYPE_3=0X33;
    public static final int TYPE_4=0X44;

    private int type;
    private Map<Integer,List<RecycleDeatil>> recycleTypes;


    public Map<Integer, List<RecycleDeatil>> getRecycleTypes() {
        return recycleTypes;
    }

    public void setRecycleTypes(Map<Integer, List<RecycleDeatil>> recycleTypes) {
        this.recycleTypes = recycleTypes;
    }

    @IntDef({TYPE_1, TYPE_2, TYPE_3, TYPE_4})
    public @interface EmType {

    }

    public int getType() {
        return type;
    }

    public void setType(@EmType int type) {
        this.type = type;
    }



    static class RecycleDeatil{

        private String title;
        private String imgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }


}
