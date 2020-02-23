package fr.jbrenier.petfoodingcontrol.android.activities.petmanagement;

/**
 * The interface defining the contract of classes that manage pet data.
 * @author Jérôme Brenier
 */
public interface PetData {
    void loadPetData(PetManagementActivity petManagementActivity);
    void savePetData(PetManagementActivity petManagementActivity);
}
