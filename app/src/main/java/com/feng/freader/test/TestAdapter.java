package com.feng.freader.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BasePagingLoadAdapter;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class TestAdapter extends BasePagingLoadAdapter<String> {

    public TestAdapter(Context context, List<String> list,
                       LoadMoreListener loadMoreListener) {
        super(context, list, loadMoreListener);
    }

    @Override
    protected int getPageCount() {
        return 20;
    }

    @Override
    protected RecyclerView.ViewHolder setItemViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, null));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestViewHolder viewHolder = (TestViewHolder) holder;
        viewHolder.content.setText(mList.get(position));
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_item_test_content);
        }
    }
}
