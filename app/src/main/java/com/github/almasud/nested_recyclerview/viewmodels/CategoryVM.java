package com.github.almasud.nested_recyclerview.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.github.almasud.nested_recyclerview.models.Category;
import com.github.almasud.nested_recyclerview.api.ApiClient;
import com.github.almasud.nested_recyclerview.api.requests.CategoryRequest;
import com.github.almasud.nested_recyclerview.api.responses.CategoryData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * An {@link AndroidViewModel} class for the {@link Category}.
 *
 * @author Abdullah Almasud
 */
public class CategoryVM extends AndroidViewModel {
    private static final String TAG = "CategoryVM";
    private MutableLiveData<List<Category>> mCategoriesMutableLiveData;

    public CategoryVM(@NonNull Application application) {
        super(application);
        loadCategoryData();
    }

    public void loadCategoryData() {
        ApiClient.getClient().create(CategoryRequest.class)
                .getCategoryData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CategoryData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull CategoryData categoryData) {
                        mCategoriesMutableLiveData.postValue(categoryData.getCategories());
                        Log.d(TAG, "onSuccess: first category is: " + categoryData.getCategories().get(0).getCategoryName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCategoriesMutableLiveData = null;
                        Log.e(TAG, "onError: " + e.getMessage());
                        Toast.makeText(getApplication(), "Failed to establish connection! Couldn't fetch the data.", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d(TAG, "loadCategoryData: is loaded.");
    }

    public LiveData<List<Category>> getCategoriesLiveData() {
        if (mCategoriesMutableLiveData == null) {
            mCategoriesMutableLiveData = new MutableLiveData<>();
        }
        return mCategoriesMutableLiveData;
    }
}
