package fr.jbrenier.petfoodingcontrol.android.bindingadpaters;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.utils.DateTimeUtils;

public final class BindingAdapters {
    private BindingAdapters() {}

    @BindingAdapter(value = "set_integer_text")
    public static void setText(TextView view, int integer) {
        view.setText(String.valueOf(integer));
    }

    @BindingAdapter(value = "set_weighing_text")
    public static void setText(TextView view, List<Weighing> weighing) {
                weighing.stream()
                .max(Comparator.comparing(Weighing::getWeighingDate))
                .ifPresent(w3 -> view.setText(String.valueOf(w3.getWeightInGrams())));
    }

    @BindingAdapter(value = "set_weighing_date_text")
    public static void setDateText(TextView view, List<Weighing> weighing) {
        weighing.stream()
                .max(Comparator.comparing(Weighing::getWeighingDate))
                .ifPresent(w3 -> view.setText(
                            DateTimeUtils.getStringDateFromOffsetDateTime(w3.getWeighingDate())));
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
