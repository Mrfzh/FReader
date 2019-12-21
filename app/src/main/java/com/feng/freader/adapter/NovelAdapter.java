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
import com.feng.freader.entity.data.ANNovelData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelViewHolder>{

    private Context mContext;
    private List<ANNovelData> mDataList;
    private NovelListener mListener;

    public interface NovelListener {
        void clickItem(String novelName);
    }

    public NovelAdapter(Context mContext, List<ANNovelData> mDataList, NovelListener mListener) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NovelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder novelViewHolder, final int i) {
        novelViewHolder.title.setText(mDataList.get(i).getTitle());
        novelViewHolder.author.setText(mDataList.get(i).getAuthor());
        novelViewHolder.shortInfo.setText(mDataList.get(i).getShortInfo());
        Glide.with(mContext)
                .load(mDataList.get(i).getCover())
                .apply(new RequestOptions()
                    .placeholder(R.drawable.cover_place_holder)
                    .error(R.drawable.cover_error))
                .into(novelViewHolder.cover);
        novelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(mDataList.get(i).getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
