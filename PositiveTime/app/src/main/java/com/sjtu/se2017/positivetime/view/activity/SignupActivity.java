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
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.sjtu.se2017.positivetime.R;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private final static String DES = "DES";
    private final static String KEY = "12345678";

    @Bind(R.id.input_email) EditText _emailText;

    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        // 执行加密操作
        return cipher.doFinal(src);
    }

    public final static String encrypt(String password, String key) {

        try {
            return byte2String(encrypt(password.getBytes(), key.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2String(byte[] b) {
        String hs="";
        String stmp="";
        for(int n=0;n<b.length;n++){
            stmp=(Integer.toHexString(b[n]&0XFF));
            if(stmp.length() == 1)
                hs+=hs+"0"+stmp;
            else
                hs=hs+stmp;
        }
        return hs.toUpperCase();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection urlCon = null;
                        URL url ;
                        try{
                            String email = _emailText.getText().toString();
                            String password = _passwordText.getText().toString();
                            String encryptEmail = encrypt(email,KEY);
                            String encryptPassword = encrypt(password,KEY);
                            String urlStr = "http://10.200.4.206:8080/user/insert_user?email="+encryptEmail+"&password="+encryptPassword;
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
                                }else{
                                    // exsiting username
                                    Toast.makeText(getApplicationContext(), "exsiting username", Toast.LENGTH_SHORT).show();    //显示toast信息
                                }
                            }else{
                                //http connection failure
                                Toast.makeText(getApplicationContext(), "http connection failure", Toast.LENGTH_SHORT).show();    //显示toast信息
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

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}