package cojocaru.alin.juniochallange;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    private static Toolbar mToolBar;
    private static TextView mName;
    private static TextView mProtein;
    private static TextView mSugars;
    private static TextView mCalories;
    private static TextView mBrand;
    private Field field;

    public DetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public DetailsFragment(Field field){
        this.field = field;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        mToolBar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);
        ((RecyclerView) getActivity().findViewById(R.id.main_list)).setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            field = (Field) savedInstanceState.getSerializable("MY_FIELD");
        }
        mToolBar.setTitle(field.getName() + " details");
        mName = (TextView) v.findViewById(R.id.text_name);
        mProtein = (TextView) v.findViewById(R.id.text_protein);
        mSugars = (TextView) v.findViewById(R.id.text_sugars);
        mCalories = (TextView) v.findViewById(R.id.text_calories);
        mBrand = (TextView) v.findViewById(R.id.text_brand);
        populate(field);
        return v;
    }

    public void populate(Field field){
        mName.setText(field.getName()+"");
        mProtein.setText(field.getProteins()+"");
        mSugars.setText(field.getSurgars()+"");
        mCalories.setText(field.getCalories()+"");
        mBrand.setText(field.getBrand() + "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MY_FIELD", field);
    }
}
