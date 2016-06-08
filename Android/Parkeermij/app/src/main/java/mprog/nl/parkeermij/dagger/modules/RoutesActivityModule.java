package mprog.nl.parkeermij.dagger.modules;

import dagger.Module;
import dagger.Provides;
import mprog.nl.parkeermij.MVP.interactors.RoutesActivityInteractor;
import mprog.nl.parkeermij.MVP.interactors.impl.RoutesActivityInteractorImpl;
import mprog.nl.parkeermij.MVP.presenters.RoutesActivityPresenter;
import mprog.nl.parkeermij.MVP.presenters.impl.RoutesActivityPresenterImpl;
import mprog.nl.parkeermij.MVP.views.RoutesActivityView;

/**
 * Created by Tamme on 31-5-2016.
 */
@Module
public class RoutesActivityModule {

    private RoutesActivityView mView;

    public RoutesActivityModule(RoutesActivityView view) {
        mView = view;
    }

    @Provides
    public RoutesActivityView provideRoutesView() {
        return mView;
    }

    @Provides
    public RoutesActivityPresenter provideRoutesPresenter(RoutesActivityPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    public RoutesActivityInteractor provideRoutesInteractor() {
        return new RoutesActivityInteractorImpl();
    }


}