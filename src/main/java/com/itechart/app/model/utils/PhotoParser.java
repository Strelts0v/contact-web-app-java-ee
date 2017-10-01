package com.itechart.app.model.utils;

import com.itechart.app.model.entities.Photo;
import org.apache.commons.fileupload.FileItem;

import java.io.IOException;

public class PhotoParser {

    public static Photo parsePhoto(FileItem item) throws IOException {
        Photo photo = new Photo();
        photo.setPhotoStream(item.getInputStream());
        photo.setPhotoSize((int)item.getSize());
        return photo;
    }
}
