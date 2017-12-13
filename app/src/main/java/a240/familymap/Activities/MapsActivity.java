package a240.familymap.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import a240.familymap.Fragments.test;
import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, test.OnFragmentInteractionListener, GoogleMap.OnMarkerClickListener
{
    private test mapFragment;
    private EventModel EntryEvent;
    private GoogleMap mMap;
    private ArrayList<Polyline> linesOnMap;
    private String personIdOfSelectedPerson;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Iconify
                .with(new TypiconsModule())
                .with(new FontAwesomeModule());

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);//map1?
        mapFragment.getMapAsync(this);*/

        String eventIDOfEntryEvent = getIntent().getStringExtra(getString(R.string.eventIDForMapActivity));
        personIdOfSelectedPerson = getIntent().getStringExtra(getString(R.string.personIDForIntent));

        AppData appData = AppData.getInstance();

        HashMap<String, ArrayList<EventModel>> personIDToEventModels = appData.getPersonIDToFilteredEvents();

        ArrayList<EventModel> eventModels = personIDToEventModels.get(personIdOfSelectedPerson);

        for(EventModel event : eventModels)
        {
            if(event.getEventID().equals(eventIDOfEntryEvent))
            {
                EntryEvent = event;
                break;
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        mapFragment = new test();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragmentContainer, mapFragment);

        //fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        AppData appData = AppData.getInstance();

        if(mMap.getMapType() != appData.getMaptype())
        {
            mMap.setMapType(appData.getMaptype());
        }
    }

    @Override
    public void loadMap(TextView eventInfoText)
    {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mapFragmentContainer, mapFragment);

        //fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng nextMarker;

        AppData appdata = AppData.getInstance();

        HashMap<String, ArrayList<EventModel>> personIDtoFilteredEvents = appdata.getPersonIDToFilteredEvents();

        String eventType;
        Set<String> personIDs = personIDtoFilteredEvents.keySet();
        Marker eventMarker;
        HashMap<String, Float> eventTypeToColor = appdata.getEventTypeColor();
        Float markerColor;

        Marker selectedMarker = null;

        for(String personID : personIDs )
        {
            ArrayList<EventModel> eventsForPerson = personIDtoFilteredEvents.get(personID);

            // lineColor = random.nextInt(/*0xffffffff*/);

            //polylineOptions = new PolylineOptions().clickable(false).color(lineColor);

            for(EventModel events : eventsForPerson)
            {
                nextMarker = new LatLng(events.getLatitude(), events.getLongitude());

                // polylineOptions.add(nextMarker);

                eventType = events.getEventType();

                markerColor = eventTypeToColor.get(eventType.toLowerCase());
                eventMarker = mMap.addMarker(new MarkerOptions()
                        .position(nextMarker)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                eventMarker.setTag(events);

                if(events.getEventID().equals(EntryEvent.getEventID()))
                {
                    selectedMarker = eventMarker;
                }
            }

            // mMap.addPolyline(polylineOptions);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(this);

        //call onMarkerClick with selectedMarker
        onMarkerClick(selectedMarker);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedMarker.getPosition()));
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        if(linesOnMap != null)
        {
            for(Polyline line : linesOnMap)
            {
                line.remove();
            }

            linesOnMap.clear();
        }
        else
        {
            linesOnMap = new ArrayList<>();
        }

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

        TextView genderIcon = mapFragmentView.findViewById(R.id.genderIcon);

        if(personModel.getGender().equals("m"))
        {
            genderIcon.setText("{fa-male 35dp}");
        }
        else
        {
            genderIcon.setText("{fa-female 35dp}");
        }

        personIdOfSelectedPerson = personID;

        HashMap<String, ArrayList<EventModel>> personIDToEvents = appData.getPersonIDToFilteredEvents();

        if(appData.isShowLifeStoryLine())
        {
            ArrayList<EventModel> events = personIDToEvents.get(personID);

            PolylineOptions polylineOptions = new PolylineOptions().color(appData.getLifeStoryLine()).clickable(false);

            LatLng latLng;

            for(EventModel event : events)
            {
                latLng = new LatLng(event.getLatitude(), event.getLongitude());
                polylineOptions.add(latLng);
            }

            linesOnMap.add(mMap.addPolyline(polylineOptions));
        }
        if(appData.isShowSpouseLines())
        {
            String spouseID = personModel.getSpouse();

            PersonModel spouseModel = personIDToPersonModel.get(spouseID);

            if(spouseModel != null)
            {
                ArrayList<EventModel> events = personIDToEvents.get(personID);

                if(events != null)
                {
                    EventModel earliestEvent = events.get(0);

                    if(earliestEvent != null)
                    {
                        LatLng latLng = new LatLng(eventModel.getLatitude(), eventModel.getLongitude());
                        LatLng latLng1 = new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude());
                        linesOnMap.add(mMap.addPolyline(new PolylineOptions().clickable(false).color(appData.getSpouseLineColor()).add(latLng).add(latLng1)));
                    }
                }
            }
        }
        if(appData.isShowFamilyTreeLines())
        {
            int startingLineThickness = 25;

            addRecursiveAncestorPolyLines(eventModel, startingLineThickness);
        }

        return false;
    }


    private void addRecursiveAncestorPolyLines(EventModel seedEvent, int lineThickness)
    {
        if(lineThickness > 5)
        {
            lineThickness = lineThickness - 5;
        }

        AppData appData = AppData.getInstance();

        String personID = seedEvent.getPerson();

        HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

        PersonModel personModel = personIdToPersonModel.get(personID);

        String fatherID = personModel.getFather();
        String motherID = personModel.getMother();

        HashMap<String, ArrayList<EventModel>> personIdToEvents = appData.getPersonIDToFilteredEvents();

        if(fatherID != null)
        {
            ArrayList<EventModel> events = personIdToEvents.get(fatherID);

            if(events != null)
            {
                EventModel earliestEvent = events.get(0);

                if(earliestEvent != null)
                {
                    linesOnMap.add(mMap.addPolyline(new PolylineOptions()
                            .color(appData.getFamilyTreeLineColor())
                            .clickable(false)
                            .width(lineThickness)
                            .add(new LatLng(seedEvent.getLatitude(), seedEvent.getLongitude()))
                            .add(new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude()))));

                    addRecursiveAncestorPolyLines(earliestEvent, lineThickness);
                }
            }
        }
        if(motherID != null)
        {
            ArrayList<EventModel> events = personIdToEvents.get(motherID);

            if(events != null)
            {
                EventModel earliestEvent = events.get(0);

                if(earliestEvent != null)
                {
                    linesOnMap.add(mMap.addPolyline(new PolylineOptions()
                            .color(appData.getFamilyTreeLineColor())
                            .clickable(false)
                            .width(lineThickness)
                            .add(new LatLng(seedEvent.getLatitude(), seedEvent.getLongitude()))
                            .add(new LatLng(earliestEvent.getLatitude(), earliestEvent.getLongitude()))));

                    addRecursiveAncestorPolyLines(earliestEvent, lineThickness);
                }
            }
        }
    }

    @Override
    public void toPersonInfo()
    {
        if(personIdOfSelectedPerson != null)
        {
            //Toast.makeText(this, "Person Detail called", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MapsActivity.this, PersonActivity.class);
            intent.putExtra(getString(R.string.personIDForIntent), personIdOfSelectedPerson);

            startActivity(intent);
        }
    }
}
