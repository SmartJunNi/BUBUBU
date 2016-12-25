package cn.edu.nini.bububu.common.utils;

import android.text.format.Formatter;

import java.io.File;

import cn.edu.nini.bububu.base.BaseApplication;

public class FileSizeUtil {
        public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
        public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
        public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
        public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值


        /**
         * 自动判断是否是文件还是文件夹
         *
         * @param filePath
         * @return
         */
        public static String getAutoFileOrFolderSize(String filePath) {
            File file=new File(filePath);
            long size = 0;
            try {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        size = getFolderSize(file);
                    } else {
                        size = getFileSize(file);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Formatter.formatFileSize(BaseApplication.getAppContext(), (long) formatSize(size, SIZETYPE_MB));
        }

        /**
         * 得到文件大小
         *
         * @param file
         * @return 以Byte为单位的文件大小
         */
        public static long getFileSize(File file) {
            long size = 0;
            if (file.exists()) {
                size = file.length();
            }
            return size;
        }

        /**
         * 得到文件夹的大小
         *
         * @param file
         * @return
         */
        public static long getFolderSize(File file) {
            long size = 0;
            try {
                File[] listFiles = file.listFiles();
                System.out.println(file.isDirectory());
                for (File f : listFiles) {
                    System.out.println(f.toString());
                    // 是文件夹
                    if (f.isDirectory()) {
                        size += getFolderSize(f);
                    } else {
                        size += f.length();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return size;
        }

        public static double formatSize(double size, int sizeType) {
            switch (sizeType) {
                case SIZETYPE_B:
                    return size;

                case SIZETYPE_KB:
                    return size / 1024;

                case SIZETYPE_MB:
                    return size / 1024 / 1024;

                case SIZETYPE_GB:
                    return size / 1024 / 1024 / 1024;

            }
            return 0;
        }

    }
