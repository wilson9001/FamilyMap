package a240.familymap.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.Inflater;

import a240.familymap.Activities.AppData;
import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link test.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link test#newInstance} factory method to
 * create an instance of this fragment.
 */
public class test extends android.support.v4.app.Fragment/*SupportMapFragment*/ //implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private TextView eventInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // TODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener;

    public test()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment test.
     */
    // TODO: Rename and change types and number of parameters
    public static test newInstance(/*String param1, String param2*/)
    {
        test fragment = new test();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        readyForMap(eventInfo);
        /*----SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mapFragmentContainer, mapFragment);

        //fragmentTransaction.addToBackStack("first");

        fragmentTransaction.commit();

        mapFragment.getMapAsync(this);---*/
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer1);
        /*getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("test"));
            }
        });

        /*if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        eventInfo = v.findViewById(R.id.eventInfoText);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void readyForMap(TextView eventInfoText/*Uri uri*/)
    {
        if (mListener != null)
        {
            mListener.loadMap(eventInfoText/*uri*/);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public void setEventInfo(String eventInfo)
    {
        this.eventInfo.setText(eventInfo);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        void loadMap(TextView eventInfoText/*Uri uri*/);
    }

   /* @Override
    public void onMapReady(GoogleMap googleMap)
    {
        GoogleMap mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng nextMarker = new LatLng(0,0);
        mMap.addMarker(new MarkerOptions().position(nextMarker).title("MyMarker"));*/

       /* LatLng nextMarker;

        AppData appdata = AppData.getInstance();

        HashMap<String, ArrayList<EventModel>> personIDtoFilteredEvents = appdata.getPersonIDToFilteredEvents();

        StringBuilder eventDescr;
        String personIDFromEvent, eventType;
        Set<String> personIDs = personIDtoFilteredEvents.keySet();
        HashMap<String, PersonModel> personIDToPersonModel = appdata.getPersonIDToPersonModel();
        Marker eventMarker;
        HashMap<String, Float> eventTypeToColor = appdata.getEventTypeColor();
        Float markerColor;

        for(String personID : personIDs )
        {
            ArrayList<EventModel> eventsForPerson = personIDtoFilteredEvents.get(personID);

            for(EventModel events : eventsForPerson)
            {
                nextMarker = new LatLng(events.getLatitude(), events.getLongitude());

                personIDFromEvent = events.getPerson();
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
                eventDescr.append(")");

                //float leftLimit = 0F;
                // /*- leftLimit*//*);
                eventType = events.getEventType();

                markerColor = eventTypeToColor.get(eventType.toLowerCase());
                eventMarker = mMap.addMarker(new MarkerOptions()
                        .position(nextMarker)
                        .title(eventDescr.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor/*markerColor.floatValue()*///) /*BitmapDescriptorFactory.HUE_AZURE*/));
               /* eventMarker.setTag(events.getEventID());
            }
        }

        mMap.setOnMarkerClickListener(this);
    }*/

    //@Override
   /* public boolean onMarkerClick(final Marker marker)
    {
        String eventID = (String) marker.getTag();

        Toast.makeText(this.getContext(), eventID, Toast.LENGTH_LONG).show();

        return false;
    }*/
}
