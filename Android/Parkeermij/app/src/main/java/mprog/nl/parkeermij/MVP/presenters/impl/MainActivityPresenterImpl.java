package mprog.nl.parkeermij.MVP.presenters.impl;

import javax.inject.Inject;

import mprog.nl.parkeermij.MVP.presenters.MainActivityPresenter;
import mprog.nl.parkeermij.MVP.views.MainActivityView;
import mprog.nl.parkeermij.R;

/**
 * Created by Tamme on 31-5-2016.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {

    public static final String TAG = "MainPresenter";

    private MainActivityView mView;

    @Inject
    public MainActivityPresenterImpl(MainActivityView view) {
        mView = view;
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.location:
                mView.startMap();
                break;
            default:
                break;
        }
    }
}
