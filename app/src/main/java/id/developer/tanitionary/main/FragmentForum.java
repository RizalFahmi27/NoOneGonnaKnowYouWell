package id.developer.tanitionary.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.developer.tanitionary.ObjectDiscussionLoaded;
import id.developer.tanitionary.R;
import id.developer.tanitionary.main.forum.RecyclerAdapterForum;

/**
 * Created by Naufal on 8/13/2016.
 */
public class FragmentForum extends Fragment {

    private static final String ROOT_URL = "http://cobacobaaja12345689.890m.com/";
    private static final String TAG = "Fragment Home";

    List<ObjectDiscussionLoaded> list;
    Handler handler;
    RecyclerAdapterForum mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_main_forum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_fragment_main_forum);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        ((Toolbar)view.findViewById(R.id.toolbar)).setTitle(getResources().getString(R.string.title_ask_farmer));

        mAdapter = new RecyclerAdapterForum(getContext(), list, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }
}
