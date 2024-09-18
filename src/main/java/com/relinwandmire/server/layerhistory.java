package com.relinwandmire.server;

public class layerhistory extends packet {
    public boolean follower;
    public int location;
    public long to;
    public long from;
    public int layer;
    public int oldtset;
    public int newtset;
    public int oldtile;
    public int newtile;
    public boolean undo;

    public int getOldtset() {
        return this.oldtset;
    }

    public void setOldtset(int oldtset) {
        this.oldtset = oldtset;
    }

    public int getOldtile() {
        return this.oldtile;
    }

    public void setOldtile(int oldtile) {
        this.oldtile = oldtile;
    }

    public int getNewtile() {
        return this.newtile;
    }

    public void setNewtile(int newtile) {
        this.newtile = newtile;
    }

    public boolean isUndo() {
        return this.undo;
    }

    public void setUndo(boolean undo) {
        this.undo = undo;
    }

    public layerhistory() {
    }

    public layerhistory(boolean follower, int layer, int num, long from, long to, int oldtset, int newtset, int oldtile, int newtile) {
        this.follower = follower;
        this.layer = layer;
        this.location = num;
        this.from = from;
        this.to = to;
        this.oldtset = oldtset;
        this.newtset = newtset;
        this.oldtile = oldtile;
        this.newtile = newtile;
    }

    public void setNewtset(int newtset) {
        this.newtset = newtset;
    }

    public int getNewtset() {
        return this.newtset;
    }

    public void setoldTset(int oldtset) {
        this.oldtset = oldtset;
    }

    public int getoldTset() {
        return this.oldtset;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
    }

    public boolean isIdentical(layerhistory lh) {
        return this.location == lh.location && this.to == lh.to && this.from == lh.from && this.layer == lh.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return this.location;
    }

    public boolean isFollower() {
        return this.follower;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getTo() {
        return this.to;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getFrom() {
        return this.from;
    }
}
