package fr.jbrenier.petfoodingcontrol.android.bindingadpaters;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.time.OffsetDateTime;

import fr.jbrenier.petfoodingcontrol.utils.DateTimeUtils;

public final class BindingAdapters {
    private BindingAdapters() {}

    @BindingAdapter(value = "set_integer_text")
    public static void setText(TextView view, int integer) {
        view.setText(String.valueOf(integer));
    }

    @BindingAdapter(value = "set_date_text")
    public static void setText(TextView view, OffsetDateTime dateTime) {
        view.setText(DateTimeUtils.getStringDateFromOffsetDateTime(dateTime));
    }

    @BindingAdapter(value="set_bitmap")
    public static void setImage(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }
}
