package id.developer.tanitionary.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.tanitionary.ActivityOtherAccount;
import id.developer.tanitionary.R;

/**
 * Created by Naufal on 8/13/2016.
 */
public class FragmentOthers extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_other, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listView = (ListView)view.findViewById(R.id.listview_fragment_main_account);
        listView.setAdapter(new ListAdapter(getData()));

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ObjectAccount obj = (ObjectAccount)listView.getAdapter().getItem(position);

                        if(obj.getName().equalsIgnoreCase(getString(R.string.frag_others_menu_account))){
                            getContext().startActivity(new Intent(getContext(), ActivityOtherAccount.class));
                        }else if(obj.getName().equalsIgnoreCase(getString(R.string.frag_others_menu_security))){

                        }else if(obj.getName().equalsIgnoreCase(getString(R.string.frag_others_menu_setting))){

                        }else if(obj.getName().equalsIgnoreCase(getString(R.string.frag_others_menu_about))){

                        }else if(obj.getName().equalsIgnoreCase(getString(R.string.frag_others_menu_help))){

                        }
                    }
                }
        );
    }

    private List<ObjectAccount> getData(){
        List<ObjectAccount> list = new ArrayList<>();

        list.add(new ObjectAccount(R.mipmap.ic_account, getString(R.string.frag_others_menu_account)));
        list.add(new ObjectAccount(R.mipmap.ic_security, getString(R.string.frag_others_menu_security)));
        list.add(new ObjectAccount(R.mipmap.ic_setting, getString(R.string.frag_others_menu_setting)));
        list.add(new ObjectAccount(R.mipmap.ic_launcher, getString(R.string.frag_others_menu_help)));
        list.add(new ObjectAccount(R.mipmap.ic_launcher, getString(R.string.frag_others_menu_about)));

        return list;
    }

    private class ObjectAccount{
        int idIcon;
        String name;

        ObjectAccount(int idIcon, String name){
            this.idIcon = idIcon;
            this.name = name;
        }

        public int getIdIcon() {
            return idIcon;
        }

        public String getName() {
            return name;
        }
    }

    private class ListAdapter extends BaseAdapter{

        Context context;
        List<ObjectAccount> objectAccounts;

        ListAdapter(List<ObjectAccount> objectAccounts){
            this.context = getContext();
            this.objectAccounts = objectAccounts;
        }

        @Override
        public int getCount() {
            return objectAccounts.size();
        }

        @Override
        public Object getItem(int position) {
            return objectAccounts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return objectAccounts.indexOf(objectAccounts.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_fragment_main_account, parent, false);
            }

            ((ImageView)convertView.findViewById(R.id.image_list_fragment_main_account)).setImageResource(objectAccounts.get(position).getIdIcon());
            ((TextView)convertView.findViewById(R.id.text_list_fragment_main_account)).setText(objectAccounts.get(position).getName());

            return convertView;
        }
    }
}
