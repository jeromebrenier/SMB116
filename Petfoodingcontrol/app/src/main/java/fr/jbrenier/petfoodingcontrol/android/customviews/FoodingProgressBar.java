package fr.jbrenier.petfoodingcontrol.android.customviews;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class FoodingProgressBar extends ProgressBar {
    public FoodingProgressBar(Context context) {
        super(context);
    }

    public FoodingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoodingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        Drawable progressDrawable = getProgressDrawable();
        progressDrawable.setColorFilter(translateValueToColor(progress, getMax()));
        setProgressDrawable(progressDrawable);
    }

    private PorterDuffColorFilter translateValueToColor(int value, int max) {
        int R = 0;
        int G = 255;
        int B = 0;
        if (value > max) {
            R = 255;
            G = 0;
            B = 0;
        } else if (value > Math.round(max/2)) {
            R = 255;
            G = 171;
            B = 0;
        }
        int color = android.graphics.Color.argb(255, R, G, B);
        return new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
}
