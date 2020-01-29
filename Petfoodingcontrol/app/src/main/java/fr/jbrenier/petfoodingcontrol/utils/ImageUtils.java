package fr.jbrenier.petfoodingcontrol.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Utilities to manipulate images.
 * @author Jérôme Brenier
 */
public final class ImageUtils {

    private ImageUtils() {}

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

    /**
     * Convert a bitmap image into a representing string using Base64.
     * @param bitmapImage the bitmap image to convert
     * @return the corresponding base64 string
     */
    public static String getBase64StringFromBitmap(Bitmap bitmapImage) {
        if (bitmapImage != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        }
        return null;
    }

    /**
     * Crop the bitmap image to obtain a squared image.
     * @param source the source bitmap
     * @return the cropped image
     */
    public static Bitmap cropPhoto(Bitmap source) {
        int sourceHeight = source.getHeight();
        int sourceWidth = source.getWidth();
        Bitmap croppedPhoto = null;
        if (sourceHeight == sourceWidth) {
            croppedPhoto = source;
        } else if (sourceHeight > sourceWidth) {
            int newYStart = (sourceHeight - sourceWidth)/2;
            croppedPhoto = Bitmap.createBitmap(source, 0, newYStart, sourceWidth, sourceWidth);
        } else {
            int newXStart = (sourceWidth - sourceHeight)/2;
            croppedPhoto = Bitmap.createBitmap(source, newXStart, 0, sourceHeight, sourceHeight);
        }
        return croppedPhoto;
    }

    /**
     * Resize the photo to 450x450.
     * @param source the source photo
     * @return the resized photo
     */
    public static Bitmap resizePhoto(Bitmap source) {
        return Bitmap.createScaledBitmap(source, 450, 450, true);
    }
}
