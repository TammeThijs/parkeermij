package mprog.nl.parkeermij.dagger.modules;

import dagger.Module;
import dagger.Provides;
import mprog.nl.parkeermij.MVP.interactors.BaseActivityInteractor;
import mprog.nl.parkeermij.MVP.interactors.impl.BaseActivityInteractorImpl;
import mprog.nl.parkeermij.MVP.presenters.BaseActivityPresenter;
import mprog.nl.parkeermij.MVP.presenters.impl.BaseActivityPresenterImpl;
import mprog.nl.parkeermij.MVP.views.BaseActivityView;

/**
 * Created by Tamme on 31-5-2016.
 * Dagger for layer binding
 */
@Module
public class BaseActivityModule {

    private BaseActivityView mView;

    public BaseActivityModule(BaseActivityView view) {
        mView = view;
    }

    @Provides
    public BaseActivityView provideRoutesView() {
        return mView;
    }

    @Provides
    public BaseActivityPresenter provideRoutesPresenter(BaseActivityPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    public BaseActivityInteractor provideRoutesInteractor() {
        return new BaseActivityInteractorImpl();
    }


}