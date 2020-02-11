package fr.jbrenier.petfoodingcontrol.ui.bindingadpaters;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public final class BindingAdapters {
    private BindingAdapters() {}

    @BindingAdapter(value = "set_integer_text")
    public static void setText(TextView view, int integer) {
        view.setText(String.valueOf(integer));
    }

    @BindingAdapter(value="set_bitmap")
    public static void setImage(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }
}
