package mprog.nl.parkeermij.dagger.components;

/**
 * Created by Tamme on 31-5-2016.
 */

import dagger.Component;
import mprog.nl.parkeermij.activities.StartUpActivity;
import mprog.nl.parkeermij.dagger.modules.StartUpActivityModule;

@Component(modules = StartUpActivityModule.class)
public interface StartUpActivityComponent {
    void inject(StartUpActivity activity);
}
