package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.IBookshelfContract;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.model.BookshelfModel;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/12
 */
public class BookshelfPresenter extends BasePresenter<IBookshelfContract.View>
        implements IBookshelfContract.Presenter {

    private IBookshelfContract.Model mModel;

    public BookshelfPresenter() {
        mModel = new BookshelfModel(this);
    }

    @Override
    public void queryAllBookSuccess(List<BookshelfNovelDbData> dataList) {
        if (isAttachView()) {
            getMvpView().queryAllBookSuccess(dataList);
        }
    }

    @Override
    public void queryAllBookError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().queryAllBookError(errorMsg);
        }
    }

    @Override
    public void unZipEpubSuccess(String filePath, OpfData opfData) {
        if (isAttachView()) {
            getMvpView().unZipEpubSuccess(filePath, opfData);
        }
    }


    @Override
    public void unZipEpubError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().unZipEpubError(errorMsg);
        }
    }

    @Override
    public void queryAllBook() {
        mModel.queryAllBook();
    }

    @Override
    public void unZipEpub(String filePath) {
        mModel.unZipEpub(filePath);
    }
}
