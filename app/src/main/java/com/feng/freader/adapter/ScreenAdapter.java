package com.feng.freader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.freader.R;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ScreenViewHolder>{

    private Context mContext;
    private List<String> mContentList;
    private List<Boolean> mIsSelectedList;
    private ScreenListener mListener;

    public interface ScreenListener {
        void clickItem(int position);
    }

    public ScreenAdapter(Context mContext, List<String> mContentList,
                         List<Boolean> mIsSelectedList, ScreenListener mListener) {
        this.mContext = mContext;
        this.mContentList = mContentList;
        this.mIsSelectedList = mIsSelectedList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ScreenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ScreenViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_screen, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenViewHolder screenViewHolder, final int i) {
        screenViewHolder.text.setText(mContentList.get(i));
        if (mIsSelectedList.get(i)) {
            screenViewHolder.text.setSelected(true);
        } else {
            screenViewHolder.text.setSelected(false);
        }
        screenViewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class ScreenViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ScreenViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_item_screen_text);
        }
    }
}
