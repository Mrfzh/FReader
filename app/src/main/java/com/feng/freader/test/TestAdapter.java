package com.feng.freader.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.freader.R;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private Context mContext;
    private int mNum;

    public TestAdapter(Context mContext, int mNum) {
        this.mContext = mContext;
        this.mNum = mNum;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder testViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mNum;
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
