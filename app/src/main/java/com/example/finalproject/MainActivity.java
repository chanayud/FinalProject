package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.*;


public class MainActivity extends AppCompatActivity implements WazeAutoCompleteListener {

    private WazeAutoCompleteListener wazeAutoCompleteListener = new WazeAutoCompleteListener() {
        @Override
        public void onWazeAutoCompleteResult(List<AddressSuggestion> result) {
            MainActivity.this.onWazeAutoCompleteResult(result);
        }
    };

    private WazeAutocompleteTask wazeAutocompleteTask;
    private EditText sourceAddress;
    ListView suggestionsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "beforeTextChanged: I'm here");


        sourceAddress = findViewById(R.id.SourceAddressID);
        suggestionsList = findViewById(R.id.suggestionsListID);
        sourceAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TAG", "beforeTextChanged: I'm here");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "onTextChanged: I'm here");
                String sourceAddressValueSTR = s.toString();
                wazeAutocompleteTask = new WazeAutocompleteTask(new WazeAutoCompleteListener() {
                    @Override
                    public void onWazeAutoCompleteResult(List<AddressSuggestion> addressSuggestions) {
                        ArrayAdapter<AddressSuggestion> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, addressSuggestions);
                        suggestionsList.setAdapter(adapter);
                    }
                });
                wazeAutocompleteTask.execute(sourceAddressValueSTR);
                    }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onWazeAutoCompleteResult(List<AddressSuggestion> result) {
        Log.d("address", "Number of suggestions: " + result.size());
        for (AddressSuggestion suggestion : result) {
            Log.d("address", suggestion.getDisplayName() + " (" + suggestion.getLat() + ", " + suggestion.getLon() + ")");
        }

    }
}