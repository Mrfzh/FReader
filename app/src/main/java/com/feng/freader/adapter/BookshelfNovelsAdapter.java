package com.feng.freader.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feng.freader.R;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class BookshelfNovelsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BookshelfNovelsAdapter";

    private Context mContext;
    private List<BookshelfNovelDbData> mDataList;
    private List<Boolean> mCheckedList;
    private BookshelfNovelListener mListener;
    private boolean mIsMultiDelete = false;   // 是否正在进行多选删除

    public interface BookshelfNovelListener {
        void clickItem(int position);
        void longClick(int position);
    }

    public BookshelfNovelsAdapter(Context mContext, List<BookshelfNovelDbData> mDataList,
                                  List<Boolean> mCheckedList, BookshelfNovelListener mListener) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mCheckedList = mCheckedList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_bookshelf_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;
        // 多选删除时显示 CheckBox
        if (mIsMultiDelete) {
            contentViewHolder.checkBox.setVisibility(View.VISIBLE);
            // 先设置 CheckBox 的状态，解决 RecyclerView 对 CheckBox 的复用所造成的影响
            if (mCheckedList.get(i)) {
                contentViewHolder.checkBox.setChecked(true);
            } else {
                contentViewHolder.checkBox.setChecked(false);
            }
        } else {
            contentViewHolder.checkBox.setVisibility(View.GONE);
        }
        contentViewHolder.name.setText(mDataList.get(i).getName());
        if (mDataList.get(i).getType() == 0) {  // 网络小说
            Glide.with(mContext)
                    .load(mDataList.get(i).getCover())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.cover_place_holder)
                            .error(R.drawable.cover_error))
                    .into(contentViewHolder.cover);
        } else if (mDataList.get(i).getType() == 1){    // 本地 txt 小说
            contentViewHolder.cover.setImageResource(R.drawable.local_txt);
        } else if (mDataList.get(i).getType() == 2) {   // 本地 epub 小说
            if (mDataList.get(i).getCover().equals("")) {
                contentViewHolder.cover.setImageResource(R.drawable.local_epub);
            } else {
                String coverPath = mDataList.get(i).getCover();
                Bitmap bitmap = FileUtil.loadLocalPicture(coverPath);
                if (bitmap != null) {
                    contentViewHolder.cover.setImageBitmap(bitmap);
                } else {
                    contentViewHolder.cover.setImageResource(R.drawable.local_epub);
                }
            }
        }

        contentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsMultiDelete) {
                    if (contentViewHolder.checkBox.isChecked()) {
                        contentViewHolder.checkBox.setChecked(false);
                        mCheckedList.set(i, false);
                    } else {
                        contentViewHolder.checkBox.setChecked(true);
                        mCheckedList.set(i, true);
                    }
                } else {
                    mListener.clickItem(i);
                }
            }
        });

        contentViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mIsMultiDelete) {
                    return false;
                }
                mListener.longClick(i);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView cover;
        TextView name;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_item_bookshelf_novel_checked);
            cover = itemView.findViewById(R.id.iv_item_bookshelf_novel_cover);
            name = itemView.findViewById(R.id.tv_item_bookshelf_novel_name);
        }
    }

    public void setIsMultiDelete(boolean mIsMultiDelete) {
        this.mIsMultiDelete = mIsMultiDelete;
    }
}
