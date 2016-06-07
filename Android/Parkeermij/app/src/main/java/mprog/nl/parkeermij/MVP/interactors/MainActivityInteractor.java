package mprog.nl.parkeermij.MVP.interactors;

import java.util.List;

import mprog.nl.parkeermij.MVP.interfaces.ResponseListener;
import mprog.nl.parkeermij.models.Route;

/**
 * Created by Tamme on 31-5-2016.
 */
public interface MainActivityInteractor {

    void getRoutes(ResponseListener<List<Route>> listener);
}
