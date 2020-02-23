package fr.jbrenier.petfoodingcontrol.android.customviews;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Custom progress bar with a varying color depending on the progress level.
 * @author Jérôme Brenier
 */
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

    /**
     * Return a PorterDuffColorFilter with a color value depending on tha progress level :
     * <ul>
     *     <li>green with a level <= 50% of the max level</li>
     *     <li>orange between 50% and max excluded</li>
     *     <li>red above max included</li>
     * </ul>
     * @param value the progress value
     * @param max the max value
     * @return the resulting PorterDuffColorFilter
     */
    private PorterDuffColorFilter translateValueToColor(int value, int max) {
        int color = android.graphics.Color.argb(255, 0, 255, 0);
        if (value > max) {
            color = android.graphics.Color.argb(255, 255, 0, 0);
        } else if (value > Math.round(max/2)) {
            color = android.graphics.Color.argb(255, 255, 171, 0);
        }
        return new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
}
