package id.code.avrist.plugin;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.code.avrist.authenticationmodule.model.UserProfile;

public class AvristAuth extends CordovaPlugin {
  private CallbackContext PUBLIC_CALLBACKS = null;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Context context = cordova.getActivity().getApplicationContext();
    PUBLIC_CALLBACKS = callbackContext;
    JSONObject json_data = args.getJSONObject(0);
    if(action.equals("login")) {
      this.login(context, json_data.getString("clientId"), json_data.getString("language"));
    }
    else if(action.equals("profile")) {
      this.profile(context, json_data.getString("clientId"), json_data.getString("language"));
    }
    else if(action.equals("change")) {
      this.change(context, json_data.getString("clientId"), json_data.getString("language"));
    }
    else if(action.equals("logout")) {
      this.logout(context, json_data.getString("clientId"), json_data.getString("language"));
    }
    else return false;

    PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
    r.setKeepCallback(true);
    callbackContext.sendPluginResult(r);
    return true;
  }
  
  private void login(Context context, String clientId, String language) {
    Intent intent = new Intent(context, NewActivity.class);
    intent.putExtra("clientId", clientId);
    intent.putExtra("language", language);
    intent.putExtra("command", "login");

    this.cordova.startActivityForResult( this, intent, 10001);
  }

  private void profile(Context context, String clientId, String language) {
    Intent intent = new Intent(context, NewActivity.class);
    intent.putExtra("clientId", clientId);
    intent.putExtra("language", language);
    intent.putExtra("command", "profile");

    this.cordova.startActivityForResult( this, intent, 10001);
  }

  private void change(Context context, String clientId, String language) {
    Intent intent = new Intent(context, NewActivity.class);
    intent.putExtra("clientId", clientId);
    intent.putExtra("language", language);
    intent.putExtra("command", "change");

    this.cordova.startActivityForResult( this, intent, 10003);
  }

  private void logout(Context context, String clientId, String language) {
    Intent intent = new Intent(context, NewActivity.class);
    intent.putExtra("clientId", clientId);
    intent.putExtra("language", language);
    intent.putExtra("command", "logout");

    this.cordova.startActivityForResult( this, intent, 10005);
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Globals g = Globals.getInstance();
    if(resultCode == 10000){
      String errMessage = g.getData().toString();
      tolog(errMessage);
      PluginResult result = new PluginResult(PluginResult.Status.ERROR, errMessage);
      result.setKeepCallback(true);
      PUBLIC_CALLBACKS.sendPluginResult(result);
      return;
    }
    else if(resultCode == 10002){
      UserProfile userProfile = (UserProfile) g.getData();
      tolog("Hai " + userProfile.getName());
      Gson gson = new Gson();
      String message = gson.toJson(userProfile);
      PluginResult result = new PluginResult(PluginResult.Status.OK, message);
      result.setKeepCallback(true);
      PUBLIC_CALLBACKS.sendPluginResult(result);
      return;
    }
    else if(resultCode == 100021){
      String profile = g.getData().toString();
      PluginResult result = new PluginResult(PluginResult.Status.OK, profile);
      result.setKeepCallback(true);
      PUBLIC_CALLBACKS.sendPluginResult(result);
      return;
    }
    else if(resultCode == 10004){
      String message = g.getData().toString();
      tolog(message);
      PluginResult result = new PluginResult(PluginResult.Status.OK, message);
      result.setKeepCallback(true);
      PUBLIC_CALLBACKS.sendPluginResult(result);
      return;
    }
    else if(requestCode == 10005){
      String message = "Success";
      PluginResult result = new PluginResult(PluginResult.Status.OK, message);
      result.setKeepCallback(true);
      PUBLIC_CALLBACKS.sendPluginResult(result);
      return;
    }
  }

  public void tolog(String toLog){
    Context context = cordova.getActivity();
    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, toLog, duration);
    toast.show();
  }

}
