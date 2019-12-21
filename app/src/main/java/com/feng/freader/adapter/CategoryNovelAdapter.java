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

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class CategoryNovelAdapter extends RecyclerView.Adapter<CategoryNovelAdapter.CategoryNovelViewHolder> {

    private Context mContext;
    private List<String> mCoverList;
    private List<String> mNameList;

    public CategoryNovelAdapter(Context mContext, List<String> mCoverList, List<String> mNameList) {
        this.mContext = mContext;
        this.mCoverList = mCoverList;
        this.mNameList = mNameList;
    }

    @NonNull
    @Override
    public CategoryNovelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryNovelViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_category_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryNovelViewHolder categoryNovelViewHolder, int i) {
        Glide.with(mContext)
                .load(mCoverList.get(i))
                .apply(new RequestOptions()
                    .placeholder(R.drawable.cover_place_holder)
                    .error(R.drawable.cover_error))
                .into(categoryNovelViewHolder.cover);
        categoryNovelViewHolder.name.setText(mNameList.get(i));
    }

    @Override
    public int getItemCount() {
        return mCoverList.size();
    }

    class CategoryNovelViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;

        public CategoryNovelViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_item_category_novel_cover);
            name = itemView.findViewById(R.id.tv_item_category_novel_name);
        }
    }
}
