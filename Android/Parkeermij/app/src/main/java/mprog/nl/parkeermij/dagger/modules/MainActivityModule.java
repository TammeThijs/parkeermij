package mprog.nl.parkeermij.dagger.modules;

import dagger.Module;
import dagger.Provides;
import mprog.nl.parkeermij.MVP.interactors.MainActivityInteractor;
import mprog.nl.parkeermij.MVP.interactors.impl.MainActivityInteractorImpl;
import mprog.nl.parkeermij.MVP.presenters.MainActivityPresenter;
import mprog.nl.parkeermij.MVP.presenters.impl.MainActivityPresenterImpl;
import mprog.nl.parkeermij.MVP.views.MainActivityView;

/**
 * Created by Tamme on 31-5-2016.
 */
@Module
public class MainActivityModule {

    private MainActivityView mView;

    public MainActivityModule(MainActivityView view) {
        mView = view;
    }

    @Provides
    public MainActivityView provideMainView() {
        return mView;
    }

    @Provides
    public MainActivityPresenter provideMainPresenter(MainActivityPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    public MainActivityInteractor provideLoginInteractor() {
        return new MainActivityInteractorImpl();
    }


}