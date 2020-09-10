package com.example.architecture.home.repository.dataloaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.example.architecture.home.ui.model.home.Album;

import java.util.ArrayList;

public class ArtistAlbumLoader {

    public static ArrayList<Album> getAlbumsForArtist(Context context, long artistID) {

        ArrayList<Album> albumList = new ArrayList<>();
        Cursor cursor = makeAlbumForArtistCursor(context, artistID);

        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    Album album = new Album(cursor.getLong(10), cursor.getString(4), cursor.getString(3), cursor.getLong(5));
                    albumList.add(album);
                }
                while (cursor.moveToNext());

        }
        if (cursor != null)
            cursor.close();
        return albumList;
    }


    public static Cursor makeAlbumForArtistCursor(Context context, long artistID) {

        if (artistID == -1)
            return null;

        return context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ARTIST_ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION,
                        MediaStore.Audio.AudioColumns.ALBUM_ID
                },
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
    }

}
