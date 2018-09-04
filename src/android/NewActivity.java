package id.code.avrist.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import id.code.avrist.authenticationmodule.AvristAuth;
import id.code.avrist.authenticationmodule.api.AvristApiRequest;
import id.code.avrist.authenticationmodule.callback.OnChangePasswordResult;
import id.code.avrist.authenticationmodule.callback.OnGetProfileResult;
import id.code.avrist.authenticationmodule.callback.OnLoginResult;
import id.code.avrist.authenticationmodule.callback.OnLogoutResult;
import id.code.avrist.authenticationmodule.model.UserProfile;
import id.code.avrist.authenticationmodule.model.UserProfileAdditionalField;

public class NewActivity extends Activity implements AvristApiRequest.DestroyableView{
  private String clientId;
  private String language;
  private Globals g;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    g = Globals.getInstance();
    String package_name = getApplication().getPackageName();
    setContentView(getApplication().getResources().getIdentifier("activity_new", "layout", package_name));

    AvristAuth.getInstance().setOnLoginResult(new OnLoginResult() {
      @Override
      public void onLoginResult(UserProfile userProfile) {
        Gson gson = new Gson();
        String profile = gson.toJson(userProfile);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ProfileUser", profile);
        editor.apply();

        g.setData(userProfile);
        setResult(10002);
      }
    });

    AvristAuth.getInstance().setOnChangePasswordResult(new OnChangePasswordResult() {
      @Override
      public void onChangePasswordResult() {
        g.setData("Success");
        setResult(10004);
      }
    });

    String command = getIntent().getExtras().getString("command");
    if(command.equals("login")) login();
    else if(command.equals("change")) changePassword();
    else if(command.equals("profile")) getProfile();
    else if(command.equals("logout")) logout();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    AvristAuth.getInstance().loginResult(requestCode, resultCode, data);
    AvristAuth.getInstance().changePasswordResult(requestCode, resultCode, data);
    finish();
  }

  public void login(){
    clientId = getIntent().getExtras().getString("clientId");
    language = getIntent().getExtras().getString("language");
    AvristAuth.getInstance().login(this, clientId, language);
  }

  protected void changePassword(){
    clientId = getIntent().getExtras().getString("clientId");
    language = getIntent().getExtras().getString("language");
    AvristAuth.getInstance().changePassword(this, clientId, language);
  }

  protected void getProfile(){
//    clientId = getIntent().getExtras().getString("clientId");
//    language = getIntent().getExtras().getString("language");
//    AvristAuth.getInstance().getProfile(NewActivity.this, clientId, language, new OnGetProfileResult() {
//      @Override
//      public void onGetProfileResult(UserProfile userProfile) {
//        g.setData(userProfile);
//        setResult(10002);
//      }
//      @Override
//      public void onGetProfileError(String errorMessage) {
//        g.setData(errorMessage);
//        setResult(10000);
//      }
//    });

    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    g.setData(preferences.getString("ProfileUser", ""));
    setResult(100021);

    finish();
  }

  protected void logout(){
    clientId = getIntent().getExtras().getString("clientId");
    language = getIntent().getExtras().getString("language");
    AvristAuth.getInstance().logout(this, clientId, language, new OnLogoutResult() {
      @Override
      public void onLogoutResult() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("ProfileUser");
        editor.apply();

        g.setData("Success");
        setResult(10004);
      }

      @Override
      public void onLogoutError(String errorMessage) {
        g.setData(errorMessage);
        setResult(10000);
      }
    });
    finish();
  }

  @Override
  public Context getContext() {
    return this;
  }
}
