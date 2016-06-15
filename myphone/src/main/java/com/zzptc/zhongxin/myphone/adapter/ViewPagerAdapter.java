package com.zzptc.zhongxin.myphone.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zhongxin on 2016/4/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> views;

    public ViewPagerAdapter(List<View> views){
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//将视图添加到viewpager中
        container.addView(views.get(position));

        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //将视图从容器中移除
        container.removeView(views.get(position));
    }
}

