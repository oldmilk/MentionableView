package com.linkedin.android.spyglass.sample.data;

/**
 * Created by CarlChia on 5/8/16.
 */
public class MyStringToken {

    private String rootString = "";
    private String token = "";
    private int startIndex = 0;
    private int endIndex = 0;

    public MyStringToken(String content, String token, int startIndex, int endIndex) {

        this.rootString = content;
        this.token = token;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

    }

    public String getRootString() {
        return rootString;
    }

    public void setRootString(String rootString) {
        this.rootString = rootString;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
