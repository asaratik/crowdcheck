package com.msfthack.crowdcheck;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Bing imports
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapLayer;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapView;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.OnMapElementTappedListener;
import com.msfthack.crowdcheck.helpers.Locations;
import com.msfthack.crowdcheck.helpers.POI;
import com.msfthack.crowdcheck.helpers.Utils;


public class MapActivity extends AppCompatActivity{
    private MapView mMapView;
    private MapElementLayer mPinLayer;
    private List<MapIcon> pins;
    private MapLayer mapLayer;
    List<Locations> locations;
    private Geopoint LOCATION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = new MapView(this, MapRenderMode.VECTOR);
        mPinLayer = new MapElementLayer();
        mPinLayer.addOnMapElementTappedListener(new OnMapElementTappedListener() {
            @Override
            public boolean onMapElementTapped(MapElementTappedEventArgs mapElementTappedEventArgs) {
                CharSequence text = "Clicked on the location : "+mapElementTappedEventArgs.toString();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                return false;
            }
        });
        mMapView.getLayers().add(mPinLayer);
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Setup the required data for the map view
        Intent intent = getIntent();
        setupRequiredData(intent.getStringExtra("location"));
        Log.println(Log.INFO, "TAG", LOCATION.getPosition().getLatitude() + " -- " + LOCATION.getPosition().getLongitude());
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(LOCATION, 18),
                MapAnimationKind.NONE);
        Log.println(Log.INFO, "TAG", pins.toString());
        addPinsToLayer(pins);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private MapIcon getPin(Geopoint geo, String title)
    {
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(geo);
        pushpin.setTitle(title);
        MapFlyout flyOut = new MapFlyout();
        flyOut.setTitle(title);
        flyOut.setDescription("Crowdy");
        pushpin.setFlyout(flyOut);
        return pushpin;
    }

    private void addPinsToLayer(List<MapIcon> pins)
    {
        for(MapIcon pin:pins)
            mPinLayer.getElements().add(pin);
    }

    private void setupRequiredData(String location)
    {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "data.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Locations>>() { }.getType();
        locations = gson.fromJson(jsonFileString, listUserType);
        pins = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            if(locations.get(i).getTitle().equals(location))
            {
                Log.println(Log.INFO, "TAG", locations.get(i).toString());
                LOCATION = new Geopoint(locations.get(i).getLat(), locations.get(i).getLng());
                List<POI> list = locations.get(i).getPoi();
                for(int j = 0;j<list.size();j++)
                {
                    pins.add(getPin(new Geopoint(list.get(j).getLat(), list.get(j).getLng()), list.get(j).getTitle()));
                }
                break;
            }
        }
    }
}
