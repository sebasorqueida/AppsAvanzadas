package com.sjo.sebao.appsavanzadas;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        getFbKeyHash("1505686659503060");

        setContentView(R.layout.activity_main);

        loginButton=(LoginButton)findViewById(R.id.login_facebook);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

                Toast.makeText(MainActivity.this, "Inicio de sesión cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(MainActivity.this, "Inicio de sesión no exitoso", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFbKeyHash(String packageName) {

        try{
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest messageDigest  = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
                System.out.println("KeyHash :"+ Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        }catch(PackageManager.NameNotFoundException e){

        }catch(NoSuchAlgorithmException e){

        }
    }
    protected void onActivityResult(int reqCode, int resCode, Intent i){
        callbackManager.onActivityResult(reqCode, resCode, i);
    }
}
