package com.tantuo.didicar.Bean;

public class MenuItemsEntity {


    private String menuItemsTitle = "ItemsTitles";
    private int menuItemsImgId;
    private String menuItemDetails;


public MenuItemsEntity(){}


    public MenuItemsEntity(String menuItemsTitle, int menuItemsImg,String menuItemDetails) {
        this.menuItemsTitle = menuItemsTitle;
        this.menuItemsImgId = menuItemsImg;
        this.menuItemDetails = menuItemDetails;
    }

    public int getMenuItemsImgId() {
        return menuItemsImgId;
    }

    public void setMenuItemsImgId(int menuItemsImgId) {
        this.menuItemsImgId = menuItemsImgId;
    }

    public String getMenuItemsTitle() {
        return menuItemsTitle;
    }

    public void setMenuItemsTitle(String menuItemsTitle) {
        this.menuItemsTitle = menuItemsTitle;
    }

    public String getMenuItemDetails() {
        return menuItemDetails;
    }

    public void setMenuItemDetails(String menuItemDetails) {
        this.menuItemDetails = menuItemDetails;
    }
}