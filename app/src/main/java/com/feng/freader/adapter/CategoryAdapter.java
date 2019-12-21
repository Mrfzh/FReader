package com.feng.freader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.entity.data.DiscoveryNovelData;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/21
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context mContext;
    private List<String> mCategoryNameList;
    private List<String> mMoreList;
    private List<DiscoveryNovelData> mNovelDataList;
    private CategoryListener mListener;

    public interface CategoryListener {
        void clickNovel(String novelName);
        void clickMore(int position);
    }

    public CategoryAdapter(Context mContext, List<String> mCategoryNameList,
                           List<String> mMoreList, List<DiscoveryNovelData> mNovelDataList,
                           CategoryListener mListener) {
        this.mContext = mContext;
        this.mCategoryNameList = mCategoryNameList;
        this.mMoreList = mMoreList;
        this.mNovelDataList = mNovelDataList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_category, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, final int i) {
        categoryViewHolder.categoryName.setText(mCategoryNameList.get(i));
        categoryViewHolder.moreTv.setText(mMoreList.get(i));
        categoryViewHolder.novelList.setLayoutManager(new GridLayoutManager(mContext, 3));
        CategoryNovelAdapter adapter = new CategoryNovelAdapter(mContext,
                mNovelDataList.get(i).getCoverUrlList(), mNovelDataList.get(i).getNovelNameList());
        adapter.setOnCategoryNovelListener(new CategoryNovelAdapter.CategoryNovelListener() {
            @Override
            public void clickItem(String novelName) {
                mListener.clickNovel(novelName);
            }
        });
        categoryViewHolder.novelList.setAdapter(adapter);

        categoryViewHolder.moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickMore(i);
            }
        });
        categoryViewHolder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickMore(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryNameList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView moreTv;
        ImageView moreIv;
        RecyclerView novelList;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.tv_item_category_category_name);
            moreTv = itemView.findViewById(R.id.tv_item_category_more);
            moreIv = itemView.findViewById(R.id.iv_item_category_more);
            novelList = itemView.findViewById(R.id.rv_item_category_novel_list);
        }
    }
}
