package com.example.steven.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.steven.myapplication.bean.FishBean;
import com.example.steven.myapplication.other.fish.FishManager;
import com.example.steven.myapplication.other.fish.FreePoolView;
import com.example.steven.myapplication.view.ExpandVideoPlayer;

import java.util.List;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    FreePoolView poolView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poolView = findViewById(R.id.poolview);

    }


    public void click(View v){
        FishBean bean = new FishBean();
        poolView.addFish(bean);
    }


    public void cancelTask(View v){

    }


    protected class MyAdapter extends ScrollDataSetAdapter<String>{

        public MyAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public int getDisPlayCount() {
            return 1;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_text;
        }

        @Override
        public void bindView(View view, String s, int position) {
            TextView tv = view.findViewById(R.id.text);
            tv.setText(s);
        }
    }
}


