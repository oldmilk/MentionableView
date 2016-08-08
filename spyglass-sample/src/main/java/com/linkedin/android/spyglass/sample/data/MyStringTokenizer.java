package com.linkedin.android.spyglass.sample.data;

/**
 * Created by CarlChia on 5/8/16.
 */


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

public class MyStringTokenizer {

    private String string;
    private String delimiters;
    private boolean returnDelimiters;
    private int position;


    public MyStringTokenizer(String string) {
        this(string, " \t\n\r\f", true);
    }

    private MyStringTokenizer(String string, String delimiters,
                           boolean returnDelimiters) {
        if (string == null) {
            throw new NullPointerException("string == null");
        }
        this.string = string;
        this.delimiters = delimiters;
        this.returnDelimiters = returnDelimiters;
        this.position = 0;
    }

    public List<MyStringToken> tokenize() {

        List<MyStringToken> result = new ArrayList<MyStringToken>();


        while(hasMoreTokens()) {
            result.add(nextToken());
        }

        return result;

    }

    private boolean hasMoreTokens() {
        if (delimiters == null) {
            throw new NullPointerException("delimiters == null");
        }
        int length = string.length();
        if (position < length) {
            if (returnDelimiters)
                return true; // there is at least one character and even if
            // it is a delimiter it is a token

            // otherwise find a character which is not a delimiter
            for (int i = position; i < length; i++)
                if (delimiters.indexOf(string.charAt(i), 0) == -1)
                    return true;
        }
        return false;
    }

    /**
     * Returns the next token in the string as a {@code String}.
     *
     * @return next token in the string as a {@code String}.
     * @throws NoSuchElementException
     *                if no tokens remain.
     */
    private MyStringToken nextToken() {
        if (delimiters == null) {
            throw new NullPointerException("delimiters == null");
        }
        int i = position;
        int length = string.length();

        if (i < length) {
            if (returnDelimiters) {
                if (delimiters.indexOf(string.charAt(position), 0) >= 0) {

                    int startIndex = position;
                    int endIndex = position + 1;

                    String str = String.valueOf(string.charAt(position++));

                    MyStringToken token = new MyStringToken(string, str, startIndex, endIndex);

                    return token;
                }

                for (position++; position < length; position++) {
                    if (delimiters.indexOf(string.charAt(position), 0) >= 0){

                        int startIndex = i;
                        int endIndex = position;
                        String str = string.substring(i, position);
                        MyStringToken token = new MyStringToken(string, str, startIndex, endIndex);
                        return token;
                    }
                }

                int startIndex = i;
                int endIndex = string.length();
                String str = string.substring(i);
                MyStringToken token = new MyStringToken(string, str, startIndex, endIndex);
                return token;
            }

            while (i < length && delimiters.indexOf(string.charAt(i), 0) >= 0){
                i++;
            }

            position = i;
            if (i < length) {
                for (position++; position < length; position++) {
                    if (delimiters.indexOf(string.charAt(position), 0) >= 0) {
                        int startIndex = i;
                        int endIndex = position;
                        String str = string.substring(i, position);
                        MyStringToken token = new MyStringToken(string, str, startIndex, endIndex);

                        return token;
                    }
                }

                int startIndex = i;
                int endIndex = string.length();
                String str = string.substring(i);
                MyStringToken token = new MyStringToken(string, str, startIndex, endIndex);
                return token;
            }
        }
        throw new NoSuchElementException();
    }

}

