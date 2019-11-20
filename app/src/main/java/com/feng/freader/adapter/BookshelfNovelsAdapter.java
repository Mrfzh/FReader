package com.feng.freader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class BookshelfNovelsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mNameList;

    public BookshelfNovelsAdapter(Context mContext, List<String> mNameList) {
        this.mContext = mContext;
        this.mNameList = mNameList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_bookshelf_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;
        contentViewHolder.name.setText(mNameList.get(i));

        contentViewHolder.cover.setImageResource(R.drawable.default_cover);
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView name;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_item_bookshelf_novel_cover);
            name = itemView.findViewById(R.id.tv_item_bookshelf_novel_name);
        }
    }
}
