package fr.jbrenier.petfoodingcontrol.services;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class PetFoodingControlService {
    /** Disposable management */
    protected final Map<Object, CompositeDisposable> compositeDisposableMap = new HashMap<>();

    /**
     * Add the Disposable to the CompositeDisposable corresponding to the context.
     * @param context the context of the CompositeDisposable
     * @param disposable the Disposable to add
     */
    protected final void addToCompositeDisposable(Object callingObject, Disposable disposable) {
        if (!compositeDisposableMap.containsKey(callingObject)) {
            compositeDisposableMap.put(callingObject, new CompositeDisposable());
        }
        compositeDisposableMap.get(callingObject).add(disposable);
    }

    /**
     * Clear the CompositeDisposable corresponding to the context given in parameter,
     * if it exists in the storage map.
     * @param context the context of th CompositeDisposable
     */
    protected final void compositeDisposableClear(Object callingObject) {
        if (compositeDisposableMap.containsKey(callingObject)) {
            compositeDisposableMap.get(callingObject).clear();
        }
    }
}
