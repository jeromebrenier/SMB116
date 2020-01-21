package fr.jbrenier.petfoodingcontrol.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * Utility class for LiveData.
 */
public class LiveDataUtils {

    private LiveDataUtils() {}
    SingleLiveEvent slv;
    public static <T> void observeOnce(LifecycleOwner owner, LiveData<T> liveData, Observer<T> observer) {
        liveData.observeForever(o -> {
            observer.onChanged(o);
            removeObserver(owner);
        });
    }
}
