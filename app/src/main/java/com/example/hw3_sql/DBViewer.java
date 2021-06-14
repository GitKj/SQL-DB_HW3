package com.example.hw3_sql;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBViewer extends AppCompatActivity {

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b_viewer);

        backButton = findViewById(R.id.btn_back);

        MyContentProvider.MainDatabaseHelper mainDatabaseHelper = new MyContentProvider.MainDatabaseHelper(this);
        ArrayList<Tickers> tickers = mainDatabaseHelper.getData();
        ListView lv = (ListView) findViewById(R.id.list_tickers);

        TickersAdapter adapter = new TickersAdapter(tickers, this);
        lv.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}