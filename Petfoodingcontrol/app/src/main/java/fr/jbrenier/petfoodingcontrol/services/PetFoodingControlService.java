package fr.jbrenier.petfoodingcontrol.services;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class PetFoodingControlService {
    /** Disposable management */
    protected final Map<Context, CompositeDisposable> compositeDisposableMap = new HashMap<>();

    /**
     * Add the Disposable to the CompositeDisposable corresponding to the context.
     * @param context the context of the CompositeDisposable
     * @param disposable the Disposable to add
     */
    protected final void addToCompositeDisposable(Context context, Disposable disposable) {
        if (!compositeDisposableMap.containsKey(context)) {
            compositeDisposableMap.put(context, new CompositeDisposable());
        }
        compositeDisposableMap.get(context).add(disposable);
    }

    /**
     * Clear the CompositeDisposable corresponding to the context given in parameter,
     * if it exists in the storage map.
     * @param context the context of th CompositeDisposable
     */
    protected final void compositeDisposableClear(Context context) {
        if (compositeDisposableMap.containsKey(context)) {
            compositeDisposableMap.get(context).clear();
        }
    }
}
