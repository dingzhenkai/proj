package com.sjtu.se2017.positivetime.view.activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.sjtu.se2017.positivetime.R;
import com.sjtu.se2017.positivetime.model.application.ATapplicaion;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String KEY_SHA = "SHA";
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    public static String encryptSHA(String data) throws Exception {
        byte[] input = data.getBytes();
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(input);

        return sha.digest().toString();

    }
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
                            String encryptPassword = encryptSHA(password);
                            String urlStr = "http://10.200.4.206:8080/user/login?email="+email+"&password="+password;
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
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();    //显示toast信息
                                    ATapplicaion.getInstance().setEmail(email);
                                    finish();
                                    Looper.loop();
                                }else{
                                    // wrong password
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();    //显示toast信息
                                    Looper.loop();
                                }
                            }else{
                                //http connection failure
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "http connection failure", Toast.LENGTH_SHORT).show();    //显示toast信息
                                Looper.loop();
                            }
                        }catch (Exception e){
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
/*
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

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
