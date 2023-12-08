package com.hutech.exampractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

// test git
public class SplashActivity extends AppCompatActivity {

    private TextView appName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.app_name);

        // Set font chữ
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);

        //Tạo animation cho loader
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appName.setAnimation(animation);

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo

        // Khởi tạo để truy vấn vào cơ sở dữ liệu firebase
        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000); // Đợi 3 giây để load
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Nêu hệ thống đã đăng nhập trước đó thì vào main chính không thì bắt đăng nhập lại
                if(mAuth.getCurrentUser() != null)
                {
                    // load dữ liệu data
                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {

                            Toast.makeText(SplashActivity.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                        }
                    });



                }
                else{
                    // Di chuyển vào main
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }.start();
    }
}