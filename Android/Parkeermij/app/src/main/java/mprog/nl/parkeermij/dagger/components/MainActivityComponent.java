package mprog.nl.parkeermij.dagger.components;

/**
 * Created by Tamme on 31-5-2016.
 */

import dagger.Component;
import mprog.nl.parkeermij.activities.MainActivity;
import mprog.nl.parkeermij.dagger.modules.MainActivityModule;

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
