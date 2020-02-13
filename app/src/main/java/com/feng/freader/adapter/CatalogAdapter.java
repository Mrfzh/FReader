package com.feng.freader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feng.freader.R;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/17
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>
    implements View.OnClickListener{
    private static final String TAG = "CatalogAdapter";

    private Context mContext;
    private List<String> mChapterNameList;

    private CatalogListener mListener;

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Log.d(TAG, "onClick: pos = " + pos);
        mListener.clickItem(pos);
    }

    public interface CatalogListener {
        void clickItem(int position);
    }

    public void setOnCatalogListener(CatalogListener listener) {
        mListener = listener;
    }

    public CatalogAdapter(Context mContext, List<String> mChapterNameList) {
        this.mContext = mContext;
        this.mChapterNameList = mChapterNameList;
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CatalogViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_catalog, null));
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder catalogViewHolder, final int i) {
        catalogViewHolder.chapterName.setText(mChapterNameList.get(i));
//        catalogViewHolder.chapterName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.clickItem(i);
//            }
//        });
        catalogViewHolder.chapterName.setOnClickListener(this);
        catalogViewHolder.chapterName.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mChapterNameList.size();
    }

    class CatalogViewHolder extends RecyclerView.ViewHolder{
        TextView chapterName;

        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterName = itemView.findViewById(R.id.tv_item_catalog_chapter_name);
        }

    }
}
