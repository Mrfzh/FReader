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
 * Created on 2019/10/28
 */
public class BookshelfNovelsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mContentList;

    public BookshelfNovelsAdapter(Context mContext, List<String> mContentList) {
        this.mContext = mContext;
        this.mContentList = mContentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_boosshelf_novels_content, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;
        contentViewHolder.content.setText(mContentList.get(i));
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView content;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_item_bookshelf_novels_content);
        }
    }
}
