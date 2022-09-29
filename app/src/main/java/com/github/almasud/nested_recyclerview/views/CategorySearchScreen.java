package com.github.almasud.nested_recyclerview.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.github.almasud.nested_recyclerview.models.Category;
import com.github.almasud.nested_recyclerview.R;
import com.github.almasud.nested_recyclerview.databinding.ActivityCategorySearchBinding;
import com.github.almasud.nested_recyclerview.views.Adapters.CategoryRVAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategorySearchScreen extends AppCompatActivity {
    private static final String TAG = "CategorySearchScreen";
    private ActivityCategorySearchBinding mViewBinding;
    private RecyclerView mCategoryRV;
    private CategoryRVAdapter mCategoryRVAdapter;
    private static Bundle sBundle;
    private List<Category> mCategories = new ArrayList<>();
    private SharedPreferences mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityCategorySearchBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        // Get the bundle from intent if exists
        sBundle = getIntent().getBundleExtra(getString(R.string.bundle_app));
        if (sBundle != null) {
            mCategories = sBundle.getParcelableArrayList(getString(R.string.parcel_name_categories));
            if (!mCategories.isEmpty()) {
                Log.d(TAG, "onCreate: First category is: " + mCategories.get(0).getCategoryName());
            }
        } else {
            Log.d(TAG, "onCreate: Data bundle is null");
        }

        // Set toolbar as an actionbar
        setSupportActionBar(mViewBinding.toolbarSearchCategory.getRoot());
        // Persist the data to hold state
        mPrefs = getSharedPreferences(getString(R.string.pref_name_categories), Context.MODE_PRIVATE);
        // Set the save button initial state
        mPrefs.edit().putString(getString(R.string.pref_key_action_save_categories),
                getString(R.string.pref_value_no))
                .apply();

        // Button action
        mViewBinding.toolbarSearchCategory.buttonBack.setOnClickListener(view -> finish());
        mViewBinding.toolbarSearchCategory.buttonSave.setOnClickListener(view -> {
            if (mPrefs.edit().putString(getString(R.string.pref_key_action_save_categories),
                    getString(R.string.pref_value_yes))
                    .commit()) {
                Intent intent = new Intent(this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mViewBinding.buttonClearAllCategory.setOnClickListener(view -> mCategoryRVAdapter.resetAllChecked());
        mViewBinding.linkMissingCategory.setOnClickListener(view -> {
            final String url = "https://almasud.github.io";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        mCategoryRV = mViewBinding.categoryRecyclerView;
        mCategoryRV.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRV.setHasFixedSize(true);
        mCategoryRVAdapter = new CategoryRVAdapter(this, mCategories);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the recycler view adapter
        mCategoryRV.setAdapter(mCategoryRVAdapter);
    }

    @Override
    protected void onStop() {
        mCategoryRV.setAdapter(null);
        super.onStop();
    }
}
