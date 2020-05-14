package fr.jbrenier.petfoodingcontrol.services.disposablemanagement;

/**
 * Interface to be implemented by classes that are Disposable owners.
 * @author Jerome Brenier
 */
public interface DisposableOwner {
    /**
     * Clear the owned Disposable objects.
     */
    void clearDisposables();
}
