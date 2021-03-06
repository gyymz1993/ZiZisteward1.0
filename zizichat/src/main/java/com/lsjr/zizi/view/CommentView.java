package com.lsjr.zizi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizi.R;
import com.lsjr.zizi.chat.bean.Comment;
import com.lsjr.zizi.mvp.circledemo.spannable.CircleMovementMethod;
import com.lsjr.zizi.mvp.circledemo.spannable.SpannableClickable;
import com.lsjr.zizi.mvp.circledemo.utils.UrlUtils;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<Comment> mDatas;
    private LayoutInflater layoutInflater ;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<Comment> datas){
        if(datas == null ){
            datas = new ArrayList<>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<Comment> getDatas(){
        return mDatas;
    }

    public CommentView(Context context) {
        super(context);
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.praise_item_default));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        }finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged(){
        removeAllViews();
        if(mDatas == null || mDatas.size() == 0){
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0; i<mDatas.size(); i++){
            final int index = i;
            View view = getView(index);
            if(view == null){
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            addView(view, index, layoutParams);
        }
    }

    private View getView(final int position){
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        final Comment bean = mDatas.get(position);
        String name = bean.getNickName();
        String id = bean.getUserId();
        String toReplyName = "";
        if (bean.getToUserId() != null) {
            toReplyName = bean.getToNickname();
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, bean.getUserId()));
        if (!TextUtils.isEmpty(toReplyName)) {
            /*不是自己回复自己*/  //如果是我我自己恢复别人 && !String.valueOf(id).equals(ConfigApplication.instance().getLoginUserId())
            if (!String.valueOf(id).equals(bean.getToUserId())){
                builder.append(" 回复 ");
                builder.append(setClickableSpan(toReplyName, bean.getToUserId()));
            }
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getBody();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        L_.e("评论区显示"+builder);
        commentTv.setText(builder);
        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if(onItemLongClickListener!=null){
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor){
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(UIUtils.getContext(), textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public interface OnItemClickListener{
         void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
         void onItemLongClick(int position);
    }



}
