package fr.jbrenier.petfoodingcontrol;

import android.app.Application;

import fr.jbrenier.petfoodingcontrol.db.DAOComponent;
import fr.jbrenier.petfoodingcontrol.db.DaggerDAOComponent;

public class PetFoodingControl extends Application {
    private static DAOComponent daoComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        daoComponent = DaggerDAOComponent.builder().build();
    }

    public static DAOComponent getDAOComponent() {
        return daoComponent;
    }
}
