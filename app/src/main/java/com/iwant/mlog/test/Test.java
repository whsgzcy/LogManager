package com.iwant.mlog.test;

import com.iwant.mlog.file.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {

        // /sdcard/asdas/asdasd-2019-08-21-11-06-46.log
        // 字符串截取

        // output ---> 2019-08-21-11-06-46

        String s = "/sdcard/migu/asdas-2019-08-21-11-06-46.log";

        s = s.replace(".log", "");

        String[] array = s.split("-");

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

        System.out.println(array[1] + "-" + array[2] + "-" + array[3] + "-" + array[4] + "-" + array[5] + "-" + array[6]);

        List<String> ts = new ArrayList<>();
        ts.add("/sdcard/dasdsaasdasd/21312312312.log");
        ts.add("/sdcard/asdas/sadasd-2019-08-25-11-06-46.log");
        ts.add("/sdcard/asdas/asdas-2019-08-22-11-06-46.log");
        ts.add("/sdcard/asdasa/asdas-2019-08-28-11-06-46.log");
        ts.add("/sdcard/asdas/asdasdsa-2019-08-24-11-06-46.log");

        FileUtil.sortFilesNameList(ts);

        System.out.println();

        for (int i = 0; i < ts.size(); i++) {
            System.out.println(ts.get(i));
        }

        System.out.println();
        FileUtil.deleteFiles(ts);

        for (int i = 0; i < ts.size(); i++) {
            System.out.println(ts.get(i));
        }

    }
}
