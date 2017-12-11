package a240.familymap.Activities;

//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsIcons;
import com.joanzapata.iconify.fonts.TypiconsModule;

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
    private boolean isMapFragment;
    private String personIdOfSelectedPerson;
    private GoogleMap mMap;
    private ArrayList<Polyline> linesOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Iconify
                .with(new TypiconsModule())
                .with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);

        personIdOfSelectedPerson = null;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null)
        {
            fragment = new Login();

            isMapFragment = false;

            fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        else
        {
            isMapFragment = true;

            invalidateOptionsMenu();
        }
    }

    @Override
    public void transferToTLMap()
    {
        mapFragment = new test();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, mapFragment);

        fragmentTransaction.addToBackStack("first");

        isMapFragment = true;

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
        mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng nextMarker;

        AppData appdata = AppData.getInstance();

        HashMap<String, ArrayList<EventModel>> personIDtoFilteredEvents = appdata.getPersonIDToFilteredEvents();

        String eventType;
        Set<String> personIDs = personIDtoFilteredEvents.keySet();
        Marker eventMarker;
        HashMap<String, Float> eventTypeToColor = appdata.getEventTypeColor();
        Float markerColor;
        /*PolylineOptions polylineOptions;
        int lineColor;
        Random random = new Random();*/

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
            }

           // mMap.addPolyline(polylineOptions);
        }

       mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(this);

        invalidateOptionsMenu();
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
       // super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.top_level_map, menu);

        menu.findItem(R.id.search_menuItem).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search).actionBarSize());
        menu.findItem(R.id.filter_menuItem).setIcon(new IconDrawable(this, TypiconsIcons.typcn_filter).actionBarSize());
        menu.findItem(R.id.settings_menuItem).setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear).actionBarSize());

        menu.findItem(R.id.search_menuItem).setVisible(false);
        menu.findItem(R.id.filter_menuItem).setVisible(false);
        menu.findItem(R.id.settings_menuItem).setVisible(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(isMapFragment)
        {
            menu.findItem(R.id.search_menuItem).setVisible(true);
            menu.findItem(R.id.filter_menuItem).setVisible(true);
            menu.findItem(R.id.settings_menuItem).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.search_menuItem).setVisible(false);
            menu.findItem(R.id.filter_menuItem).setVisible(false);
            menu.findItem(R.id.settings_menuItem).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.search_menuItem:
                Toast.makeText(this, "Search Item Called", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filter_menuItem:
                Toast.makeText(this, "Filter item called", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings_menuItem:
                Toast.makeText(this, "Settings item called", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void toPersonInfo()
    {
        if(personIdOfSelectedPerson != null)
        {
            Toast.makeText(this, "Person Detail called", Toast.LENGTH_SHORT).show();
        }
    }
}