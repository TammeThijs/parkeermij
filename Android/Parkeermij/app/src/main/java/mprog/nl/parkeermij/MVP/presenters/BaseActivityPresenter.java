package mprog.nl.parkeermij.MVP.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Tamme on 8-6-2016.
 */
public interface BaseActivityPresenter {
    void init(Intent intent);
    void onNavigationItemSelected(int id,Fragment fragment);
    void onOptionsItemSelected(int id, Context context, boolean isChecked);
}
