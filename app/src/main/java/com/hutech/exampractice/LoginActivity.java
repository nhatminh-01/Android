package com.hutech.exampractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText email, pass;
    private Button loginB;
    private TextView forgotPassB, signupB;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView dialogText;
    private RelativeLayout gSignB; // Bấm vào layout để đăng nhập google

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginB = findViewById(R.id.loginB);
        forgotPassB = findViewById(R.id.forgot_password);
        signupB = findViewById(R.id.signupB);
        gSignB = findViewById(R.id.g_SignB);

        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing in...");

        mAuth = FirebaseAuth.getInstance(); // Khởi tạo

        // Cấu hình đăng nhập bằng google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Sự kiện bấm nút đăng nhập
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData())
                {
                    login();
                }
            }
        });

        // Sự kiện bấm chuyển sang giao diện đăng ký
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện đăng nhập bằng google
        gSignB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignIn();
            }
        });
    }

    //  Kiêm tra nhập thông itn
    private boolean validateData(){
        if(email.getText().toString().isEmpty())
        {
            email.setError("Enter E-Mail ID");
            return false;
        }

        if(pass.getText().toString().isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }

        return true;
    }

    // hàm login
    private void login(){
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                            // load data
                            DbQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    // Hàm login với google
    private void googleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Kết quả đăng nhập fireBase goolge
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
               Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // lấy idToken
    private void firebaseAuthWithGoogle(String idToken){
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Google Sign in Success", Toast.LENGTH_SHORT).show();

                    // Người dùng hiện tại
                    FirebaseUser user =mAuth.getCurrentUser();

                    if(task.getResult().getAdditionalUserInfo().isNewUser()){
                        DbQuery.createUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                // load data
                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                    @Override
                                    public void onFailure() {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Something went wrong ! Please try again",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Something went wrong ! Please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        // load Data
                        DbQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Something went wrong ! Please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

        });

    }

}