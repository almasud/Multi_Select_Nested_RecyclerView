package com.github.almasud.nested_recyclerview.views.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.almasud.nested_recyclerview.models.Category;
import com.github.almasud.nested_recyclerview.models.Subcatg;
import com.github.almasud.nested_recyclerview.R;
import com.github.almasud.nested_recyclerview.databinding.ItemCategoryBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder> {
    private static final String TAG = "CategoryRVAdapter";
    private List<Category> mCategories;
    private final Context mContext;
    // An object of RecyclerView.RecycledViewPool　is created to share the Views　between
    // the child and　the parent RecyclerViews
    private final RecyclerView.RecycledViewPool mRvViewPool = new RecyclerView.RecycledViewPool();
    private SharedPreferences mPrefs;

    public CategoryRVAdapter(Context context, List<Category> categories) {
        mCategories = categories;
        mContext = context;

        // Persist the data to hold state
        mPrefs = mContext.getSharedPreferences(
                mContext.getString(R.string.pref_name_categories), Context.MODE_PRIVATE
        );
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCategoryBinding categoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(categoryBinding);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = mCategories.get(position);
        // Set the category name into the UI
        holder.categoryBinding.tvCategory.setText(category.getCategoryName());

        // Load item with expanded and checked state
        loadItemWithExpandState(holder, category);
        loadItemWithCheckState(holder, category);

        // For subcategory recycler view adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.categoryBinding.rvSubCategory.setLayoutManager(layoutManager);
        holder.categoryBinding.rvSubCategory.setHasFixedSize(true);
        SubcatgRVAdapter subcatgRVAdapter = new SubcatgRVAdapter(category.getSubcatg(), holder.categoryBinding.rvSubCategory.getContext());
        holder.categoryBinding.rvSubCategory.setAdapter(subcatgRVAdapter);
        holder.categoryBinding.rvSubCategory.setRecycledViewPool(mRvViewPool);

        // Set the category as a tag to get it's reference in subcategory adapter
        holder.categoryBinding.rvSubCategory.setTag(category);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public ItemCategoryBinding categoryBinding;

        public CategoryViewHolder(ItemCategoryBinding categoryBinding) {
            super(categoryBinding.getRoot());

            this.categoryBinding = categoryBinding;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        Log.d(TAG, "onDetachedFromRecyclerView: is called.");
        // Set all expanded values to no
        for (Category category : mCategories) {
            final String categoryExpandedId =
                    mContext.getString(R.string.pref_key_category_expanded_id) + category.getCategoryId();
            mPrefs.edit().putString(categoryExpandedId, mContext.getString(R.string.pref_value_no))
                    .apply();
        }

        // Reset all checked
        resetAllChecked();

        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void resetAllChecked() {
        // If the save button isn't pressed
        if (!mPrefs.getString(mContext.getString(R.string.pref_key_action_save_categories),
                mContext.getString(R.string.pref_value_no))
                .equals(mContext.getString(R.string.pref_value_yes))) {
            // Set all checked values to no
            for (Category category : mCategories) {
                final String categoryCheckedId =
                        mContext.getString(R.string.pref_key_category_checked_id) + category.getCategoryId();
                if (mPrefs.edit().putString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
                        .commit()) {

                    for (Subcatg subcatg : category.getSubcatg()) {
                        final String subcategoryCheckedId =
                                mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
                        mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
                                .apply();
                    }
                }
            }

            notifyDataSetChanged();
        }
    }

    public void updateCategories(List<Category> categories) {
        mCategories.clear();
        mCategories = categories;
        notifyDataSetChanged();
    }

    private void loadItemWithCheckState(CategoryViewHolder holder, Category category) {
        final String categoryCheckedId =
                mContext.getString(R.string.pref_key_category_checked_id) + category.getCategoryId();

        // Check whether the item is checked or not
        if (mPrefs.getString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
                .equals(mContext.getString(R.string.pref_value_yes))) {
            holder.categoryBinding.iButtonCatAddable.setImageResource(R.drawable.ic_check);

            // If the category is checked then it's all subcategory are also checked
//            for (Subcatg subcatg : category.getSubcatg()) {
//                final String subcategoryCheckedId =
//                        mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
//                mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_yes))
//                        .apply();
//            }
        } else {
            holder.categoryBinding.iButtonCatAddable.setImageResource(R.drawable.ic_add);

            // If the category is unchecked then it's all subcategory are also unchecked
//            for (Subcatg subcatg : category.getSubcatg()) {
//                final String subcategoryCheckedId =
//                        mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
//                mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
//                        .apply();
//            }
        }

        // Action for category add button click
        holder.categoryBinding.iButtonCatAddable.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Add item pref key is: " + categoryCheckedId);
            Log.d(TAG, "onBindViewHolder: Add item pref value is: " + (
                    mPrefs.getString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
            ));

            // Uncheck the item if already checked with it's subcategory items
            if (mPrefs.getString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
                    .equals(mContext.getString(R.string.pref_value_yes))) {
                holder.categoryBinding.iButtonCatAddable.setImageResource(R.drawable.ic_add);
                if (mPrefs.edit().putString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
                        .commit()) {
                    for (Subcatg subcatg: category.getSubcatg()) {
                        final String subcategoryCheckedId =
                                mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
                        mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
                                .apply();
                    }
                    // Reload the subcategory recycler view
                    if (holder.categoryBinding.rvSubCategory.getAdapter() != null) {
                        holder.categoryBinding.rvSubCategory.getAdapter().notifyDataSetChanged();
                    }
                }

            } else {
                // Checked the item if is not checked with it's subcategory items
                holder.categoryBinding.iButtonCatAddable.setImageResource(R.drawable.ic_check);
                if (mPrefs.edit().putString(categoryCheckedId, mContext.getString(R.string.pref_value_yes))
                        .commit()) {
                    for (Subcatg subcatg: category.getSubcatg()) {
                        final String subcategoryCheckedId =
                                mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
                        mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_yes))
                                .apply();
                    }
                    // Reload the subcategory recycler view
                    if (holder.categoryBinding.rvSubCategory.getAdapter() != null) {
                        holder.categoryBinding.rvSubCategory.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void loadItemWithExpandState(CategoryViewHolder holder, Category category) {
        final String categoryExpandedId =
                mContext.getString(R.string.pref_key_category_expanded_id) + category.getCategoryId();

        // Check whether the item is expanded or not
        if (mPrefs.getString(categoryExpandedId, mContext.getString(R.string.pref_value_no))
                .equals(mContext.getString(R.string.pref_value_yes))) {
            holder.categoryBinding.imageCatExpandable.setImageResource(R.drawable.ic_keyboard_arrow_down);
            holder.categoryBinding.rvSubCategory.setVisibility(View.VISIBLE);
        } else {
            holder.categoryBinding.imageCatExpandable.setImageResource(R.drawable.ic_arrow_forward_ios);
            holder.categoryBinding.rvSubCategory.setVisibility(View.GONE);
        }

        // Action for category item click
        holder.itemView.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Clicked item pref key is: " + categoryExpandedId);
            Log.d(TAG, "onBindViewHolder: Clicked item pref value is: " + (
                    mPrefs.getString(categoryExpandedId, mContext.getString(R.string.pref_value_no))
            ));
            if (mPrefs.getString(categoryExpandedId, mContext.getString(R.string.pref_value_no))
                    .equals(mContext.getString(R.string.pref_value_yes))) {
                holder.categoryBinding.imageCatExpandable.setImageResource(R.drawable.ic_arrow_forward_ios);
                holder.categoryBinding.rvSubCategory.setVisibility(View.GONE);
                mPrefs.edit().putString(categoryExpandedId, mContext.getString(R.string.pref_value_no))
                        .apply();
            } else {
                holder.categoryBinding.imageCatExpandable.setImageResource(R.drawable.ic_keyboard_arrow_down);
                holder.categoryBinding.rvSubCategory.setVisibility(View.VISIBLE);
                mPrefs.edit().putString(categoryExpandedId, mContext.getString(R.string.pref_value_yes))
                        .apply();
            }
        });
    }
}