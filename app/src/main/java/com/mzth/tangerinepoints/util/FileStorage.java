package com.mzth.tangerinepoints.util;

import android.os.Environment;

import com.mzth.tangerinepoints.common.Constans;

import java.io.File;

/**
 * Created by ddz on 2016/12/28.
 */

public class FileStorage {
    private File cropIconDir;
    private File iconDir;

    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + Constans.APP_NAME;
            cropIconDir = new File(external, rootDir + "/crop");
            if (!cropIconDir.exists()) {
                cropIconDir.mkdirs();

            }
            iconDir = new File(external, rootDir + "/head");
            if (!iconDir.exists()) {
                iconDir.mkdirs();
            }
        }
    }

    public File createIconFile() {

        return iconDir;
    }

}
