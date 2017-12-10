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
   // private GoogleMap mMap;
    /*private EditText mServerPortInput, mServerHostInput, mUserNameInput, mPasswordInput;
    private Button mSignInButton;*/
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

        /*View fragmentView = fragment.getView();

        mServerPortInput = (EditText) fragmentView.findViewById(R.id.serverPortInput);
        mServerHostInput = (EditText) fragmentView.findViewById(R.id.serverHostInput);
        mUserNameInput = (EditText) fragmentView.findViewById(R.id.userNameInput);
        mPasswordInput = (EditText) fragmentView.findViewById(R.id.passwordInput);

        mSignInButton = (Button) fragmentView.findViewById(R.id.signInButton);
        //mSignInButton.setEnabled(false);
       mSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });
        */
    }

    /*private void signIn()
    {
        //grab info from host, port, username, password and send through server proxy to login to server.
        String serverHostString = mServerHostInput.getText().toString();
        int portInt = Integer.valueOf(mServerHostInput.getText().toString());
        String userNameString = mUserNameInput.getText().toString();
        String passwordString = mPasswordInput.getText().toString();

        //boolean success = false;

        //interact with HTTPServerProxy
        LoginTask loginTask = new LoginTask(this);

        loginTask.execute(serverHostString, String.valueOf(portInt), userNameString, passwordString);
        /*if(success)
        {
            String personName = "Login Successful";
            //^ will show first and last name concatenated.

            Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(this, R.string.signInFailedText, Toast.LENGTH_SHORT).show();
        }*/
    //}

    @Override
    public void transferToTLMap()/*onFragmentInteraction(Uri uri)*/
    {

        mapFragment = new test();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragmentContainer, mapFragment);

        fragmentTransaction.addToBackStack("first");

        fragmentTransaction.commit();
    }

    @Override
    public void loadMap(TextView eventInfoText/*Uri uri*/)
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
        //HashMap<String, PersonModel> personIDToPersonModel = appdata.getPersonIDToPersonModel();
        Marker eventMarker;
        HashMap<String, Float> eventTypeToColor = appdata.getEventTypeColor();
        Float markerColor;

        for(String personID : personIDs )
        {
            ArrayList<EventModel> eventsForPerson = personIDtoFilteredEvents.get(personID);

            for(EventModel events : eventsForPerson)
            {
                /*personIDFromEvent = events.getPerson();
                eventDescr = new StringBuilder();
                eventDescr.append(personIDToPersonModel.get(personIDFromEvent).getFirstName());
                eventDescr.append(" ");
                eventDescr.append(personIDToPersonModel.get(personIDFromEvent).getLastName());
                //eventDescr.append("\n");
                eventDescr.append(" ");
                eventDescr.append(events.getEventType());
                eventDescr.append(": ");
                eventDescr.append(events.getCity());
                eventDescr.append(", ");
                eventDescr.append(events.getCountry());
                eventDescr.append(" (");
                eventDescr.append(events.getYear());
                eventDescr.append(")");*/

                //float leftLimit = 0F;
                // /*- leftLimit*//*);

                nextMarker = new LatLng(events.getLatitude(), events.getLongitude());
                eventType = events.getEventType();

                markerColor = eventTypeToColor.get(eventType.toLowerCase());
                eventMarker = mMap.addMarker(new MarkerOptions()
                        .position(nextMarker)
                        //.title(eventDescr.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
                eventMarker.setTag(events/*.getEventID()*/);
            }
        }

       mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        //String eventID = (String) marker.getTag();
        EventModel eventModel = (EventModel) marker.getTag();

        AppData appData = AppData.getInstance();

        //HashMap<String, EventModel> eventIDToEventModel = appData.getEventIDToEvent();

        //EventModel eventModel = eventIDToEventModel.get(eventID);

        String personID = eventModel.getPerson();

        HashMap<String, PersonModel> personIDToPersonModel = appData.getPersonIDToPersonModel();

        PersonModel personModel = personIDToPersonModel.get(personID);

        //String firstName, lastName, city, country;
        //int year;

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

        //Toast.makeText(this, eventID, Toast.LENGTH_LONG).show();

        //android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.test);

        View mapFragmentView = mapFragment.getView();

        TextView eventInfo = mapFragmentView.findViewById(R.id.eventInfoText);

        //test currentMap = getSupportFragmentManager().findFragmentById(R.id.fragmentOuterContainer1);
        eventInfo.setText(eventInfoBuilder.toString());
        return false;
    }
}