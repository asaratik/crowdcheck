package com.msfthack.crowdcheck;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Bing imports
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.maps.CustomTileMapLayer;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapLayer;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapTileBitmapRequestedEventArgs;
import com.microsoft.maps.MapView;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.OnBitmapRequestedListener;
import com.microsoft.maps.OnMapElementTappedListener;
import com.msfthack.crowdcheck.helpers.Locations;
import com.msfthack.crowdcheck.helpers.POI;
import com.msfthack.crowdcheck.helpers.Utils;


public class MapActivity extends AppCompatActivity{
    private MapView mMapView;
    private MapElementLayer mPinLayer;
    private List<MapIcon> pins;
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
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(),
                        getLocationDetails((int) mapElementTappedEventArgs.mapElements.get(0).getTag()), duration);
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
        mMapView.setScene(
                MapScene.createFromLocationAndZoomLevel(LOCATION, 18),
                MapAnimationKind.NONE);
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

    private MapIcon getPin(Geopoint geo, String title, int intensity, int uid)
    {
        MapIcon pushpin = new MapIcon();
        pushpin.setLocation(geo);
        MapFlyout flyOut = new MapFlyout();
        // ImageView iv = new ImageView(getApplicationContext());
        // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        try {
            InputStream is;
            if(intensity > 75)
            {
                is = getApplicationContext().getAssets().open("red.png");
                flyOut.setDescription("Busy");
            }
            else if(intensity > 40 && intensity < 75)
            {
                is = getApplicationContext().getAssets().open("orange.png");
                flyOut.setDescription("Moderate");
            }
            else
            {
                is = getApplicationContext().getAssets().open("green.png");
                flyOut.setDescription("Free");
            }
            pushpin.setTag(uid);
            pushpin.setImage(new MapImage(is));
            pushpin.setOpacity(0.5f);
        }catch (IOException io)
        {
            io.printStackTrace();
        }
        pushpin.setTitle(title);
        pushpin.setFlyout(flyOut);
        return pushpin;
    }

    private void addPinsToLayer(List<MapIcon> pins)
    {
        for(MapIcon pin:pins) {
            mPinLayer.getElements().add(pin);
        }
    }

    private void setupRequiredData(String location)
    {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "data.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Locations>>() { }.getType();
        locations = gson.fromJson(jsonFileString, listUserType);
        pins = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++)
            if(locations.get(i).getTitle().equals(location))
            {
                Log.println(Log.INFO, "TAG", locations.get(i).toString());
                LOCATION = new Geopoint(locations.get(i).getLat(), locations.get(i).getLng());
                List<POI> list = locations.get(i).getPoi();
                for(int j = 0;j<list.size();j++)
                    pins.add(getPin(new Geopoint(list.get(j).getLat(), list.get(j).getLng()),
                            list.get(j).getTitle(), list.get(j).getIntensity(), list.get(j).getUid()));
                break;
            }
    }

    private String getLocationDetails(int uid)
    {
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "data.json");
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Locations>>() { }.getType();
        locations = gson.fromJson(jsonFileString, listUserType);
        pins = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            List<POI> list = locations.get(i).getPoi();
            for(int j = 0;j<list.size();j++)
                if ((list.get(j).getUid() == uid))
                    return list.get(j).getTitle();
        }
        return "default";
    }
}
