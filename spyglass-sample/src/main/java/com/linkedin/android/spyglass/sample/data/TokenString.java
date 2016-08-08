package com.linkedin.android.spyglass.sample.data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by CarlChia on 5/8/16.
 */
public class TokenString {

    private String rootString = "";
    private String token = "";

    private int startIndex = 0;
    private int endIndex = 0;


    public TokenString(String content, String token, int startIndex, int endIndex) {

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

    public static List<TokenString> tokenizeBySpace(String content) {

        List<TokenString> result = new ArrayList<TokenString>();
        StringTokenizer tokenizer = new StringTokenizer(content, " ", true);

        while(tokenizer.hasMoreTokens()) {

        }



        String[] spittedStrs =  content.split(" ");

        int cursor = 0;

        for(String token : spittedStrs) {

            int startIndex = cursor;


            if(token.contentEquals(" ")) {
                cursor = cursor + token.length() + 1;
            }else{
                cursor = cursor + token.length();
            }

            int endIndex = cursor;


            TokenString tokenString = new TokenString(content, token, startIndex, endIndex);
            result.add(tokenString);
        }

        return result;

    }

}
