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
import com.feng.freader.base.BasePagingLoadAdapter;
import com.feng.freader.constant.Constant;
import com.feng.freader.entity.data.ANNovelData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class NovelAdapter extends BasePagingLoadAdapter<ANNovelData> {

    private NovelListener mListener;

    public NovelAdapter(Context mContext, List<ANNovelData> mList,
                        LoadMoreListener loadMoreListener, NovelListener novelListener) {
        super(mContext, mList, loadMoreListener);
        mListener = novelListener;
    }

    public interface NovelListener {
        void clickItem(String novelName);
    }

    @Override
    protected int getPageCount() {
        return Constant.NOVEL_PAGE_NUM;
    }

    @Override
    protected RecyclerView.ViewHolder setItemViewHolder(ViewGroup parent, int viewType) {
        return new NovelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_novel, null));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NovelViewHolder novelViewHolder = (NovelViewHolder) holder;
        novelViewHolder.title.setText(mList.get(position).getTitle());
        novelViewHolder.author.setText(mList.get(position).getAuthor());
        novelViewHolder.shortInfo.setText(mList.get(position).getShortInfo());
        Glide.with(mContext)
                .load(mList.get(position).getCover())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.cover_place_holder)
                        .error(R.drawable.cover_error))
                .into(novelViewHolder.cover);
        novelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(mList.get(position).getTitle());
            }
        });
    }

    class NovelViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView author;
        TextView shortInfo;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_item_novel_cover);
            title = itemView.findViewById(R.id.tv_item_novel_title);
            author = itemView.findViewById(R.id.tv_item_novel_author);
            shortInfo = itemView.findViewById(R.id.tv_item_novel_short_info);
        }
    }
}
