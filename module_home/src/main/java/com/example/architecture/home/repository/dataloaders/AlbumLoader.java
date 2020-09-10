package com.example.architecture.home.repository.dataloaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.example.architecture.home.ui.model.home.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumLoader {

//                new String[]{"_id", "album", "artist", "artist_id", "numsongs", "minyear"},

    public static Album getAlbum(Cursor cursor) {
        Album album = new Album();
        if (cursor != null) {
            if (cursor.moveToFirst())
                album = new Album(cursor.getLong(10), cursor.getString(4), cursor.getString(3), cursor.getLong(5));
        }
        if (cursor != null)
            cursor.close();
        return album;
    }


    public static List<Album> getAlbumsForCursor(Cursor cursor) {
        ArrayList<Album> arrayList = new ArrayList<>();
        if ((cursor != null) && (cursor.moveToFirst()))
            do {
                arrayList.add(new Album(cursor.getLong(10), cursor.getString(4), cursor.getString(3), cursor.getLong(5)));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;
    }

    public static List<Album> getAllAlbums(Context context) {
        return getAlbumsForCursor(makeAlbumCursor(context, null, null));
    }

    public static Album getAlbum(Context context, long id) {
        return getAlbum(makeAlbumCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static List<Album> getAlbums(Context context, String paramString, int limit) {
        List<Album> result = getAlbumsForCursor(makeAlbumCursor(context, "album LIKE ?", new String[]{paramString + "%"}));
        if (result.size() < limit) {
            result.addAll(getAlbumsForCursor(makeAlbumCursor(context, "album LIKE ?", new String[]{"%_" + paramString + "%"})));
        }
        return result.size() < limit ? result : result.subList(0, limit);
    }


    public static Cursor makeAlbumCursor(Context context, String selection, String[] paramArrayOfString) {
//        final String albumSortOrder = PreferencesUtility.getInstance(context).getAlbumSortOrder();
        Cursor cursor = context.getContentResolver().query(
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
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                },
                selection,
                paramArrayOfString,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        return cursor;
    }
}
