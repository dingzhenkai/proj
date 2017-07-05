package com.sjtu.se2017.positivetime.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.sjtu.se2017.positivetime.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection urlCon = null;
                        URL url ;
                        try{
                            String email = _emailText.getText().toString();
                            String password = _passwordText.getText().toString();
                            String urlStr = "http://192.168.1.198:8080/login?username="+email+"&password="+password;
                            url = new URL(urlStr);
                            urlCon= (HttpURLConnection) url.openConnection();
                            urlCon.setRequestMethod("GET");
                            urlCon.connect();
                            if(urlCon.getResponseCode()== HttpURLConnection.HTTP_OK){
                                InputStream in = urlCon.getInputStream();
                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                String line = null;
                                StringBuffer buffer = new StringBuffer();
                                while((line=br.readLine())!=null){
                                    buffer.append(line);
                                }
                                in.close();
                                br.close();
                                String result = buffer.toString();
                                if(result.equals("1")){
                                    //success,add some code here to jump to somewhere else
                                    finish();
                                }else{
                                    // wrong password
                                    System.out.println("wrong password");
                                }
                            }else{
                                //http connection failure
                                System.out.println("http connection failure");
                            }
                        }catch (IOException e){
                            //illegal url form
                            e.printStackTrace();
                        }finally {
                            urlCon.disconnect();
                        }
                    }
                }).start();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        // TODO: Implement your own authentication logic here.

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
