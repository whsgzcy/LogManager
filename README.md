## android 日志 收集器

**cpu memory 日志**

需求，收集当前设备运行状态的 cpu 以及 memory数据，这里推荐iflytek的itest4.0可以做个参考，可以把代码运行一下，算一下，唯一需要注意的点是 一个 进程名 可能有多个 子主pid

**日志追踪**

如果有这么一个场景，对于1000(one thousand)台Android设备，每个设备 最多存储1G的log文件，那么日志的总量就是1000*1G(此处为最大值)，对于这么庞大的文件，不可能一个一个登上去查找日志，我们都知道，修复问题，可能修复的是一类问题。

```
主要部分
...
check_log()
{
    GAME_LIST=`ssh $HOSTCHECK $IP "grep -c "\"${WORDS}\"" /sdcard/xxx/*.log "`

    for line in $GAME_LIST
    do
       echo  $line
    done
    return 0
}
...

```

**需求1**

想象有这么一个场景，当线上的问题发生之后再去抓log，这时抓到的log很可能会对不上，这会对开发造成不小的影响，本文就是为了解决这个问题。

本文收集log的策略是“4 - 1 = 3”，即自然排序之后，永远只保存最新的三段log，service中的thread每两个小时执行一次，避免日志过大，占用存储空间。

主要代码逻辑如下：

```
LogRecorder的具体用法网上百度，我也是直接拿过来用，他的好处不仅可以指定对应的类别的log，而且也会对写入文件大小的做限制

LogRecorder.java

logRecorderError = new LogRecorder.Builder(this)
                .setLogFolderName("asdasdsa")
                .setLogFolderPath("/sdcard/1312321")
                .setLogFileNameSuffix("asdasdsad")
                .setLogFileSizeLimitation(10 * 1024)
                .setLogLevel(2)
                .build();
        logRecorderError.start();

ClearLogService.java

...

ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
mScheduledThreadPoolExecutor.scheduleAtFixedRate(mFileThread, 0, 2, TimeUnit.HOURS);

        Thread mFileThread = new Thread() {

        @Override
        public void run() {
            super.run();

            List<String> lists = FileUtil.getFilesAllName("/sdcard/asdasdasd");
            if (lists == null) return;
            FileUtil.sortFilesNameList(lists);

            FileUtil.deleteFiles(lists);

            Log.d("dadsadsa", "***************************");
        }
    };
 ...

 FileUtil.java

 在当前类对集合进行操作的时候没有再分配内存去承载他，只是对对应地址的元素做了修改

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
            Log.e("dasdasdsa", "空目录");
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
```

**需求2**

需求，要对上述代码进行改造，实现 进程起来之后 对未满指定大小的log文件进行追加，不去再生成一个新的文件

```
//将下述代码添加至对应位置即可
...
            // 自然排序
            // 找出最新的log文件
            // 判断是都大于100M

            // 是 则 新建文件

            // 否 则 继续写入当前文件

            List<String> lists = FileUtils.getFilesAllName(Constants.APP_LOG_PATH);

            if (lists != null || !lists.isEmpty()) {
                Collections.sort(lists);

                int lastest = lists.size() - 1;
                String absoluteFilePath = lists.get(lastest);

                long fileSize = FileUtils.getLogFileLength(absoluteFilePath);
                currentFileSize = fileSize;

                NLog.d(TAG, "absoluteFilePath = " + absoluteFilePath);
                NLog.d(TAG, "fileSize = " + fileSize);

                if (fileSize < 100 * 1024 * 1024) {
                    try {
                        out = new FileOutputStream(absoluteFilePath, true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
...
```

**注**

只需参考本文的代码即可
