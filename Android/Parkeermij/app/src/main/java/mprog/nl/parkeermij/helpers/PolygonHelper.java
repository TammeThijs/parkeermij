package mprog.nl.parkeermij.helpers;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
     * Reads the hardcoded coordinates for the polygons from xml and returns an usable array
     * of Latlng objects
     */
    public List<List<LatLng>> parseXML(){
        List<List<LatLng>> parsedPolyArray = new ArrayList<>();
        String[] mXmlArray = mContext.getResources().getStringArray(R.array.polygon_geo_array);

        for (String polyString: mXmlArray) {

            List<LatLng> parsedPoints = new ArrayList<>();
            String[] points = polyString.split(", ");

            for (String point : points) {
                StringTokenizer coordinates = new StringTokenizer(point, " ");
                String lon = coordinates.nextToken();
                String lat = coordinates.nextToken();
                parsedPoints.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));
            }
            parsedPolyArray.add(parsedPoints);
        }

        return parsedPolyArray;
    }




}
