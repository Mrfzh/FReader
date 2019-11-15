package com.feng.freader.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/13
 */
public class BlurUtil {

    /**
     * 使用 RenderScript 对图片进行高斯模糊
     *
     * @param context
     * @param originImage 原图
     * @param blurRadius 模糊半径，取值区间为 (0, 25]
     * @param scaleRatio 缩小比例，假设传入 a，那么图片的宽高是原来的 1 / a 倍，取值 >= 1
     * @return
     */
    public static Bitmap blurBitmap(Context context, Bitmap originImage,
                                    float blurRadius, int scaleRatio) {
        if (blurRadius <= 0 || blurRadius > 25f || scaleRatio < 1) {
            throw new IllegalArgumentException("ensure blurRadius in (0, 25] and scaleRatio >= 1");
        }

        // 计算图片缩小后的宽高
        int width = originImage.getWidth() / scaleRatio;
        int height = originImage.getHeight() / scaleRatio;

        // 创建缩小的 Bitmap
        Bitmap bitmap = Bitmap.createScaledBitmap(originImage, width, height, false);

        // 创建 RenderScript 对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个带模糊效果的工具对象
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于 RenderScript 没有使用 VM 来分配内存，所以需要使用 Allocation 类来创建和分配内存空间
        Allocation input = Allocation.createFromBitmap(rs, bitmap);
        // 创建相同类型的 Allocation 对象用来输出
        Allocation output = Allocation.createTyped(rs, input.getType());

        // 设置渲染的模糊程度，最大为 25f
        blur.setRadius(blurRadius);
        // 设置输入和输出内存
        blur.setInput(input);
        blur.forEach(output);
        // 将数据填充到 Bitmap
        output.copyTo(bitmap);

        // 销毁它们的内存
        input.destroy();
        output.destroy();
        blur.destroy();
        rs.destroy();

        return bitmap;
    }
}
