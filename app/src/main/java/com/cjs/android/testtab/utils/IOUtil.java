package com.cjs.android.testtab.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IOUtil {
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName), "utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }
            inputReader.close();
            bufReader.close();
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IOUtil", "读取assets文件失败");
        }
        return "";
    }
}
