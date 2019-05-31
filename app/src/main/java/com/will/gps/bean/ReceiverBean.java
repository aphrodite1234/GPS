package com.will.gps.bean;

/**
 * Created by MaiBenBen on 2019/5/20.
 */

public class ReceiverBean {
    private String id;
    private String realname;
    private String rlongitude;
    private String rlatitude;
    private int done;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRlongitude() {
        return rlongitude;
    }

    public void setRlongitude(String rlongitude) {
        this.rlongitude = rlongitude;
    }

    public String getRlatitude() {
        return rlatitude;
    }

    public void setRlatitude(String rlatitude) {
        this.rlatitude = rlatitude;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

}
