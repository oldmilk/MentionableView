/*
* Copyright 2015 LinkedIn Corp. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*/

package com.linkedin.android.spyglass.sample.samples;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.linkedin.android.spyglass.mentions.MentionSpan;
import com.linkedin.android.spyglass.sample.R;
import com.linkedin.android.spyglass.sample.data.MyStringToken;
import com.linkedin.android.spyglass.sample.data.MyStringTokenizer;
import com.linkedin.android.spyglass.sample.data.TokenString;
import com.linkedin.android.spyglass.sample.data.models.City;
import com.linkedin.android.spyglass.sample.data.models.Person;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.suggestions.impl.BasicSuggestionsListBuilder;
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible;
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.MentionsEditText;
import com.linkedin.android.spyglass.ui.RichEditorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates how mentions are handled when there is a delay retrieving the suggestions (i.e. over a network).
 */
public class TaggingHexingMentions extends AppCompatActivity implements QueryTokenReceiver {

    private static final String PERSON_BUCKET = "people-database";
    private static final String CITY_BUCKET = "city-network";
//    private static final int PERSON_DELAY = 10;
//    private static final int CITY_DELAY = 2000;

    private RichEditorView editor;
    private Button mButton;

    private Person.PersonLoader people;
    private City.CityLoader cities;

    private SuggestionsResult lastPersonSuggestions;
    private SuggestionsResult lastCitySuggestions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagginghexxing_mentions);

        mButton = (Button) findViewById(R.id.button);
        editor = (RichEditorView) findViewById(R.id.editor);
        editor.setQueryTokenReceiver(this);

        editor.setSuggestionsListBuilder(new CustomSuggestionsListBuilder());

        people = new Person.PersonLoader(getResources());
        cities = new City.CityLoader(getResources());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Encodedstr = editor.getTextWithBusinessLogicString();
                String PlainText = editor.getText().toString();

                MentionSpan[] mentionSpans = editor.getText().getSpans(0, editor.getText().length(), MentionSpan.class);
                for(MentionSpan ms : mentionSpans){
                    int start = editor.getText().getSpanStart(ms);
                    int end = editor.getText().getSpanEnd(ms);
                    Log.i("MentionsEditText", "MentionSpan.Start:"+start+", Matched.End:"+end);
                }

                Log.i("TaggingHexingMentions","Encoded Str:"+Encodedstr);
                Log.i("TaggingHexingMentions","PlainText Str:"+PlainText);

            }
        });

        editor.addTextChangedListener(new TextWatcher() {

            private boolean isSpace = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == null) {
                    return ;
                }

                if(s.length() > 0 && (before==0) && (s.toString().charAt(start) == ' ') ) {
                    isSpace = true;
                }else{
                    isSpace = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(isSpace) {

                    MentionSpan[] mentionSpans = s.getSpans(0, s.length(), MentionSpan.class);

                    MyStringTokenizer tokenizer = new MyStringTokenizer(s.toString());
                    List<MyStringToken> tokenStringList = tokenizer.tokenize();
                    for(int i = 0; i < tokenStringList.size(); i++) {
                        if(tokenStringList.get(i).getToken().length() > 1 && tokenStringList.get(i).getToken().charAt(0) == '#' &&  i < tokenStringList.size()-1 && tokenStringList.get(i+1).getToken().contentEquals(" ")) {
                            boolean isConverted = false;

                            for(MentionSpan ms : mentionSpans){
                                int start = s.getSpanStart(ms);
                                int end = s.getSpanEnd(ms);

                                Log.i("MentionsEditText", "MentionSpan.Start:"+start+", Matched.End:"+end);
                                Log.i("MentionsEditText", "Token.Start:"+tokenStringList.get(i).getStartIndex()+", Token.End:"+tokenStringList.get(i).getEndIndex());

                                if(tokenStringList.get(i).getStartIndex() == start && tokenStringList.get(i).getEndIndex() == end) {

                                    Log.i("MentionsEditText", "MentionSpan Matched");

                                    isConverted = true;
                                    break;
                                }
                            }

                            if(!isConverted) {
                                Log.i("MentionsEditText", "hasNotConverted Token:"+tokenStringList.get(i).getToken()+", Start:"+tokenStringList.get(i).getStartIndex()+", End:"+tokenStringList.get(i).getEndIndex());

                                String cityStr = tokenStringList.get(i).getToken().substring(1, tokenStringList.get(i).getToken().length());
                                City newCity = new City(cityStr);
                                editor.insertMentionInternal(newCity, tokenStringList.get(i).getStartIndex(), tokenStringList.get(i).getEndIndex());
                            }else{
                                Log.i("MentionsEditText", "hasConvert");

                            }
                        }

                    }

//                    MentionSpan[] mentionSpans = s.getSpans(0, s.length(), MentionSpan.class);
//
//                    if(mentionSpans.length == 0) {
//
//                        MyStringTokenizer tokenizer = new MyStringTokenizer(s.toString());
//                        List<MyStringToken> tokenStringList = tokenizer.tokenize();
//                        for(int i = 0; i < tokenStringList.size(); i++) {
//                            if(tokenStringList.get(i).getToken().length() > 1 && tokenStringList.get(i).getToken().charAt(0) == '#' &&  i < tokenStringList.size()-1 && tokenStringList.get(i+1).getToken().contentEquals(" ")) {
//                                String cityStr = tokenStringList.get(i).getToken().substring(1, tokenStringList.get(i).getToken().length());
//                                City newCity = new City(cityStr);
//                                editor.insertMentionInternal(newCity, tokenStringList.get(i).getStartIndex(), tokenStringList.get(i).getEndIndex()+1);
//                            }
//                        }
//                    }else{
//                        MyStringTokenizer tokenizer = new MyStringTokenizer(s.toString());
//                        List<MyStringToken> tokenStringList = tokenizer.tokenize();
//                        for(int i = 0; i < tokenStringList.size(); i++) {
//                            if(tokenStringList.get(i).getToken().length() > 1 && tokenStringList.get(i).getToken().charAt(0) == '#' &&  i < tokenStringList.size()-1 && tokenStringList.get(i+1).getToken().contentEquals(" ")) {
//                                boolean isConverted = false;
//
//                                for(MentionSpan ms : mentionSpans){
//                                    int start = s.getSpanStart(ms);
//                                    int end = s.getSpanEnd(ms);
//
//                                    Log.i("MentionsEditText", "MentionSpan.Start:"+start+", Matched.End:"+end);
//                                    Log.i("MentionsEditText", "Token.Start:"+tokenStringList.get(i).getStartIndex()+", Token.End:"+tokenStringList.get(i).getEndIndex());
//
//                                    if(tokenStringList.get(i).getStartIndex() == start && tokenStringList.get(i).getEndIndex() == end) {
//
//                                        Log.i("MentionsEditText", "MentionSpan Matched");
//
//                                        isConverted = true;
//                                        break;
//                                    }
//                                }
//
//                                if(!isConverted) {
//                                    Log.i("MentionsEditText", "hasNotConverted Token:"+tokenStringList.get(i).getToken()+", Start:"+tokenStringList.get(i).getStartIndex()+", End:"+tokenStringList.get(i).getEndIndex());
//
//                                    String cityStr = tokenStringList.get(i).getToken().substring(1, tokenStringList.get(i).getToken().length());
//                                    City newCity = new City(cityStr);
//                                    editor.insertMentionInternal(newCity, tokenStringList.get(i).getStartIndex(), tokenStringList.get(i).getEndIndex());
//                                }else{
//                                    Log.i("MentionsEditText", "hasConvert");
//
//                                }
//                            }
//
//                        }
//
//
//
//                    }

                }





            }
        });
    }

    // --------------------------------------------------
    // QueryTokenReceiver Implementation
    // --------------------------------------------------

    @Override
    public List<String> onQueryReceived(final @NonNull QueryToken queryToken) {

//        Log.i("onQueryReceived","queryToken.getExplicitChar():"+queryToken.getExplicitChar());
//        Log.i("onQueryReceived","queryToken.getKeywords():"+queryToken.getKeywords());
//        Log.i("onQueryReceived","queryToken.getTokenString():"+queryToken.getTokenString());

//        Log.i("onQueryReceived","editor.getTextWithBusinessLogicString():"+editor.getTextWithBusinessLogicString());

        final List<String> buckets = new ArrayList<>();
        final SuggestionsResultListener listener = editor;

        if(queryToken.getExplicitChar() == '@') {
            buckets.add(PERSON_BUCKET);
            List<Person> suggestions = people.getSuggestions(queryToken);
            lastPersonSuggestions = new SuggestionsResult(queryToken, suggestions);
            listener.onReceiveSuggestionsResult(lastPersonSuggestions, PERSON_BUCKET);

        }else if(queryToken.getExplicitChar() == '#') {
            buckets.add(CITY_BUCKET);
            List<City> suggestions = cities.getSuggestions(queryToken);
            lastCitySuggestions = new SuggestionsResult(queryToken, suggestions);
            listener.onReceiveSuggestionsResult(lastCitySuggestions, CITY_BUCKET);
        }

        // Return buckets, one for each source (serves as promise to editor that we will call
        // onReceiveSuggestionsResult at a later time)
        return buckets;
    }

    // --------------------------------------------------
    // Inner class to customize appearance of suggestions
    // --------------------------------------------------

    private class CustomSuggestionsListBuilder extends BasicSuggestionsListBuilder {

        @NonNull
        @Override
        public View getView(@NonNull Suggestible suggestion,
                            @Nullable View convertView,
                            ViewGroup parent,
                            @NonNull Context context,
                            @NonNull LayoutInflater inflater,
                            @NonNull Resources resources) {

            View v =  super.getView(suggestion, convertView, parent, context, inflater, resources);
            if (!(v instanceof TextView)) {
                return v;
            }

            // Color text depending on the type of mention
            TextView tv = (TextView) v;
            if (suggestion instanceof Person) {
                tv.setTextColor(getResources().getColor(R.color.person_mention_text));
            } else if (suggestion instanceof City) {
                tv.setTextColor(getResources().getColor(R.color.city_mention_text));
            }

            return tv;
        }
    }


}