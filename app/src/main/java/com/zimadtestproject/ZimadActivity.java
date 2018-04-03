package com.zimadtestproject;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zimadtestproject.api.Animal;
import com.zimadtestproject.observer.AnimalDataRepository;

import java.util.Observable;
import java.util.Observer;

public class ZimadActivity extends AppCompatActivity implements Observer {

    public static final String LOG_TAG = "===>" + ZimadActivity.class.getName();
    public static final String BUNDLE_PARAM_TAB_INDEX = "BUNDLE_PARAM_TAB_INDEX";
    private Observable animalDataRepositoryObservable;
    private TabLayout tabLayout;
    private Fragment catsFragment;
    private Fragment dogsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "onCreate - Add observer");
        animalDataRepositoryObservable = AnimalDataRepository.getInstance();
        animalDataRepositoryObservable.addObserver(this);

        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "onCreate - Restore fragments, selected tab");
            catsFragment = getSupportFragmentManager().getFragment(savedInstanceState, CatFragment.class.getName());
            dogsFragment = getSupportFragmentManager().getFragment(savedInstanceState, DogFragment.class.getName());
            int selectedTabIndex = savedInstanceState.getInt(BUNDLE_PARAM_TAB_INDEX, -1);
            if (selectedTabIndex != -1) {
                TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabIndex);
                if (selectedTab != null) {
                    selectedTab.select();
                }
            }
            if (dogsFragment == null) {
                dogsFragment = new DogFragment();
            }

            if (catsFragment == null) {
                catsFragment = new CatFragment();
            }
        } else {
            Log.d(LOG_TAG, "onCreate - Create new fragments, select cat tab by default");
            catsFragment = new CatFragment();
            dogsFragment = new DogFragment();
            selectTab(AnimalType.CAT.getId());
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState - Save fragments, selected tab index");
        super.onSaveInstanceState(outState);
        if (getSupportFragmentManager().findFragmentByTag(CatFragment.class.getName()) != null) {
            getSupportFragmentManager().putFragment(outState, CatFragment.class.getName(), catsFragment);
        }
        if (getSupportFragmentManager().findFragmentByTag(DogFragment.class.getName()) != null) {
            getSupportFragmentManager().putFragment(outState, DogFragment.class.getName(), dogsFragment);
        }
        outState.putInt(BUNDLE_PARAM_TAB_INDEX, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy - Remove observer");
        animalDataRepositoryObservable.deleteObserver(this);
    }

    private void selectTab(int tabPosition) {
        Log.d(LOG_TAG, "selectTab - tabPosition: " + tabPosition);
        if (tabPosition == AnimalType.CAT.getId()) {
            openFragment(catsFragment);
        } else {
            openFragment(dogsFragment);
        }
    }

    private void openFragment(Fragment fragment) {
        Log.d(LOG_TAG, "openFragment");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentLayout, fragment);
        if (fragment instanceof AnimalDetailedFragment) {
            ft.addToBackStack(fragment.getClass().getName());
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void update(Observable observable, Object arg) {
        Log.d(LOG_TAG, "This method is notified after click on some animal.");
        if (observable instanceof AnimalDataRepository) {
            AnimalDataRepository animalDataRepository = (AnimalDataRepository) observable;
            Animal selectedAnimal = animalDataRepository.getData();
            AnimalDetailedFragment fr = AnimalDetailedFragment.getInstance(selectedAnimal);
            openFragment(fr);
        }
    }
}