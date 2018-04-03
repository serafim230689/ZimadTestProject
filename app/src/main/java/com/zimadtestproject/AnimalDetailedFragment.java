package com.zimadtestproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zimadtestproject.api.Animal;


public class AnimalDetailedFragment extends Fragment {

    private ImageView animalImageDetail;
    private TextView animalTextDetail;
    private TextView counter;
    private static final String PARCELABLE_KEY = "key";

    public static AnimalDetailedFragment getInstance(Animal animal) {
        AnimalDetailedFragment detailedFragment = new AnimalDetailedFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_KEY, animal);
        detailedFragment.setArguments(bundle);
        return detailedFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_detail, container, Boolean.parseBoolean(null));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        animalImageDetail = view.findViewById(R.id.animal_image_detail);
        animalTextDetail = view.findViewById(R.id.animal_text_detail);
        counter = view.findViewById(R.id.counter_detail);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Animal animal = bundle.getParcelable(PARCELABLE_KEY);
            Log.d("Animal Item", String.valueOf(animal));

            animalTextDetail.setText(animal.getTitle());
            Picasso.with(getContext()).load(animal.getImageUrl()).resize(setHeight(), setWidth()).into(animalImageDetail);
            counter.setText(Integer.toString(animal.getId()));
        }
    }

    private int setHeight() {//высота
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = (int) (displaymetrics.heightPixels / 2);
        return height;
    }

    private int setWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) (displaymetrics.heightPixels / 1.5);
        return width;
    }

}
