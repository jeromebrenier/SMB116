package fr.jbrenier.petfoodingcontrol.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Utilities to manipulate graphics.
 */
public final class GraphicUtils {

    private GraphicUtils() {}

    public static Bitmap getBitmapFromBase64String(String base64Image) {
        if (base64Image != null) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }
}
