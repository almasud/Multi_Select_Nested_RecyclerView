package com.github.almasud.nested_recyclerview.views.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.github.almasud.nested_recyclerview.models.Subcatg;
import com.github.almasud.nested_recyclerview.R;
import com.github.almasud.nested_recyclerview.databinding.ItemSubcategoryBinding;

public class SubcatgRVAdapter extends RecyclerView.Adapter<SubcatgRVAdapter.SubCatgViewHolder> {
    private static final String TAG = "SubcatgRVAdapter";
    private List<Subcatg> mSubcatgs;
    private final Context mContext;
    private SharedPreferences mPrefs;
    private RecyclerView mParentRecyclerView;

    public SubcatgRVAdapter(List<Subcatg> subcatgs, Context context) {
        Log.d(TAG, "SubcatgRVAdapter: is called");
        mContext = context;
        mSubcatgs = subcatgs;
        // Persist the data to hold state
        mPrefs = mContext.getSharedPreferences(
                mContext.getString(R.string.pref_name_categories), Context.MODE_PRIVATE
        );
    }

    @Override
    public SubCatgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSubcategoryBinding itemSubcategoryBinding = ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        mParentRecyclerView = parent.findViewById(R.id.rvSubCategory);

        return new SubCatgViewHolder(itemSubcategoryBinding);
    }

    @Override
    public void onBindViewHolder(SubCatgViewHolder holder, int position) {
        Subcatg subcatg = mSubcatgs.get(position);
        holder.subcategoryBinding.tvSubcategory.setText(subcatg.getSubCategoryName());
        Log.d(TAG, "onBindViewHolder: is called");

        // Load item with checked state
        loadItemWithCheckState(holder, subcatg);
    }


    @Override
    public int getItemCount() {
        return mSubcatgs.size();
    }

    public static class SubCatgViewHolder extends RecyclerView.ViewHolder {
        public ItemSubcategoryBinding subcategoryBinding;

        public SubCatgViewHolder(ItemSubcategoryBinding subcategoryBinding) {
            super(subcategoryBinding.getRoot());

            this.subcategoryBinding = subcategoryBinding;
        }
    }

    private void loadItemWithCheckState(SubCatgViewHolder holder, Subcatg subcatg) {

        final String subcategoryCheckedId =
                mContext.getString(R.string.pref_key_subcategory_checked_id) + subcatg.getSubCategoryId();
        // Check whether the item is checked or not
        if (mPrefs.getString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
                .equals(mContext.getString(R.string.pref_value_yes))) {
            holder.subcategoryBinding.iButtonSubcatAddable.setImageResource(R.drawable.ic_check);
        } else {
            holder.subcategoryBinding.iButtonSubcatAddable.setImageResource(R.drawable.ic_add);
        }

        // Action for subcategory add button click
        holder.subcategoryBinding.iButtonSubcatAddable.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Add item pref key is: " + subcategoryCheckedId);
            Log.d(TAG, "onBindViewHolder: Add item pref value is: " + (
                    mPrefs.getString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
            ));
            Log.d(TAG, "loadItemWithCheckState: getParentItemCount(): " + mParentRecyclerView.getAdapter().getItemCount());

            // Unchecked the item if is already checked
            if (mPrefs.getString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
                    .equals(mContext.getString(R.string.pref_value_yes))) {
                holder.subcategoryBinding.iButtonSubcatAddable.setImageResource(R.drawable.ic_add);

                // If any subcategory item is unchecked then it's category item should also be unchecked
                if (mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_no))
                        .commit()) {

//                    if (mParentRecyclerView.getAdapter().getItemCount() != mSubcatgs.size()) {
//                        if (mParentRecyclerView.getTag() instanceof Category) {
//                            Category category = (Category) mParentRecyclerView.getTag();
//                            Log.d(TAG, "loadItemWithCheckState unchecked: category is: " + category.getCategoryName());
//
//                            final String categoryCheckedId =
//                                    mContext.getString(R.string.pref_key_category_checked_id) + category.getCategoryId();
//                            if (mPrefs.edit().putString(categoryCheckedId, mContext.getString(R.string.pref_value_no))
//                                    .commit()) {
//
//                                // Reload the recycler view
//                                notifyDataSetChanged();
//                                if (mParentRecyclerView.getAdapter() != null) {
//                                    mParentRecyclerView.getAdapter().notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    }
                }
            } else {
                // Checked the item that is not checked yet
                holder.subcategoryBinding.iButtonSubcatAddable.setImageResource(R.drawable.ic_check);

                // If the all subcategory item is checked then it's category item should also be checked
                if (mPrefs.edit().putString(subcategoryCheckedId, mContext.getString(R.string.pref_value_yes))
                        .commit()) {

//                    if (mParentRecyclerView.getAdapter().getItemCount() == mSubcatgs.size()) {
//                        if (mParentRecyclerView.getTag() instanceof Category) {
//                            Category category = (Category) mParentRecyclerView.getTag();
//                            Log.d(TAG, "loadItemWithCheckState checked: category is: " + category.getCategoryName());
//
//                            final String categoryCheckedId =
//                                    mContext.getString(R.string.pref_key_category_checked_id) + category.getCategoryId();
//                            if (mPrefs.edit().putString(categoryCheckedId, mContext.getString(R.string.pref_value_yes))
//                                    .commit()) {
//
//                                // Reload the recycler view
//                                notifyDataSetChanged();
//                                if (mParentRecyclerView.getAdapter() != null) {
//                                    mParentRecyclerView.getAdapter().notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    }
                }
            }
        });
    }
}