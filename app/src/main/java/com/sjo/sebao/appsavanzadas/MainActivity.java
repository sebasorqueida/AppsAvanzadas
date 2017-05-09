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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        getFbKeyHash("wpdHw7YvNMZjx52OudAvuijMWzU=");

        setContentView(R.layout.activity_main);

        //Link banner with class
        adView = (AdView)findViewById(R.id.add_view);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        //Link facebook Login Button with class
        loginButton=(LoginButton)findViewById(R.id.login_facebook);

        //facebook login
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

    //banner method on Destroy, on Pause and on Resume
    @Override
    protected void onDestroy() {
        if(adView!=null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(adView!=null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(adView!=null){
            adView.resume();
        }
        super.onResume();
    }

    //Method for KeyHash validation
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

    //Send session login results al LoginManager throw CallBackManager
    protected void onActivityResult(int reqCode, int resCode, Intent i){
        callbackManager.onActivityResult(reqCode, resCode, i);
    }
}
