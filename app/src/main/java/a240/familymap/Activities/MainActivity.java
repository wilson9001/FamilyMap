package a240.familymap.Activities;

//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.net.Uri;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Button;
//import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import a240.familymap.Fragments.Login;
//import a240.familymap.Fragments.TLMap;
import a240.familymap.Fragments.test;
import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.R;

//import static a240.familymap.R.layout.fragment_login;

public class MainActivity extends AppCompatActivity implements Login.OnFragmentInteractionListener, test.OnFragmentInteractionListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener /*, LoginTask.Context, SyncTask.Context*/
{
    private test mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null)
        {
            fragment = new Login();

            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public void transferToTLMap()
    {

        mapFragment = new test();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, mapFragment);

        fragmentTransaction.addToBackStack("first");

        fragmentTransaction.commit();
    }

    @Override
    public void loadMap(TextView eventInfoText)
    {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mapFragmentContainer, mapFragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        GoogleMap mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng nextMarker;

        AppData appdata = AppData.getInstance();

        HashMap<String, ArrayList<EventModel>> personIDtoFilteredEvents = appdata.getPersonIDToFilteredEvents();

        String eventType;
        Set<String> personIDs = personIDtoFilteredEvents.keySet();
        Marker eventMarker;
        HashMap<String, Float> eventTypeToColor = appdata.getEventTypeColor();
        Float markerColor;

        for(String personID : personIDs )
        {
            ArrayList<EventModel> eventsForPerson = personIDtoFilteredEvents.get(personID);

            for(EventModel events : eventsForPerson)
            {
                nextMarker = new LatLng(events.getLatitude(), events.getLongitude());
                eventType = events.getEventType();

                markerColor = eventTypeToColor.get(eventType.toLowerCase());
                eventMarker = mMap.addMarker(new MarkerOptions()
                        .position(nextMarker)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                eventMarker.setTag(events);
            }
        }

       mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        EventModel eventModel = (EventModel) marker.getTag();

        AppData appData = AppData.getInstance();

        String personID = eventModel.getPerson();

        HashMap<String, PersonModel> personIDToPersonModel = appData.getPersonIDToPersonModel();

        PersonModel personModel = personIDToPersonModel.get(personID);

        StringBuilder eventInfoBuilder = new StringBuilder();

        eventInfoBuilder.append(personModel.getFirstName());
        eventInfoBuilder.append(" ");
        eventInfoBuilder.append(personModel.getLastName());
        eventInfoBuilder.append("\n");
        eventInfoBuilder.append(eventModel.getEventType());
        eventInfoBuilder.append(": ");
        eventInfoBuilder.append(eventModel.getCity());
        eventInfoBuilder.append(", ");
        eventInfoBuilder.append(eventModel.getCountry());
        eventInfoBuilder.append(" (");
        eventInfoBuilder.append(eventModel.getYear());
        eventInfoBuilder.append(")");

        View mapFragmentView = mapFragment.getView();

        TextView eventInfo = mapFragmentView.findViewById(R.id.eventInfoText);

        eventInfo.setText(eventInfoBuilder.toString());
        return false;
    }
}