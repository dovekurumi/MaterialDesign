package com.che.materialdesign.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dove on 2017/7/5.
 */

public class BottomBar extends LinearLayout{

    private LinearLayout mTabLayout;
    private LayoutParams mTabParams;

    private int mCurrentPosition = 0;
    private List<BottomBarTab> mTabs = new ArrayList<>();

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private boolean mVisible = true;

    private static final int TRANSLATE_DURATION_MILLIS = 200;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet defStyleAttr) {
        setOrientation(VERTICAL);

        mTabLayout = new LinearLayout(context);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setOrientation(HORIZONTAL);
        addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;

    }

    public BottomBar addItem(final BottomBarTab tab){
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) return;
                int pos = tab.getTabPosition();
                if (mCurrentPosition == pos){
                    mListener.onTabReselected(pos);
                }else {
                    mListener.onTabSelected(pos, mCurrentPosition);
                    tab.setSelected(true);
                    mListener.onTabUnselected(mCurrentPosition);
                    mTabs.get(mCurrentPosition).setSelected(true);
                    mCurrentPosition = pos;
                }
            }
        });
        tab.setTabPosition(mTabLayout.getChildCount());
        tab.setLayoutParams(mTabParams);
        mTabLayout.addView(tab);
        mTabs.add(tab);
        return this;
    }

    public interface OnTabSelectedListener{
        void onTabSelected(int position, int prePosition);

        void onTabUnselected(int position);

        void onTabReselected(int position);
    }

    private OnTabSelectedListener mListener;

    public void setOnTabSelectedListener(OnTabSelectedListener tabSelectedListener){
        this.mListener = tabSelectedListener;
    }

    public void setCurrentItem(final int position){
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.getChildAt(position).performClick();
            }
        });
    }

    public int getCurrentItemPosition() {
        return mCurrentPosition;
    }

    /**
     * 获取 Tab
     * @param index
     * @return
     */
    public BottomBarTab getItem(int index){
        if (mTabs.size() < index) return  null;
        return  mTabs.get(index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentPosition);
    }


    static class SavedState extends BaseSavedState{
        private int position;

        public SavedState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SavedState(Parcelable superState, int position) {
            super(superState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void toggle(final boolean visible, final boolean animate, boolean force){
        if (mVisible != visible || force){
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force){
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()){
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()){
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return false;
                        }
                    });
                    return;
                }
            }

            int translationY = visible ? 0 :height;
            if (animate){
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            }else {
                ViewCompat.setTranslationX(this, translationY);
            }
        }
    }

}
