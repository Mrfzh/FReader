package com.feng.freader.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BasePagingLoadAdapter;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private Context mContext;
    private List<String> mContentList;
    private List<Boolean> mCheckedList;

    public TestAdapter(Context mContext, List<String>
            mContentList, List<Boolean> mCheckedList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        this.mCheckedList = mCheckedList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder testViewHolder, final int i) {
        // 先设置 CheckBox 的状态，解决 RecyclerView 对 CheckBox 的复用所造成的影响
        if (mCheckedList.get(i)) {
            testViewHolder.checkBox.setChecked(true);
        } else {
            testViewHolder.checkBox.setChecked(false);
        }
        testViewHolder.content.setText(mContentList.get(i));
        testViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testViewHolder.checkBox.isChecked()) {
                    testViewHolder.checkBox.setChecked(false);
                    mCheckedList.set(i, false);
                } else {
                    testViewHolder.checkBox.setChecked(true);
                    mCheckedList.set(i, true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CheckBox checkBox;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_item_test_content);
            checkBox = itemView.findViewById(R.id.cb_test);
        }
    }
}
