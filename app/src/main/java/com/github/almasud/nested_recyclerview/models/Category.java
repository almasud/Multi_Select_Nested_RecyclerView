package com.github.almasud.nested_recyclerview.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("subcatg")
    @Expose
    private List<Subcatg> subcatg = null;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Subcatg> getSubcatg() {
        return subcatg;
    }

    public void setSubcatg(List<Subcatg> subcatg) {
        this.subcatg = subcatg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeList(this.subcatg);
    }

    public void readFromParcel(Parcel source) {
        this.categoryId = source.readString();
        this.categoryName = source.readString();
        this.subcatg = new ArrayList<Subcatg>();
        source.readList(this.subcatg, Subcatg.class.getClassLoader());
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.categoryId = in.readString();
        this.categoryName = in.readString();
        this.subcatg = new ArrayList<Subcatg>();
        in.readList(this.subcatg, Subcatg.class.getClassLoader());
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}

