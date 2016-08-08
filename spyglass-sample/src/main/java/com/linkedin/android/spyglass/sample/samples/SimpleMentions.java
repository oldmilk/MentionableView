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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LogPrinter;

import com.linkedin.android.spyglass.sample.R;
import com.linkedin.android.spyglass.sample.data.models.City;
import com.linkedin.android.spyglass.suggestions.SuggestionsResult;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.RichEditorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple example showing city mentions.
 */
public class SimpleMentions extends AppCompatActivity implements QueryTokenReceiver {

    private static final String BUCKET = "cities-memory";

    private RichEditorView editor;
    private City.CityLoader cities;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_mentions);
        editor = (RichEditorView) findViewById(R.id.editor);
        editor.setQueryTokenReceiver(this);
        editor.setHint(getResources().getString(R.string.type_city));
        cities = new City.CityLoader(getResources());

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("SimpleMentions","CharSequence:"+s+", start:"+start+", count:"+count+", after:"+after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("SimpleMentions","editor.getText():"+editor.getText().toString());

                Log.i("SimpleMentions","editor.getText():"+editor.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("SimpleMentions","Editable:"+s);
            }
        });
    }

    @Override
    public List<String> onQueryReceived(final @NonNull QueryToken queryToken) {
        List<String> buckets = Collections.singletonList(BUCKET);
        List<City> suggestions = cities.getSuggestions(queryToken);
        SuggestionsResult result = new SuggestionsResult(queryToken, suggestions);
        editor.onReceiveSuggestionsResult(result, BUCKET);
        return buckets;
    }
}
