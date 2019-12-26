package org.kathmandulivinglabs.exploreindore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.kathmandulivinglabs.exploreindore.Api_helper.ApiHelper;
import org.kathmandulivinglabs.exploreindore.Api_helper.ApiInterface;
import org.kathmandulivinglabs.exploreindore.R;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.AuthenticateModel;
import org.kathmandulivinglabs.exploreindore.RetrofitPOJOs.LoginModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText prePassword, newPassword;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        prePassword = findViewById(R.id.edittext_username);
        prePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        prePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        prePassword.setHint("Old Password");
        newPassword = findViewById(R.id.edittext_password);
        newPassword.setHint("New Password");
        Button changeBtn = findViewById(R.id.btn_login);
        changeBtn.setText(R.string.apply);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkandcall();
            }
        });
        setTitle("Password Reset");
        loginDialog();
    }

    public void loginDialog(){
        progressBar = new ProgressDialog(ResetPasswordActivity.this);
        progressBar.setTitle("Logging In");
        progressBar.setMessage("Please Wait");
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
    private void checkandcall(){
        String oldp = prePassword.getText().toString();
        String newp = newPassword.getText().toString();

        if(oldp.isEmpty() || newp.isEmpty()){
            showInfoSnackbar("I don't think password field should be empty, but that's just me." + ("\ud83d\ude01"));
        }
        else if(oldp.equals(newp)){
            showInfoSnackbar("I remember that one, mix it up!");
        }
        else {
            progressBar.show();
            requestPassword(oldp, newp);
        }
    }
    private void requestPassword(String oldp, String newp){
        ApiInterface api = new ApiHelper().getApiInterface();
        LoginModel params = new LoginModel();
        params.setNewpassword(newp);
        params.setOldpassword(oldp);
        String token = MainActivity.mSharedPref.getString(LoginActivity.TOKEN, null);
        Call<AuthenticateModel> call = api.getResetResponse("Bearer " + token,params);
        call.enqueue(new Callback<AuthenticateModel>() {
            @Override
            public void onResponse(Call<AuthenticateModel> call, Response<AuthenticateModel> response) {
                if(response.isSuccessful() && response.body()!=null){
                    progressBar.dismiss();
                    if(response.body().getSuccess()==1){
                        Toast.makeText(getApplicationContext(),"Password reset successful. You need to login with new passowrd to continue.",Toast.LENGTH_LONG).show();
                        Intent intentabout = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentabout);
                    }
                    else showInfoSnackbar(response.body().getMessage());
                }
                else {
                    progressBar.dismiss();
                    showInfoSnackbar("Cannot connect to server. Please try again later.");
                }
                }

            @Override
            public void onFailure(Call<AuthenticateModel> call, Throwable t) {
                progressBar.dismiss();
                showInfoSnackbar("Cannot connect to the server. Please check your connection and try again");
            }
        });
    }
}
