package com.baihe.lib_common.api;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.baihe.lib_common.entity.LocalPhotoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumParser {
        /**
         * 所有相片
         */
        public List<LocalPhotoEntity> photoInfos = new ArrayList<LocalPhotoEntity>();
        private ContentResolver resolver;

        public AlbumParser(Context context) {
            resolver = context.getContentResolver();
        }

        /***
         * 解析相册
         */
        public void parseAlbum() {
            Map<String, String> mapForPhotoThumbnail = parseThumbnail();
            String columns[] = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    MediaStore.Images.Media.DATE_MODIFIED + " desc");
            if (cursor.moveToFirst()) {
                int photoIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                do {
                    String photoId = cursor.getString(photoIdIndex);
                    String photoPath = cursor.getString(photoPathIndex);
                    LocalPhotoEntity photoInfo = new LocalPhotoEntity(photoId, photoPath);
                    String photoThumbnailPath = mapForPhotoThumbnail.get(photoId);
                    photoInfo.setPhotoThumbnailPath(photoThumbnailPath);

                    photoInfos.add(photoInfo); //添加到所有相片中
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        /**
         * 解析缩略图
         */
        private Map<String, String> parseThumbnail() {
            String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA};
            Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(resolver, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Thumbnails.MINI_KIND, projection);
            Map<String, String> map = new HashMap<>();
            if (cursor.moveToFirst()) {
                int photoIdIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int photoThumbnailIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    String phototId = String.valueOf(cursor.getInt(photoIdIndex));
                    String photoThumbnailPath = cursor.getString(photoThumbnailIndex);
                    map.put(phototId, photoThumbnailPath);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return map;
        }

    }