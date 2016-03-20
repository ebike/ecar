package com.jcsoft.ecar.event;


import com.jcsoft.ecar.bean.ImageItem;

/**
 * Created by huguangwen on 16/1/29.
 */
public class SelectPhotoEvent {
    private ImageItem item;

    public SelectPhotoEvent(ImageItem item) {
        this.item = item;
    }

    public ImageItem getItem() {
        return item;
    }

    public void setItem(ImageItem item) {
        this.item = item;
    }
}
