package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IBookshelfContract;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.util.EpubUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/12
 */
public class BookshelfModel implements IBookshelfContract.Model {

    private static final String TAG = "BookshelfModel";
    private IBookshelfContract.Presenter mPresenter;
    private DatabaseManager mDbManager;

    private OpfData mOpfData;

    public BookshelfModel(IBookshelfContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mDbManager = DatabaseManager.getInstance();
    }

    /**
     * 从数据库中查询所有书籍信息
     */
    @Override
    public void queryAllBook() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<BookshelfNovelDbData> dataList = mDbManager.queryAllBookshelfNovel();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.queryAllBookSuccess(dataList);
                    }
                });
            }
        }).start();
    }

    /**
     * 解压 epub 文件，解析 opf 文件后，返回 OpfData
     * @param filePath
     */
    @Override
    public void unZipEpub(final String filePath) {
        File file = new File(filePath);
        final String savePath = Constant.EPUB_SAVE_PATH + "/" + file.getName();
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            getOpfData(savePath);
            if (mOpfData != null) {
                unZipEpubSuccess(filePath, mOpfData);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EpubUtils.unZip(filePath, savePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    unZipEpubError("解压失败，可能文件被加密");
                    return;
                }
                getOpfData(savePath);
                if (mOpfData != null) {
                    unZipEpubSuccess(filePath, mOpfData);
                }
            }
        }).start();
    }

    private void getOpfData(String savePath) {
        try {
            // 先得到 opf 文件的位置
            String opfPath = EpubUtils.getOpfPath(savePath);
            Log.d(TAG, "unZipEpub: opfPath = " + opfPath);
            // 解析 opf 文件
            mOpfData = EpubUtils.parseOpf(opfPath);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            unZipEpubError("解压失败，Xml 解析出错");
        } catch (IOException e) {
            e.printStackTrace();
            unZipEpubError("解压失败，I/O 错误");
        }
    }

    private void unZipEpubError(final String errorMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.unZipEpubError(errorMsg);
            }
        });
    }

    private void unZipEpubSuccess(final String filePath, final OpfData opfData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.unZipEpubSuccess(filePath, opfData);
            }
        });
    }
}
