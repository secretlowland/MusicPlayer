package com.andy.music.entity;

import java.io.Serializable;

/**
 * 该类用于描述音乐文件
 * Created by Andy on 2014/11/14.
 */
public class Music implements Serializable {

    private int id;
    private String name;
    private String singer;
    private String path;

    public Music() {}
    public Music(int id) {
        this.id = id;
    }
    public Music(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Music(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    @Override
    public String toString() {
        return "Name : "+this.name + "  Path :  "+this.path +"\n";
    }
}
