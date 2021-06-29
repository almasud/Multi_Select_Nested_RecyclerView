package com.github.almasud.nested_recyclerview.api.requests;

import com.github.almasud.nested_recyclerview.api.responses.CategoryData;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * The API interface for the {@link CategoryData}.
 *
 * @author Abdullah Almasud
 */
public interface CategoryRequest {
    @GET("get_categories")
    Single<CategoryData> getCategoryData();
}
