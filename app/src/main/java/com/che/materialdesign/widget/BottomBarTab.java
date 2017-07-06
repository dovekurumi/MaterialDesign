package com.che.materialdesign.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.che.materialdesign.R;

/**
 * Created by dove on 2017/7/5.
 */

public class BottomBarTab extends FrameLayout {

    private Context mContext;
    private ImageView mIcon;
    private TextView mTvTitle;
    private TextView mTvUnreadContent;
    private int mTabPosition = -1;

    public BottomBarTab(@NonNull Context context, int icon, CharSequence title) {
        this(context, null, icon, title);
    }

    public BottomBarTab(@NonNull Context context, @Nullable AttributeSet attrs, int icon, CharSequence title) {
        this(context, attrs, 0, icon, title);
    }

    public BottomBarTab(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, int icon, CharSequence title) {
        super(context, attrs, defStyleAttr);
        init(context, icon, title);
    }

    private void init(Context context, int icon, CharSequence title) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        LinearLayout lLContainer = new LinearLayout(context);
        lLContainer.setOrientation(LinearLayout.VERTICAL);
        lLContainer.setGravity(Gravity.CENTER);
        LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContainer.gravity = Gravity.CENTER;
        lLContainer.setLayoutParams(paramsContainer);

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        mIcon.setImageResource(icon);
        mIcon.setLayoutParams(params);
        mIcon.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect));
        lLContainer.addView(mIcon);

        mTvTitle = new TextView(context);
        mTvTitle.setText(title);
        LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTv.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mTvTitle.setTextSize(10);
        mTvTitle.setTextColor(ContextCompat.getColor(context, R.color.tab_unselect));
        mTvTitle.setLayoutParams(paramsTv);
        lLContainer.addView(mTvTitle);

        addView(lLContainer);

        int min = dip2px(context, 20);
        int padding = dip2px(context, 5);
        mTvUnreadContent = new TextView(context);
        mTvUnreadContent.setBackgroundResource(R.drawable.bg_msg_bubble);
        mTvUnreadContent.setMinWidth(min);
        mTvUnreadContent.setTextColor(Color.WHITE);
        mTvUnreadContent.setPadding(padding, 0, padding, 0);
        mTvUnreadContent.setGravity(Gravity.CENTER);
        LayoutParams tvUnReadParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, min);
        tvUnReadParams.gravity = Gravity.CENTER;
        tvUnReadParams.leftMargin = dip2px(context, 17);
        tvUnReadParams.bottomMargin = dip2px(context, 14);
        mTvUnreadContent.setLayoutParams(tvUnReadParams);
        mTvUnreadContent.setVisibility(GONE);

        addView(mTvUnreadContent);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected){
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
            mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }else {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.tab_unselect));
            mTvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.tab_unselect));
        }
    }

    /**
     * 设置未读数量
     * @param num
     */
    public void setUnreadCount(int num){
        if (num <= 0){
            mTvUnreadContent.setText(String.valueOf(0));
            mTvUnreadContent.setVisibility(GONE);
        }else {
            mTvUnreadContent.setVisibility(VISIBLE);
            if (num > 99){
                mTvUnreadContent.setText("99+");
            }else {
                mTvUnreadContent.setText(String.valueOf(num));
            }
        }
    }

    public int getUnreadCount(){
        int count = 0;
        if (TextUtils.isEmpty(mTvUnreadContent.getText())){
            return count;
        }
        if (mTvUnreadContent.getText().toString().equals("99+")){
            return 99;
        }
        try {
            count = Integer.valueOf(mTvUnreadContent.getText().toString());
        }catch (Exception ignored){

        }
        return  count;
    }

    private int dip2px(Context context, float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public int getTabPosition() {
        return mTabPosition;
    }

    public void setTabPosition(int position){
        mTabPosition = position;
        if (position == 0){
            setSelected(true);
        }
    }
}
