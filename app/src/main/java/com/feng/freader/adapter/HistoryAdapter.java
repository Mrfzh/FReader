package com.feng.freader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.util.BaseUtil;
import com.feng.freader.widget.FlowLayout;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/11
 */
public class HistoryAdapter extends FlowLayout.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context mContext;
    private List<String> mContentList;

    public HistoryAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_history, parent, false);
        // 给 View 设置 margin
        ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        int margin = BaseUtil.dip2px(mContext, 5);
        mlp.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(mlp);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.content.setText(mContentList.get(position));
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class HistoryViewHolder extends FlowLayout.ViewHolder {
        TextView content;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_item_history_content);
        }
    }
}
