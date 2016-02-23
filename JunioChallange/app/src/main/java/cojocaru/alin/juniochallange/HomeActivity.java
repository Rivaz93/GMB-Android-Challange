package cojocaru.alin.juniochallange;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cojocaru.alin.juniochallange.MainListAdapter.RecyclerViewClickListener;

public class HomeActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private static Toolbar mToolBar;
    private static FloatingActionButton mFAB;
    private static FragmentTransaction fragmentTransaction;
    private static Fragment mSearchFragment;
    private static RecyclerView mRecyclerView;
    private static  MainListAdapter mListAdapter;
    private DatabaseHandler mDataBase;
    private List<Field> items;
    private static DetailsFragment mDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();


    }

    private void initComponents(){
        mToolBar = (Toolbar) findViewById(R.id.app_bar);
        mToolBar.setTitle("Welcome to Android Chalange");
        setSupportActionBar(mToolBar);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(mFABClickListener);
        mDataBase = new DatabaseHandler(this);
        items = mDataBase.getAllField();
        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);
        mListAdapter = new MainListAdapter(this, items, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void reCreate(){
        mToolBar.setTitle("Welcome to Android Chalange");
        mFAB.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        items = mDataBase.getAllField();
        mListAdapter = new MainListAdapter(this, items, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter);
    }

    private View.OnClickListener mFABClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            mSearchFragment = new SearchFragment();
            fragmentTransaction.replace(R.id.container, mSearchFragment).addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        reCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getSupportFragmentManager().popBackStack();
            reCreate();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mDetailsFragment = new DetailsFragment(items.get(position));
        fragmentTransaction.replace(R.id.container, mDetailsFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
