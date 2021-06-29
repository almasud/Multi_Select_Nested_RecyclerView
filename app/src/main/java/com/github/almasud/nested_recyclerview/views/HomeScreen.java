package com.github.almasud.nested_recyclerview.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;

import com.github.almasud.nested_recyclerview.models.Category;
import com.github.almasud.nested_recyclerview.models.ChooseItem;
import com.github.almasud.nested_recyclerview.models.Subcatg;
import com.github.almasud.nested_recyclerview.R;
import com.github.almasud.nested_recyclerview.databinding.ActivityHomeScreenBinding;
import com.github.almasud.nested_recyclerview.viewmodels.CategoryVM;
import com.github.almasud.nested_recyclerview.views.Adapters.ChosenItemRVAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG = "HomeScreen";
    private ActivityHomeScreenBinding mViewBinding;
    private RecyclerView mChosenItemRV;
    private CategoryVM mCategoryVM;
    private ChosenItemRVAdapter mChosenItemRVAdapter;
    private List<Category> mCategories = new ArrayList<>();
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        // Initialize the view model & prefs
        mCategoryVM = new ViewModelProvider(this).get(CategoryVM.class);
        // Persist the data to hold state
        mPrefs = getSharedPreferences(
                getString(R.string.pref_name_categories), Context.MODE_PRIVATE
        );

        // For recycler view
        mChosenItemRV = mViewBinding.chosenItemsRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mChosenItemRV.setLayoutManager(layoutManager);
        mChosenItemRVAdapter = new ChosenItemRVAdapter(this);

        // Load the categories from view model
        mCategoryVM.getCategoriesLiveData().observe(this, categories -> {
            mCategories = categories;
            ArrayList<ChooseItem> chooseItems = new ArrayList<>();

            for (Category category : categories) {
                final String categoryCheckedId =
                        getString(R.string.pref_key_category_checked_id) + category.getCategoryId();

                // Check whether the category item is checked or not
                if (mPrefs.getString(categoryCheckedId, getString(R.string.pref_value_no))
                        .equals(getString(R.string.pref_value_yes))) {
                    chooseItems.add(new ChooseItem(
                            category.getCategoryId(),
                            category.getCategoryName(),
                            ChooseItem.ItemType.ITEM_TYPE_CATEGORY
                    ));
                    // If the category is checked then it's all subcategory are also checked
//                    for (Subcatg subcatg : category.getSubcatg()) {
//                        chooseItems.add(new ChooseItem(
//                                subcatg.getSubCategoryId(),
//                                subcatg.getSubCategoryName(),
//                                ChooseItem.ItemType.ITEM_TYPE_SUBCATEGORY
//                        ));
//                    }
                } else {
                    // Check whether the subcategory is checked or not while the whole category is not checked
                    for (Subcatg subcatg : category.getSubcatg()) {
                        final String subcategoryCheckedId =
                                getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
                        if (mPrefs.getString(subcategoryCheckedId, getString(R.string.pref_value_no))
                                .equals(getString(R.string.pref_value_yes))) {
                            chooseItems.add(new ChooseItem(
                                    subcatg.getSubCategoryId(),
                                    subcatg.getSubCategoryName(),
                                    ChooseItem.ItemType.ITEM_TYPE_SUBCATEGORY
                            ));
                        }
                    }
                }
            }

            mChosenItemRVAdapter.updateCategories(chooseItems);
        });
        // Action for navigating to the category search screen
        mViewBinding.buttonChooseCategoryScreen.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(getString(R.string.parcel_name_categories), (ArrayList<? extends Parcelable>) mCategories);
            Intent intent = new Intent(HomeScreen.this, CategorySearchScreen.class);
            intent.putExtra(getString(R.string.bundle_app), bundle);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the recycler view adapter
        mChosenItemRV.setAdapter(mChosenItemRVAdapter);
    }
}