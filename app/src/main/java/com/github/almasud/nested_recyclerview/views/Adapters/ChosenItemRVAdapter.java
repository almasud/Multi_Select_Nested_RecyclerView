package com.github.almasud.nested_recyclerview.views.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.almasud.nested_recyclerview.models.ChooseItem;
import com.github.almasud.nested_recyclerview.R;
import com.github.almasud.nested_recyclerview.databinding.ItemChosenBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChosenItemRVAdapter extends RecyclerView.Adapter<ChosenItemRVAdapter.ChosenItemViewHolder> {
    private static final String TAG = "ChosenItemRVAdapter";
    private List<ChooseItem> mChooseItems;
    private final Context mContext;
    private SharedPreferences mPrefs;

    public ChosenItemRVAdapter(Context context) {
        mChooseItems = new ArrayList<>();
        mContext = context;

        // Persist the data to hold state
        mPrefs = mContext.getSharedPreferences(
                mContext.getString(R.string.pref_name_categories), Context.MODE_PRIVATE
        );
    }

    @Override
    public ChosenItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChosenBinding itemChosenBinding = ItemChosenBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChosenItemViewHolder(itemChosenBinding);
    }

    @Override
    public int getItemCount() {
        return mChooseItems.size();
    }

    @Override
    public void onBindViewHolder(ChosenItemViewHolder holder, int position) {
        ChooseItem chooseItem = mChooseItems.get(position);
        holder.chosenBinding.tvChosenItem.setText(chooseItem.getItemName());

        // Load item with checked state
        loadItemWithCheckState(holder, chooseItem);
    }

    public static class ChosenItemViewHolder extends RecyclerView.ViewHolder {
        public ItemChosenBinding chosenBinding;

        public ChosenItemViewHolder(ItemChosenBinding chosenBinding) {
            super(chosenBinding.getRoot());

            this.chosenBinding = chosenBinding;
        }
    }

    public void updateCategories(List<ChooseItem> chooseItems) {
        mChooseItems.clear();
        mChooseItems = chooseItems;
        notifyDataSetChanged();
    }

    private void loadItemWithCheckState(ChosenItemViewHolder holder, ChooseItem chooseItem) {

        final String checkedItemId;
        switch (chooseItem.getItemType()) {
            case ITEM_TYPE_CATEGORY:
                checkedItemId = mContext.getString(R.string.pref_key_category_checked_id) + chooseItem.getItemId();
                break;
            case ITEM_TYPE_SUBCATEGORY:
                checkedItemId = mContext.getString(R.string.pref_key_subcategory_checked_id) + chooseItem.getItemId();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + chooseItem.getItemType());
        }

        // Action for category add button click
        holder.chosenBinding.buttonClose.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: Add item pref key is: " + checkedItemId);
            Log.d(TAG, "onBindViewHolder: Add item pref value is: " + (
                    mPrefs.getString(checkedItemId, mContext.getString(R.string.pref_value_no))
            ));

            // Uncheck the item if already is checked
            if (mPrefs.getString(checkedItemId, mContext.getString(R.string.pref_value_no))
                    .equals(mContext.getString(R.string.pref_value_yes))) {
                mPrefs.edit().putString(checkedItemId, mContext.getString(R.string.pref_value_no))
                        .apply();
            }

            // Remove the items and reload the recycler view
            mChooseItems.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), mChooseItems.size());
//            notifyDataSetChanged();
        });
    }
}