package org.kathmandulivinglabs.exploreindore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiHelper;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiInterface;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AuthenticateModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText mail, pass;
    private Button login;
    private ProgressDialog progressBar;
    SharedPreferences mSharedPref;
    public static final String TOKEN = "token";
    public static final String AUTHENTICATED = "authenticated";
    public static final String AUTHEMAIL = "authemail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mSharedPref = getSharedPreferences(getString(R.string.prefrence_file_key), Context.MODE_PRIVATE);
        mail = findViewById(R.id.edittext_username);
        pass = findViewById(R.id.edittext_password);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calllogin();
            }
        });
        setTitle("Log in");
        loginDialog();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void showInfoSnackbar(String msg) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void calllogin(){
        String email = mail.getText().toString();
        String password = pass.getText().toString();
        if(email.isEmpty()){
            showInfoSnackbar("Please enter the Username");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showInfoSnackbar("Please enter the registered email");
        }
        else if(password.isEmpty()){
            showInfoSnackbar("Please enter the Password");
        }
        else {
            getToken(email, password);
        }

    }
    public void loginDialog(){
        progressBar = new ProgressDialog(LoginActivity.this);
        progressBar.setTitle("Logging In");
        progressBar.setMessage("Please Wait");
    }
    private void getToken(final String email, final String password){
        progressBar.show();
        ApiInterface api = new ApiHelper().getApiInterface();
        LoginModel lm = new LoginModel();
        lm.setEmail(email);
        lm.setPassword(password);
        Call<AuthenticateModel> call = api.getAuthenticateResponse(lm);
        call.enqueue(new Callback<AuthenticateModel>() {
            @Override
            public void onResponse(Call<AuthenticateModel> call, Response<AuthenticateModel> response) {
                if(call.isExecuted()) progressBar.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getSuccess()==1) {
                        if(response.body().getToken()!=null) {
                            showInfoSnackbar(response.body().getMessage());
                            SharedPreferences.Editor memory = mSharedPref.edit();
                            memory.putString(TOKEN,response.body().getToken());
                            memory.putBoolean(AUTHENTICATED,true);
                            memory.putString(AUTHEMAIL,email);
                            memory.apply();
                        }
                    }
                    else showInfoSnackbar(response.body().getMessage());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        showInfoSnackbar(jObjError.getString("message"));
                    } catch (Exception e) {
                        showInfoSnackbar(e.getMessage());
                    }

                }

            }
            @Override
            public void onFailure(Call<AuthenticateModel> call, Throwable t) {
                progressBar.dismiss();
                showInfoSnackbar("You are not connected to the internet. Please check your connection");
            }
        });
    }

}
