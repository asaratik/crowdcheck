package com.msfthack.crowdcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msfthack.crowdcheck.helpers.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> places = new ArrayList<>();
    List<Locations> locations;
    ArrayAdapter<String> arrayAdapter;
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);

        //Refresh the location data
        refreshLocationData();

        //Update the places array
        for (int i = 0; i < locations.size(); i++) {
            places.add(locations.get(i).getTitle());
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, places);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("location", adapterView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
    }

    private void refreshLocationData()
    {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "data.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Locations>>() { }.getType();
        locations = gson.fromJson(jsonFileString, listUserType);
    }
}