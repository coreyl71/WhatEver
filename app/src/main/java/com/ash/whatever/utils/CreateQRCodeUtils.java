package com.ash.whatever.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * Created by Corey on 2016/6/30.
 */
public final class CreateQRCodeUtils {
    private static final int BLACK = 0xff000000;

    public static Bitmap createQRCode(String str, int widthAndHeight, Bitmap icon) throws WriterException {
        // 针对icon图像进行缩小，不能太大 会影响结果
        icon = Bitmap.createScaledBitmap(icon,70,70,true);
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        // 设置稍后解码时对文字使用的字符编码类型
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        // 根据字符串得到指定的二维码图形的矩阵（二维码图片每一个像素点上要显示的像素值）
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        // 遍历BitMatrix中的每一个点，然后给数组中的元素赋值
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //判断当前的x，y点位置是否位于中心头像位置
                if (x > (width / 2 - icon.getWidth() / 2) && x < (width / 2 + icon.getWidth() / 2) && y > (height / 2 - icon.getHeight() / 2) && y < (height / 2 + icon.getHeight() / 2)) {
                    pixels[y * width + x] = icon.getPixel(x-(width / 2 - icon.getWidth() / 2),y-(height / 2 - icon.getHeight() / 2));
                } else {
                    // 判断当前x , y 点上的坐标是否需要显示有效数据
                    if (matrix.get(x, y)) {
                        // 海水蓝渐变
                        if (y < height / 8) {
                            pixels[y * width + x] = 0xff000000; // 外圈显示不了
                        } else if ((y < height / 4)) {
                            pixels[y * width + x] = 0xff009DD9;
                        } else if ((y < (3 * height / 8))) {
                            pixels[y * width + x] = 0xff00AAEA;
                        } else if ((y < height / 2)) {
                            pixels[y * width + x] = 0xff00B3F7;
                        } else if ((y < (5 * height / 8))) {
                            pixels[y * width + x] = 0xff0BBCFF;
                        } else if ((y < (3 * height / 4))) {
                            pixels[y * width + x] = 0xff24C2FF;
                        } else if ((y < (7 * height / 8))) {
                            pixels[y * width + x] = 0xff4ACDFF;
                        } else {
                            pixels[y * width + x] = 0xff000000; // 外圈显示不了
                        }

                        // 紫色渐变
                    /*if (y < height / 8) {
                        pixels[y * width + x] = 0xff000000; // 外圈显示不了
                    } else if ((y < height / 4)) {
                        pixels[y * width + x] = 0xff30255C;
                    } else if ((y < (3 * height / 8))) {
                        pixels[y * width + x] = 0xff382C6B;
                    } else if ((y < height / 2)) {
                        pixels[y * width + x] = 0xff473689;
                    } else if ((y < (5 * height / 8))) {
                        pixels[y * width + x] = 0xff523F9E;
                    } else if ((y < (3 * height / 4))) {
                        pixels[y * width + x] = 0xff5C47B1;
                    } else if ((y < (7 * height / 8))) {
                        pixels[y * width + x] = 0xff6C58BC;
                    } else {
                        pixels[y * width + x] = 0xff000000; // 外圈显示不了
                    }*/
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 让bitmap图片上显示数组中指定的像素内容
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
