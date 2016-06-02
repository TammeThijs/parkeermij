package mprog.nl.parkeermij.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skyfishjy.library.RippleBackground;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mprog.nl.parkeermij.MVP.presenters.MainActivityPresenter;
import mprog.nl.parkeermij.MVP.views.MainActivityView;
import mprog.nl.parkeermij.R;
import mprog.nl.parkeermij.dagger.components.DaggerMainActivityComponent;
import mprog.nl.parkeermij.dagger.modules.MainActivityModule;

public class MainActivity extends AppCompatActivity implements MainActivityView,
        View.OnClickListener {

    public static final String TAG = "MainActivity";
    public static final int GPS_CHECK = 999;

    public LocationManager mLocManager;

    @BindView(R.id.location)
    FloatingActionButton mLocation;

    @BindView(R.id.ripple)
    RippleBackground mRippleBackground;

    @Inject
    MainActivityPresenter mPresenter;

    private Boolean isRipple = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initDependencies();

    }

    private void initDependencies() {
        DaggerMainActivityComponent.builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    private void init(){
        mLocation.setOnClickListener(this);
        mLocManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    }

    @Override
    public void startMainActivty() {
    // TODO
    }

    @Override
    public void toggleRipple() {
        if(!isRipple){
            startMap();
//            mRippleBackground.startRippleAnimation();
//            isRipple = true;
        }
        else {
            mRippleBackground.stopRippleAnimation();
            isRipple = false;
        }
    }

    private void gpsAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_CHECK);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }

    public void startMap(){

        // Check if GPS is enabled
        if ( !mLocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            gpsAlert();
        } else {
            Intent intent = LocationActivity.newIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GPS_CHECK){
            startMap();
        }
    }
}
