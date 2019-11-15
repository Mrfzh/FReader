package com.feng.freader.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseDialog;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/12
 */
public class TipDialog extends BaseDialog implements View.OnClickListener{

    private OnClickListener mOnClickListener;
    private String mTitle;
    private String mContent;
    private String mEnsure;
    private String mCancel;

    public interface OnClickListener {
        void clickEnsure();
        void clickCancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //将默认背景设置为透明，否则有白底，看不出圆角
    }

    private TipDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View getCustomView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tip, null);
        TextView title = view.findViewById(R.id.tv_dialog_tip_title);
        title.setText(mTitle);
        TextView content = view.findViewById(R.id.tv_dialog_tip_content);
        content.setText(mContent);
        TextView ensure = view.findViewById(R.id.tv_dialog_tip_ensure);
        ensure.setText(mEnsure);
        ensure.setOnClickListener(this);
        TextView cancel = view.findViewById(R.id.tv_dialog_tip_cancel);
        cancel.setText(mCancel);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    protected float getWidthScale() {
        return 0.7f;
    }

    @Override
    protected float getHeightScale() {
        return 0.7f;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dialog_tip_ensure:
                dismiss();
                if (mOnClickListener != null){
                    mOnClickListener.clickEnsure();
                }
                break;
            case R.id.tv_dialog_tip_cancel:
                dismiss();
                if (mOnClickListener != null){
                    mOnClickListener.clickCancel();
                }
                break;
            default:
                break;
        }
    }

    public static class Builder {
        private String title = "! 提示";
        private String content = "提示框内容";
        private String ensure = "是";
        private String cancel = "否";
        private OnClickListener onClickListener = null;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setEnsure(String ensure) {
            this.ensure = ensure;
            return this;
        }

        public Builder setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public TipDialog build() {
            TipDialog tipDialog = new TipDialog(context);
            tipDialog.mTitle = title;
            tipDialog.mContent = content;
            tipDialog.mEnsure = ensure;
            tipDialog.mCancel = cancel;
            tipDialog.mOnClickListener = onClickListener;
            tipDialog.init();

            return tipDialog;
        }
    }
}
