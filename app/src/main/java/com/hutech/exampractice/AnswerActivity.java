package com.hutech.exampractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.hutech.exampractice.Adapter.AnswerAdapter;

public class AnswerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView answersView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


       Toolbar toolbar = (Toolbar) findViewById(R.id.aa_toolbar);
       answersView = findViewById(R.id.aa_recycler_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ANSWERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);

        AnswerAdapter adapter = new AnswerAdapter(DbQuery.g_quesList);
        answersView.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            AnswerActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}