package com.sjtu.se2017.positivetime.view.activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.sjtu.se2017.positivetime.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final static String DES = "DES";
    private final static String KEY = "12345678";
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

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
            stmp=(java.lang.Integer.toHexString(b[n]&0XFF));
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
                            String encryptEmail = encrypt(email,KEY);
                            String encryptPassword = encrypt(password,KEY);
                            String urlStr = "http://10.0.2.2:8080/login?username="+encryptEmail+"&password="+encryptPassword;
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
                                    Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();    //显示toast信息
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
