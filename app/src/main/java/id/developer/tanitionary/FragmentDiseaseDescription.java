package id.developer.tanitionary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rizal Fahmi on 31-Aug-16.
 */
public class FragmentDiseaseDescription extends Fragment {

    TextView diseaseName, symptoms, commodity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kamus_description,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        diseaseName = (TextView) view.findViewById(R.id.diseaseNameText);
        symptoms = (TextView) view.findViewById(R.id.symptomsListText);
        commodity = (TextView) view.findViewById(R.id.CommodityListText);


    }



    public void onFragmentSelected(String postion){
        diseaseName.setText(""+postion);

    }
}
