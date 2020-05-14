package fr.jbrenier.petfoodingcontrol.services.disposablemanagement;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DisposableManager {
    private static final String TAG = "Disposable Manager";

    /** Disposable management storing map */
    private final Map<DisposableOwner, CompositeDisposable> compositeDisposableMap
            = new HashMap<>();

    // empty constructor needed by Dagger
    @Inject
    public DisposableManager() { }

    /**
     * Add the Disposable to the CompositeDisposable corresponding to the Disposable owner.
     * @param disposableOwner the owner of the CompositeDisposable
     * @param disposable the Disposable to add
     */
    public final void addDisposable(DisposableOwner disposableOwner, Disposable disposable) {
        if (!compositeDisposableMap.containsKey(disposableOwner)) {
            compositeDisposableMap.put(disposableOwner, new CompositeDisposable());
        }
        compositeDisposableMap.get(disposableOwner).add(disposable);
    }

    /**
     * Clear the CompositeDisposable corresponding to the Disposable owner given in parameter,
     * if it exists in the storage map.
     * @param disposableOwner the context of th CompositeDisposable
     */
    public final void clear(DisposableOwner disposableOwner) {
        if (compositeDisposableMap.containsKey(disposableOwner)) {
            compositeDisposableMap.get(disposableOwner).clear();
        }
    }
}
