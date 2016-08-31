package id.developer.tanitionary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rizal Fahmi on 31-Aug-16.
 */
public class FragmentDiseaseList extends Fragment {

    ListView listView;
    ArrayList<ObjectDisease> data;
    String[] values = new String[] { "Android List View",
            "Adapter implementation",
            "Simple List View In Android",
            "Create List View Android",
            "Android Example",
            "List View Source Code",
            "List View Array Adapter",
            "Android Example List View"
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kamus_disease_list,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.listViewKamus);

        final Context context = ((ActivityKamus)getActivity()).getApplicationContext();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPagerKamus viewPagerKamus = ((ActivityKamus)getActivity()).viewPagerKamus;
                viewPagerKamus.setCurrentItem(1);
                FragmentDiseaseDescription fragment = (FragmentDiseaseDescription) ((FragmentKamusAdapter)viewPagerKamus.getAdapter()).getFragment(1);
                fragment.onFragmentSelected((String)listView.getItemAtPosition(position));
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,android.R.id.text1,values);
        listView.setAdapter(arrayAdapter);
        //ArrayAdapter<ArrayList<ObjectDisease>> arrayListArrayAdapter = new ArrayAdapter<ArrayList<ObjectDisease>>(this,android.R.layout.simple_expandable_list_item_1);

    }

    @Override
    public void onResume() {
        Context context = ((ActivityKamus)getActivity()).getApplicationContext();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewPagerKamus viewPagerKamus = ((ActivityKamus)getActivity()).viewPagerKamus;
                viewPagerKamus.setCurrentItem(1);
                FragmentDiseaseDescription fragment = (FragmentDiseaseDescription) ((FragmentKamusAdapter)viewPagerKamus.getAdapter()).getFragment(1);
                fragment.onFragmentSelected((String)listView.getItemAtPosition(position));
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,values);
        listView.setAdapter(arrayAdapter);
        super.onResume();
    }
}
