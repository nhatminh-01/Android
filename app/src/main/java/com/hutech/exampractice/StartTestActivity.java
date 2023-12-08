package com.hutech.exampractice;

import static com.hutech.exampractice.DbQuery.g_catList;
import static com.hutech.exampractice.DbQuery.loadquestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore, time;
    private Button startTestB;
    private ImageView backB;
    private Dialog progressDialog;  // Di chuyển vào main
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init(); // khởi tạo

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading");

        progressDialog.show();

        loadquestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Something went wrong ! Please try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init()
    {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this, QuestionsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setData()
    {
        catName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName()); // Lấy tên danh mục
        testNo.setText("Test No. "+ String.valueOf(DbQuery.g_selected_test_index + 1)); // Lấy ra tên phần
        totalQ.setText(String.valueOf(DbQuery.g_quesList.size())); // Lấy ra tên phần
        bestScore.setText((String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()))); // lấy mức điểm tối đa
        time.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime())); // lấy ra mức thời gian

    }

}