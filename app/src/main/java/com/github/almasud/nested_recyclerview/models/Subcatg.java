package com.github.almasud.nested_recyclerview.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcatg implements Parcelable {

    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subCategoryId);
        dest.writeString(this.subCategoryName);
    }

    public void readFromParcel(Parcel source) {
        this.subCategoryId = source.readString();
        this.subCategoryName = source.readString();
    }

    public Subcatg() {
    }

    protected Subcatg(Parcel in) {
        this.subCategoryId = in.readString();
        this.subCategoryName = in.readString();
    }

    public static final Parcelable.Creator<Subcatg> CREATOR = new Parcelable.Creator<Subcatg>() {
        @Override
        public Subcatg createFromParcel(Parcel source) {
            return new Subcatg(source);
        }

        @Override
        public Subcatg[] newArray(int size) {
            return new Subcatg[size];
        }
    };
}

