package cojocaru.alin.juniochallange;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment implements SearchListAdapter.RecyclerViewClickListener{

    private static LinearLayout mRoot;
    private static Toolbar mToolBar;
    private static EditText mEditText;
    private static Button mButton;
    private static ProgressDialog progressDialog;
    private ArrayList<Field> items = new ArrayList<>();
    private static RecyclerView mRecyclerView;
    private SearchListAdapter mSearchAdapter;
    private Field selectedField;
    private DatabaseHandler mDataBase;
    private View.OnClickListener mSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mToolBar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);
        ((RecyclerView) getActivity().findViewById(R.id.main_list)).setVisibility(View.GONE);
        mToolBar.setTitle("Add new item");
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEditText = (EditText) v.findViewById(R.id.search_input);
        mButton = (Button) v.findViewById(R.id.go_button);
        mRoot = (LinearLayout) v.findViewById(R.id.root);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected(getActivity())) {
                    submit();
                    hideKeyboard();
                } else {
                    Snackbar.make(mRoot, "Please connect to internet first", Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.text_connect), mSnackBarClickListener)
                            .show();
                    hideKeyboard();
                }
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        if (savedInstanceState != null) {
            items = (ArrayList<Field>) savedInstanceState.getSerializable("MY_ITEMS");
        }
        mRecyclerView = (RecyclerView) v.findViewById(R.id.search_list);
        mSearchAdapter = new SearchListAdapter(getActivity(), items, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSearchAdapter);
        mDataBase = new DatabaseHandler(getActivity());
        return v;
    }


    public void submit() {
        try {
            String searchElement = mEditText.getText().toString();
            String url = "https://api.nutritionix.com/v1_1/search/" + searchElement + "?results=0:30&fields=item_name,brand_name&appId=085c05f2&appKey=22b33fd7d383b18dec6b04e704bb2ae0";
            new Search().execute(url,"search");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        String id = items.get(position).getId();
        String url = "https://api.nutritionix.com/v1_1/item?id=" + id + "&appId=085c05f2&appKey=22b33fd7d383b18dec6b04e704bb2ae0";
        selectedField = items.get(position);
        new Search().execute(url, "details");
    }

    private class Search extends AsyncTask<String, Void, JSONObject> {

        private OkHttpClient mClient;


        public Search() {
            mClient = new OkHttpClient();

        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Request request = new Request.Builder()
                        .url(params[0])
                        .build();

                Response response = mClient.newCall(request).execute();
                JSONObject mJson = new JSONObject(response.body().string());
                mJson.put("type",params[1]);
                return mJson;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if(response.getString("type").equals("search")){
                    JSONArray mJsnArr = response.getJSONArray("hits");
                    items.clear();
                    for (int i = 0; i < mJsnArr.length(); i++) {
                        items.add(new Field(mJsnArr.getJSONObject(i).getString("_id"))
                                .fromJSON(mJsnArr.getJSONObject(i).getJSONObject("fields")));
                    }
                    mSearchAdapter.notifyDataSetChanged();
                } else {
                    Field mField = selectedField.fromDetailsJSON(response);
                    if(mDataBase.itemExist(mField)==false) {
                        mDataBase.addField(mField);
                        getActivity().onBackPressed();
                    }else{
                        Snackbar.make(mRoot, "Item already exists in your list", Snackbar.LENGTH_SHORT)
                                .show();
                        hideKeyboard();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("MY_ITEMS", items);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return true;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        if (nwInfo != null && nwInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
