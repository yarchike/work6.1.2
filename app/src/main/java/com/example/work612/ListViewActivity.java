package com.example.work612;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    static final String KEY1 = "Key1";
    static final String KEY2 = "Key2";
    static final String DATAS = "DataS";
    ArrayList<Integer> alist = new ArrayList<>();

    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

    ListView list;
    SharedPreferences sharedPref;
    SharedPreferences.Editor myEditor;
    SwipeRefreshLayout swipeLayout;
    BaseAdapter listContentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//-------------------------------------------------------------------------------------------
        sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        myEditor = sharedPref.edit();
        list = findViewById(R.id.list);
        swipeLayout = findViewById(R.id.swiperefresh);


        myEditor.putString(DATAS, getString(R.string.large_text));
        myEditor.apply();

        prepareContent();

        listContentAdapter = createAdapter(simpleAdapterContent);
        list.setAdapter(listContentAdapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdapterContent.clear();
                prepareContent();
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                alist.add(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });

    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(ListViewActivity.this, values, R.layout.list_layout, new String[]{KEY1, KEY2}, new int[]{R.id.TextOne, R.id.TextTwo});
    }

    @NonNull
    private void prepareContent() {
        String[] arrayContent = sharedPref.getString(DATAS, "").split("\n\n");
        for (int i = 0; i < arrayContent.length; i++) {
            Map<String, String> temp = new HashMap<>();
            temp.put(KEY1, arrayContent[i]);
            temp.put(KEY2, String.valueOf(arrayContent[i].length()));
            simpleAdapterContent.add(temp);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList("one", alist);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        alist = savedInstanceState.getIntegerArrayList("one");
        for (int i = 0; i < simpleAdapterContent.size(); i++) {
            for (int j = 0; j < alist.size(); j++) {
                if (i == alist.get(j)) {
                    simpleAdapterContent.remove(i);
                }
            }
        }

        super.onRestoreInstanceState(savedInstanceState);

    }
}
