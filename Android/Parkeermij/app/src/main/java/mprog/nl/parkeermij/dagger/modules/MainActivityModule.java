package mprog.nl.parkeermij.dagger.modules;

import dagger.Module;
import dagger.Provides;
import mprog.nl.parkeermij.MVP.interactors.StartUpActivityInteractor;
import mprog.nl.parkeermij.MVP.interactors.impl.StartUpActivityInteractorImpl;
import mprog.nl.parkeermij.MVP.presenters.StartUpActivityPresenter;
import mprog.nl.parkeermij.MVP.presenters.impl.StartUpActivityPresenterImpl;
import mprog.nl.parkeermij.MVP.views.StartUpActivityView;

/**
 * Created by Tamme on 31-5-2016.
 */
@Module
public class MainActivityModule {

    private StartUpActivityView mView;

    public MainActivityModule(StartUpActivityView view) {
        mView = view;
    }

    @Provides
    public StartUpActivityView provideMainView() {
        return mView;
    }

    @Provides
    public StartUpActivityPresenter provideMainPresenter(StartUpActivityPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    public StartUpActivityInteractor provideLoginInteractor() {
        return new StartUpActivityInteractorImpl();
    }


}