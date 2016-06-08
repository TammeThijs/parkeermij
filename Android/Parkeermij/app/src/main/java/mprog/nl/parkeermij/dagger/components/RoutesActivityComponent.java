package mprog.nl.parkeermij.dagger.components;

/**
 * Created by Tamme on 31-5-2016.
 */

import dagger.Component;
import mprog.nl.parkeermij.activities.RoutesActivity;
import mprog.nl.parkeermij.dagger.modules.RoutesActivityModule;

@Component(modules = RoutesActivityModule.class)
public interface RoutesActivityComponent {
    void inject(RoutesActivity activity);
}
