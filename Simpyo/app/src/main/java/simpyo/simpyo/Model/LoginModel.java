package simpyo.simpyo.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import simpyo.simpyo.Activity.LoginActivity;
import simpyo.simpyo.HttpRequest.PostHttp;
import simpyo.simpyo.Setting.ServerURL;
import simpyo.simpyo.Setting.Setting;


public class LoginModel {
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LoginModel(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences("pref", 0);
        editor = sharedPreferences.edit();
    }

    // 아이디 가져오기
    public static String getId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        return sharedPreferences.getString("UserId", "");
    }

    // admin 얻기
    public static int getAdmin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        return sharedPreferences.getInt("admin", 0);
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        return sharedPreferences.getString("Password", "");
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref", 0);
        return sharedPreferences.getString("Name", "");
    }

    public static String checkAdmin(Context context) throws ExecutionException, InterruptedException {

        Setting.postParameter = new PostHttp().execute(ServerURL.ADMIN_CHECK, LoginModel.getId(context), LoginModel.getPassword(context)).get();

        Log.d(Setting.Tag, "관리자 체크 : " + Setting.postParameter);
        return Setting.postParameter;
    }

    public static void logout(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
        saveAutoLogin(activity, false);

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /* 자동로그인을 하는지 가져와주는 메쏘드 */
    public boolean getAutoLogin() {
        return sharedPreferences.getBoolean("login", false);
    }

    /* 로그인 할때 자동로그인하는지 안하는지 저장! */
    public void saveAutoLogin(boolean autoLogin) {
        editor.putBoolean("login", autoLogin);
        editor.commit();
    }

    public static void saveAutoLogin(Context context,boolean autoLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", autoLogin);
        editor.commit();
    }

    // 로그인
    public void login(String inputEmail, String inputPw) throws ExecutionException, InterruptedException {
        Log.d(Setting.Tag, "로그인 시도");

        Setting.postParameter = new PostHttp().execute(ServerURL.LOGIN, inputEmail, inputPw).get();
        Log.d(Setting.Tag, "응답 : " + Setting.postParameter);
    }

    // 회원가입
    public void register(String inputName, String inputPw, String inputEmail, String birthday) throws ExecutionException, InterruptedException {
        Log.d(Setting.Tag, "회원가입 시도");

        Setting.postParameter = new PostHttp().execute(ServerURL.REGISTER, inputName, inputEmail, inputPw, birthday).get();
    }

    // 아이디 저장
    public void saveId(String id) {
        Log.d(Setting.Tag, "ID 저장 : " + id);

        editor.putString("UserId", id);
        editor.commit();
    }

    // 아이디 가져오기
    public String getId() {
        return sharedPreferences.getString("UserId", "");
    }

    // admin 저장
    public void saveAdmin(int admin) {
        Log.d(Setting.Tag, "Admin : " + admin);

        editor.putInt("admin", admin);
        editor.apply();
    }

    public void savePassword(String inputPw) {
        Log.d(Setting.Tag, "Password 저장 : " + inputPw);

        editor.putString("Password", inputPw);
        editor.apply();
    }

    public void saveName(String name) {
        Log.d(Setting.Tag, "Name 저장 : " + name);

        editor.putString("Name", name);
        editor.apply();
    }


}
