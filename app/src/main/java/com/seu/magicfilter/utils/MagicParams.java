package com.seu.magicfilter.utils;

import android.content.Context;
import android.os.Environment;

import com.seu.magicfilter.widget.base.MagicBaseView;

/**
 * Created by why8222 on 2016/2/26.
 */
public class MagicParams {
    public static Context context;
    public static MagicBaseView magicBaseView;

    public static String videoPath = Environment.getExternalStorageDirectory().getPath();//录像保存路径
    public static String videoName = "MagicCamera_test.mp4";//录像文件名称

    public static int beautyLevel = 5;

    public MagicParams() {

    }
}
