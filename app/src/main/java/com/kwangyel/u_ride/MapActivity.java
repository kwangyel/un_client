package com.kwangyel.u_ride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {

    private ITileSource osm;
    private MapView mapView;
    private IMapController mapController;
    private BottomSheetBehavior bottomSheetBehavior;
    private TextInputLayout destField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        destField = findViewById(R.id.destLocation);

        mapView = findViewById(R.id.mapview);
        bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);

        osm = new XYTileSource("google sat tiles",
                0,22,256,".png",new String[]{
                "https://mt0.google.com",
                "https://mt1.google.com",
                "https://mt2.google.com",
                "https://mt3.google.com"
        }){
            @Override
            public String getTileURLString(long pMapTileIndex) {
                return getBaseUrl() + "/vt/lyrs=h&hl=en&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
            }
        };


        mapView.setTileSource(osm);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(false);

        mapController = mapView.getController();
        mapController.setZoom(15.0);
        mapController.setCenter(new GeoPoint(27.4712, 89.6339));

        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.4f);

        final MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTitleText("Select Pickup Time")
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = String.valueOf(timePicker.getHour());
                String min = String.valueOf(timePicker.getMinute());
                destField.setSuffixText(hour + " : "+min);
            }
        });

        destField.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kinley","Pressed clock");
                timePicker.show(getSupportFragmentManager(),null);
            }
        });
    }
}