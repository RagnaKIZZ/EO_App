package com.hard.eoapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hard.eoapp.MainActivity;
import com.hard.eoapp.R;
import com.hard.eoapp.login.loginmodel.LoginModel;
import com.hard.eoapp.register.RegisterActivity;
import com.hard.eoapp.utils.HelperClass;
import com.hard.eoapp.utils.SessionPrefs;
import com.hard.eoapp.utils.UrlServer;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextInputLayout edtEmail, edtPassword;
    private TextInputEditText txtDone;
    private Button btnLogin;
    private TextView txtToRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findView();
    }

    private void findView() {
        edtEmail = findViewById(R.id.txt_email_login);
        edtPassword = findViewById(R.id.txt_password_login);
        btnLogin = findViewById(R.id.btn_login);
        txtToRegist = findViewById(R.id.txt_register);
        txtDone = findViewById(R.id.edt_pswd_login);

        txtToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtDone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });

       HelperClass.turnOffError(edtEmail);
       HelperClass.turnOffError(edtPassword);
    }

    private void login() {
        String email = edtEmail.getEditText().getText().toString().trim().toLowerCase();
        String password = edtPassword.getEditText().getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginUser(email, password);
        }

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.cant_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.valid_email));
        } else {
            edtEmail.setErrorEnabled(false);
            edtEmail.setError(null);
        }

        if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.cant_empty));
        } else {
            edtPassword.setErrorEnabled(false);
            edtPassword.setError(null);
        }
    }

    private void loginUser(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        HelperClass.loading(progressDialog, null, null, false);
        AndroidNetworking.post(UrlServer.URL_LOGIN)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .build()
                .getAsOkHttpResponseAndObject(LoginModel.class, new OkHttpResponseAndParsedRequestListener<LoginModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, LoginModel response) {
                        progressDialog.dismiss();

                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Prefs.putString(SessionPrefs.U_ID, response.getData().getUserId());
                                Prefs.putString(SessionPrefs.NAMA, response.getData().getNama());
                                Prefs.putString(SessionPrefs.EMAIL, response.getData().getEmail());
                                Prefs.putString(SessionPrefs.TELEPON, response.getData().getTelepon());
                                Prefs.putString(SessionPrefs.FOTO, response.getData().getFoto());
                                Prefs.putString(SessionPrefs.TOKEN_LOGIN, response.getData().getTokenLogin());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toast.makeText(LoginActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}