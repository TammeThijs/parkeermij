package mprog.nl.parkeermij.MVP.presenters;

import mprog.nl.parkeermij.models.LocationObject;

/**
 * Created by Tamme on 31-5-2016.
 */
public interface StartUpActivityPresenter {
    void onClick(int id);
    void getData(LocationObject locationObject);
}
