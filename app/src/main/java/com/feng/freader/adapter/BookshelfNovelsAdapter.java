package com.feng.freader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feng.freader.R;
import com.feng.freader.entity.data.BookshelfNovelDbData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class BookshelfNovelsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BookshelfNovelDbData> mDataList;

    private BookshelfNovelListener mListener;

    public interface BookshelfNovelListener {
        void clickItem(int position);
    }

    public void setBookshelfNovelListener(BookshelfNovelListener mListener) {
        this.mListener = mListener;
    }

    public BookshelfNovelsAdapter(Context mContext, List<BookshelfNovelDbData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_bookshelf_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;

        contentViewHolder.name.setText(mDataList.get(i).getName());

        Glide.with(mContext)
                .load(mDataList.get(i).getCover())
                .apply(new RequestOptions()
                    .placeholder(R.drawable.cover_place_holder)
                    .error(R.drawable.cover_error))
                .into(contentViewHolder.cover);

        contentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
