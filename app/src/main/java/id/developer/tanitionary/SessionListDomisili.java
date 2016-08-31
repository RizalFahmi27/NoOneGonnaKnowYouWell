package id.developer.tanitionary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Naufal on 8/28/2016.
 */
public class SessionListDomisili {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Tanitionary";
    private static final String KEY_LIST_DOMISILI = "list_domisili";
    private static final String IS_DOMISILI_LOADED = "list_domisili_loaded";

    public SessionListDomisili(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setListForum(List<ObjectDomisili> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_LIST_DOMISILI, json);
        editor.putBoolean(IS_DOMISILI_LOADED, true);
        editor.commit();
    }

    public List<ObjectDomisili> getListForum() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_LIST_DOMISILI, null);
        Type type = new TypeToken<List<ObjectDomisili>>(){}.getType();
        List<ObjectDomisili> list= gson.fromJson(json, type);

        return list;
    }

    public boolean isDomisiliLoaded(){
        return pref.getBoolean(IS_DOMISILI_LOADED, false);
    }
}
