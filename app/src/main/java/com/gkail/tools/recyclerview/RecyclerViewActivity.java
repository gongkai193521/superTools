package com.gkail.tools.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gongkai on 2019/3/10.
 */

public class RecyclerViewActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerview;
    private RecyclerViewAdapter mAdapter;

    @Override
    public int setContentView() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        recyclerview = v(R.id.recyclerview);
        v(R.id.btn1).setOnClickListener(this);
        v(R.id.btn2).setOnClickListener(this);
        v(R.id.btn3).setOnClickListener(this);
        v(R.id.btn4).setOnClickListener(this);
        mAdapter = new RecyclerViewAdapter();
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                //线性布局
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerview.setLayoutManager(layoutManager);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    list.add(i + "线性list");
                }
                mAdapter.setData(list);
                break;
            case R.id.btn2:
                //线性布局
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
                layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerview.setLayoutManager(layoutManager1);
                List<String> list1 = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    list1.add(i + "横向性list");
                }
                mAdapter.setData(list1);
                break;
            case R.id.btn3:
                //网格布局
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, true);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return position % 3 == 0 ? 1 : 2;
                    }
                });
                recyclerview.setLayoutManager(gridLayoutManager);
                List<String> list2 = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    list2.add(i + "网格布局");
                }
                mAdapter.setData(list2);
                break;
            case R.id.btn4:
                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
                List<String> list3 = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    list3.add(i + "瀑布流");
                    list3.add(i + "瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流");
                    list3.add(i + "瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流瀑布流");
                }
                mAdapter.setData(list3);
                break;
            default:
                break;
        }
    }
}
