package com.example.djangocrud;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;

public class FileUtil {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getPath(Context context, Uri uri) {
        // Converting Uri To Image Path
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        // Returning Image Path
        return filePath;
    }
}
