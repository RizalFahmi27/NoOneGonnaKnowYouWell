package id.developer.tanitionary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Naufal on 8/17/2016.
 */
public class SessionLoginManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Tanitionary";
    private static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String USER_POSITION = "position";

    public SessionLoginManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email, String position){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(USER_POSITION, position);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(USER_POSITION, pref.getString(USER_POSITION, null));

        return user;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void userLogout(){
        editor.clear();
        editor.commit();
    }
}
