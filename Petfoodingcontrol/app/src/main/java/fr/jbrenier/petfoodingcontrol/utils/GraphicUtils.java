package fr.jbrenier.petfoodingcontrol.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Utilities to manipulate graphics.
 * @author Jérôme Brenier
 */
public final class GraphicUtils {

    private GraphicUtils() {}

    /**
     * Generate a bitmap image from a string representing a base64 encoded image.
     * @param base64Image string representing the base64 encoded image
     * @return the corresponding bitmap image
     */
    public static Bitmap getBitmapFromBase64String(String base64Image) {
        if (base64Image != null) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        return null;
    }
}
