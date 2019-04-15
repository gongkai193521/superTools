package com.gkail.tools.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gkail.tools.R;

import java.util.List;

/**
 * Created by gongkai on 2019/3/12.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> mList;

    public void setData(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lineaer_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
//        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
//        params.height = 200 + (position % 4) * 200;
//        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    public ViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.item_lineaer_text);
    }
}
