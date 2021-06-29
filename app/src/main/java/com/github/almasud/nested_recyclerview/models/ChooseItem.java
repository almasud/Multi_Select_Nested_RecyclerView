package com.github.almasud.nested_recyclerview.models;

public class ChooseItem {
    public enum ItemType {
        ITEM_TYPE_CATEGORY,
        ITEM_TYPE_SUBCATEGORY
    }
    private String itemId;
    private String itemName;
    private ItemType itemType;

    public ChooseItem(String itemId, String itemName, ItemType itemType) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
