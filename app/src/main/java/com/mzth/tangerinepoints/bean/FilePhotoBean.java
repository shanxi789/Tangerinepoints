package com.mzth.tangerinepoints.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/21.
 */

public class FilePhotoBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;// 名字
    private String id;// id
    private int type;
    //图片路径
    private String path;
    private String count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof FilePhotoBean) {
            FilePhotoBean bean = (FilePhotoBean) o;
            if(bean.getId().equals(this.getId())) {
                return true;
            }
        }
        return false;
    }
}
