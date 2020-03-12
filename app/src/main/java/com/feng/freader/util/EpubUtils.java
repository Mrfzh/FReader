package com.feng.freader.util;

import android.util.Log;

import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.entity.epub.EpubTocItem;
import com.feng.freader.entity.epub.OpfData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EpubUtils {

    private static final String TAG = "EpubUtils";

    /**
     * 将 filePath 的文件解压到 savePath 中
     *
     * PS: 这是一个耗时操作，最好放在子线程进行
     */
    public static void unZip(String filePath, String savePath) throws IOException {
        File sourceFile = new File(filePath);
        ZipFile zipFile = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try{
            zipFile = new ZipFile(sourceFile);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = savePath + "/" + entry.getName();
                    createDirIfNotExist(dirPath);
                } else {
                    File targetFile = new File(savePath + "/" + entry.getName());
                    createFileIfNotExist(targetFile);
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                }
            }
        } finally {
            if(zipFile != null){
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createDirIfNotExist(String path){
        File file = new File(path);
        createDirIfNotExist(file);
    }

    private static void createDirIfNotExist(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

    private static void createParentDirIfNotExist(File file){
        createDirIfNotExist(file.getParentFile());
    }


    /**
     * 得到 opf 文件的位置
     */
    public static String getOpfPath(String savePath) throws XmlPullParserException, IOException {
        String containerPath = savePath + "/META-INF/container.xml";
        String opfPath = "";
        XmlPullParser pullParser;
        InputStreamReader inputStreamReader = null;
        try {
            // 创建Pull解析器
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            pullParser = factory.newPullParser();
            // 设置xml数据源
            inputStreamReader = new InputStreamReader(new FileInputStream(containerPath));
            pullParser.setInput(inputStreamReader);

            int eventType = pullParser.getEventType();
            // 开始解析。如果解析遇到的事件是文件解析结束的话就退出循环
            boolean flag = false;
            while (!flag && eventType != XmlPullParser.END_DOCUMENT) {
                String currentNodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // 开始解析某个节点
                        if (currentNodeName.equals("rootfile")) {
                            opfPath = savePath + "/" + pullParser
                                    .getAttributeValue(null, "full-path");
                            flag = true;
                        }
                        break;
                    default:
                        break;
                }
                // 继续解析下一个事件
                eventType = pullParser.next();
            }
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }


        return opfPath;
    }

    /**
     * 解析 opf 文件
     */
    public static OpfData parseOpf(String opfFilePath) throws XmlPullParserException, IOException {
        OpfData opfData = new OpfData();
        XmlPullParser pullParser;
        InputStreamReader inputStreamReader = null;
        File opfFile = new File(opfFilePath);
        opfData.setParentPath(opfFile.getParent());
        try {
            // 创建Pull解析器
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            pullParser = factory.newPullParser();
            // 设置xml数据源
            inputStreamReader = new InputStreamReader(new FileInputStream(opfFilePath));
            pullParser.setInput(inputStreamReader);

            List<String> spine = new ArrayList<>();
            // key 为文件 id，value 为文件相对位置（绝对位置为 opfFilePath 的父文件夹 + "/" + 相对位置）
            HashMap<String, String> hashMap = new HashMap<>();
            // 保存封面的 id
            String coverId = "";
            int eventType = pullParser.getEventType();
            // 开始解析。如果解析遇到的事件是文件解析结束的话就退出循环
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String currentNodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // 开始解析某个节点
                        switch (currentNodeName) {
                            case "dc:title":
                                String title = pullParser.nextText();
                                opfData.setTitle(title);
                                break;
                            case "meta":
                                if (pullParser.getAttributeValue(null, "name")
                                        .equals("cover")) {
                                    coverId = pullParser.getAttributeValue(null, "content");
                                }
                                break;
                            case "item":
                                String id = pullParser.getAttributeValue(null, "id");
                                String href = pullParser.getAttributeValue(null, "href");
                                hashMap.put(id, href);
                                break;
                            case "spine":
                                String toc = pullParser.getAttributeValue(null, "toc");
                                String ncx = opfFile.getParent() + "/" + hashMap.get(toc);
                                opfData.setNcx(ncx);
                                break;
                            case "itemref":
                                String idref = pullParser.getAttributeValue(null, "idref");
                                String textPath = opfFile.getParent() + "/" + hashMap.get(idref);
                                spine.add(textPath);
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // 结束某个节点的解析
                        if (currentNodeName.equals("manifest")) {
                            if (!coverId.equals("") && hashMap.containsKey(coverId)) {
                                String coverPath = opfFile.getParent() + "/" + hashMap.get(coverId);
                                opfData.setCover(coverPath);
                            }
                        } else if (currentNodeName.equals("spine")) {
                            opfData.setSpine(spine);
                        }
                        break;
                    default:
                        break;
                }
                // 继续解析下一个事件
                eventType = pullParser.next();
            }
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }

        return opfData;
    }

    /**
     * 解析 ncx 文件，得到目录信息
     */
    public static List<EpubTocItem> getTocData(String ncxPath) throws XmlPullParserException, IOException {
        List<EpubTocItem> dataList = new ArrayList<>();
        XmlPullParser pullParser;
        InputStreamReader inputStreamReader = null;
        File ncxFile = new File(ncxPath);
        try {
            // 创建Pull解析器
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            pullParser = factory.newPullParser();
            // 设置xml数据源
            inputStreamReader = new InputStreamReader(new FileInputStream(ncxPath));
            pullParser.setInput(inputStreamReader);

            EpubTocItem item = null;
            boolean flag = false;
            int eventType = pullParser.getEventType();
            // 开始解析。如果解析遇到的事件是文件解析结束的话就退出循环
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String currentNodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // 开始解析某个节点
                        switch (currentNodeName) {
                            case "navPoint":
                                flag = true;
                                item = new EpubTocItem();
                                break;
                            case "text":
                                if (!flag) {
                                    break;
                                }
                                item.setTitle(pullParser.nextText());
                                break;
                            case "content":
                                if (!flag) {
                                    break;
                                }
                                String path = ncxFile.getParent() + "/" + pullParser
                                        .getAttributeValue(null, "src");
                                item.setPath(path);
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // 结束某个节点的解析
                        if (currentNodeName.equals("navPoint")) {
                            flag = false;
                            dataList.add(item);
                        }
                        break;
                    default:
                        break;
                }
                // 继续解析下一个事件
                eventType = pullParser.next();
            }
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }

        return dataList;
    }

    /**
     * 解析 html/xhtml 文件，得到章节信息
     */
    public static List<EpubData> getEpubData(String parentPath, String filePath) throws IOException {
        Log.d(TAG, "getEpubData: filePath = " + filePath);
        List<EpubData> dataList = new ArrayList<>();
        File file = new File(filePath);
        Document document = Jsoup.parse(file, null);
        Elements allElements = document.getAllElements();
        StringBuilder builder = new StringBuilder();    // 存放文本
        for (Element element : allElements) {
            Log.d(TAG, "getEpubData: run");
            // 该值大于 1 时，表示内部还有其他标签，跳过
            if (element.getAllElements().size() > 1) {
                continue;
            }
            // <div> 获取一个段落
            if (element.is("div")) {
                if (element.text().equals("")) {
                    continue;
                }
                builder.append("    ").append(element.text().trim());
                builder.append("\n");
            }
            // <p> 获取一个段落
            else if (element.is("p")) {
                if (element.text().equals("")) {
                    continue;
                }
                builder.append("    ").append(element.text().trim());
                builder.append("\n");
            }
            // <img> 获取图片地址
            else if (element.is("img")) {
                if (element.attr("src").equals("")) {
                    continue;
                }
                if (!builder.toString().equals("")) {
                    EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
                    dataList.add(epubData);
                    builder = new StringBuilder();
                }
                String srcValue = element.attr("src");
                String picPath = getImagePath(parentPath, srcValue);
                String altValue = element.attr("alt");
                String secondPath = "";
                if (!altValue.equals("")) {
                    int last = srcValue.lastIndexOf("/");
                    if (last == -1) {
                        secondPath = getImagePath(parentPath, altValue);
                    } else {
                        altValue = srcValue.substring(0, last+1) + altValue;
                        secondPath = getImagePath(parentPath, altValue);
                    }
                    last = srcValue.lastIndexOf(".");
                    if (last != -1) {
                        secondPath = secondPath + srcValue.substring(last);
                    }
                }
                EpubData epubData = new EpubData(picPath, secondPath, EpubData.TYPE.IMG);
                dataList.add(epubData);
            }
            // <a> 获取超链接
            else if (element.is("a")) {
                if (element.text().equals("")) {
                    continue;
                }
                // 先简化一下，将超链接当作普通文本
                builder.append(element.text());
                builder.append("\n");
            }
            // <h1> - <h6>, 获取标题
            else if (element.is("h1") || element.is("h2") ||
                    element.is("h3") || element.is("h4") ||
                    element.is("h5") || element.is("h6")) {
                if (element.text().equals("")) {
                    continue;
                }
                if (!builder.toString().equals("")) {
                    EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
                    dataList.add(epubData);
                    builder = new StringBuilder();
                }
                EpubData epubData = new EpubData(element.text(), EpubData.TYPE.TITLE);
                dataList.add(epubData);
            }
        }
        if (!builder.toString().equals("")) {
            EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
            dataList.add(epubData);
        }

        return dataList;
    }


    private static String getImagePath(String parentPath, String value) {
        String picPath;
        if (value.length() >= 2 && value.substring(0, 2).equals("..")) {
            picPath = parentPath + value.substring(value.indexOf("/"));
        } else if (value.length() >= 1 && value.substring(0, 1).equals("/")) {
            picPath = parentPath + value;
        } else {
            picPath = parentPath + "/" + value;
        }
        return picPath;
    }
}
