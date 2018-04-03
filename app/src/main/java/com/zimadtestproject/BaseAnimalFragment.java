package com.zimadtestproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zimadtestproject.api.APIClient;
import com.zimadtestproject.api.APIInterface;
import com.zimadtestproject.api.Animal;
import com.zimadtestproject.api.AnimalResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseAnimalFragment extends Fragment {

    public static final String LOG_TAG = "===>" + BaseAnimalFragment.class.getName();
    protected LinearLayoutManager layoutManager;
    protected RecyclerView recyclerView;
    protected ProgressBar progressBar;
    protected RecyclerViewAdapter recyclerViewAdapter;
    protected List<Animal> items = new ArrayList<>();

    protected abstract AnimalType getType();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_animal, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recycle_view_container);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(bundle);
        if (bundle != null) {
            Log.d(LOG_TAG, "onActivityCreated - Restore data from bundle");
            String json = bundle.getString(getType().type());
            List<Animal> restoredData = new Gson().fromJson(json, new TypeToken<List<Animal>>() {
            }.getType());
            items.addAll(restoredData);
            showView();
        } else {
            if (items != null && !items.isEmpty()) {
                Log.d(LOG_TAG, "onActivityCreated - Display view");
                showView();
            } else {
                Log.d(LOG_TAG, "onActivityCreated - Load data fy request");
                loadData();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState - items: " + items);
        super.onSaveInstanceState(outState);
        String json = new Gson().toJson(items);
        outState.putString(getType().type(), json);
    }

    protected void loadData() {
        showProgress(true);
        String animalType = getType().type();
        Log.d(LOG_TAG, "loadData - Animal type: " + animalType);

        APIInterface apiInterface = APIClient.create(APIInterface.class);
        final Call animalCall = apiInterface.getAnimal(animalType);
        animalCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful() && response.body() != null) {
                    AnimalResponse animalResponse = (AnimalResponse) response.body();
                    items = animalResponse.data;
                    for (int i = 0; i < items.size(); i++) {
                        Animal a = items.get(i);
                        a.setId(i + 1);
                    }
                    showView();
                    Log.d(LOG_TAG, "loadData - onResponse: " + items);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(LOG_TAG, "loadData - onFailure");
                showProgress(false);
            }
        });
    }

    protected void showView() {
        showProgress(false);
        recyclerViewAdapter.setItems(items);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    protected void showProgress(boolean progressShown) {
        progressBar.setVisibility(progressShown ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(progressShown ? View.GONE : View.VISIBLE);
    }
}
