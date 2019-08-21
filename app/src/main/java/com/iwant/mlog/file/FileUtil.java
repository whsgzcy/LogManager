package com.iwant.mlog.file;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FileUtil {

    /**
     * 获取指定文件夹下所有文件的名称
     *
     * @param path
     * @return
     */
    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("migu", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    /**
     * 自然排序
     *
     * @param list
     * @return
     */
    public static List<String> sortFilesNameList(List<String> list) {

        if (list == null) return null;
        if (list.size() == 0) return null;

        Collections.sort(list);

        return list;
    }

    public static List<String> deleteFiles(List<String> list) {

        if (list == null) return null;
        if (list.size() == 0) return null;
        if (list.size() < 4) return list;

        //删除策略 4 - 1
        //永远保留最新的3个log文件

        //需求是删除B和D
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext() && list.size() > 3) {
            String value = iterator.next();

            File file = new File(value);

            if (file.isFile() && file.exists()) {
                file.delete();
                iterator.remove();
            }

        }


        return list;
    }

}
