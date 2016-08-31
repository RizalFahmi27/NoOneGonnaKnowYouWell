package id.developer.tanitionary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Naufal on 8/26/2016.
 */
public class SessionListData {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Tanitionary";
    private static final String KEY_LIST_FORUM = "list_forum";

    public SessionListData(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setListForum(List<ObjectDiscussionLoaded> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_LIST_FORUM, json);
        editor.commit();
    }

    public List<ObjectDiscussionLoaded> getListForum() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_LIST_FORUM, null);
        Type type = new TypeToken<List<ObjectDiscussionLoaded>>(){}.getType();
        List<ObjectDiscussionLoaded> list= gson.fromJson(json, type);

        return list;
    }
}
