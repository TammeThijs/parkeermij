package mprog.nl.parkeermij.helpers;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import mprog.nl.parkeermij.R;

/**
 * Created by Tamme on 20-6-2016.
 */
public class PolygonHelper {

    private Context mContext;

    public PolygonHelper(Context context) {
        mContext = context;
    }

    /**
     * Reads the hardcoded coordinates for the polygons from xml arrays and returns an usable array
     * that contains a list, per zone, filled with a list of polygon coordinates.
     * DISCLAIMER: very ugly coding due to time and hardcoded data.
     */
    public List<List<List<LatLng>>> parseXML() {
        // init global method lists
        List<List<String>> mCompleteAreaList = new ArrayList<>();
        List<List<List<LatLng>>> mReturnList = new ArrayList<>();


        // reading string arrays from xml
        String[] mZone1Array = mContext.getResources().getStringArray(R.array.poly_zone1);
        String[] mZone2Array = mContext.getResources().getStringArray(R.array.poly_zone2);
        String[] mZone3Array = mContext.getResources().getStringArray(R.array.poly_zone3);
        String[] mZone4Array = mContext.getResources().getStringArray(R.array.poly_zone4);
        String[] mZone5Array = mContext.getResources().getStringArray(R.array.poly_zone5);
        String[] mZone6Array = mContext.getResources().getStringArray(R.array.poly_zone6);
        String[] mZone7Array = mContext.getResources().getStringArray(R.array.poly_zone7);

        // convert to List and add to global List
        mCompleteAreaList.add(Arrays.asList(mZone1Array));
        mCompleteAreaList.add(Arrays.asList(mZone2Array));
        mCompleteAreaList.add(Arrays.asList(mZone3Array));
        mCompleteAreaList.add(Arrays.asList(mZone4Array));
        mCompleteAreaList.add(Arrays.asList(mZone5Array));
        mCompleteAreaList.add(Arrays.asList(mZone6Array));
        mCompleteAreaList.add(Arrays.asList(mZone7Array));


        for (List<String> zone : mCompleteAreaList) {
            List<List<LatLng>> parsedPolyArray = new ArrayList<>();

            for (String polyString : zone) {

                List<LatLng> parsedPoints = new ArrayList<>();
                String[] points = polyString.split(", "); // split string into coordinate array

                // convert each coordinate string to usable Latlng object
                for (String point : points) {
                    StringTokenizer coordinates = new StringTokenizer(point, " ");
                    String lon = coordinates.nextToken();
                    String lat = coordinates.nextToken();
                    parsedPoints.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
                }
                parsedPolyArray.add(parsedPoints);
            }
            mReturnList.add(parsedPolyArray); // add to final list
        }

        return mReturnList;
    }
}
