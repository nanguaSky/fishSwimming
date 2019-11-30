package com.example.steven.myapplication;

import android.view.View;

import com.example.steven.myapplication.view.VerticalScrollLayout;

import java.lang.ref.SoftReference;
import java.util.List;

public abstract class ScrollDataSetAdapter<T> {

    private List<T> datas;

    private SoftReference<VerticalScrollLayout> attachView;


    public ScrollDataSetAdapter(List<T> datas) {
        this.datas = datas;
    }


    public int count() {
        return datas == null ? 0 : datas.size();
    }


    //要显示几行 view
    public abstract int getDisPlayCount();


    //child view
    public abstract int getLayoutId();


    //bind data to view
    public abstract void bindView(View view, T t, int position);


    public void addData(T t){
        if(datas != null && attachView.get() != null){
            attachView.get().addData(t);
        }
    }


    public List<T> getDatas() {
        return datas;
    }


    public void setAttachView(VerticalScrollLayout view){
        attachView = new SoftReference<>(view);
    }
}
