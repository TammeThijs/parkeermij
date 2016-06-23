package mprog.nl.parkeermij.dagger.components;

/**
 * Created by Tamme on 31-5-2016.
 */

import dagger.Component;
import mprog.nl.parkeermij.activities.BaseActivity;
import mprog.nl.parkeermij.dagger.modules.BaseActivityModule;

@Component(modules = BaseActivityModule.class)
public interface BaseActivityComponent {
    void inject(BaseActivity activity);
}
