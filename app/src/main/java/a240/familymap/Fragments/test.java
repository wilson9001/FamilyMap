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
import android.widget.LinearLayout;
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
    private LinearLayout eventDetailLayout;

    // TOoDO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // ToODO: Rename and change types of parameters
   /* private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener, mTransferListener;

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
    // TO0DO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        eventInfo = v.findViewById(R.id.eventInfoText);

        eventDetailLayout = v.findViewById(R.id.eventInfoLayout);
        eventDetailLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                transferToPerson();
            }
        });

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

    public void transferToPerson()
    {
        if(mTransferListener != null)
        {
            mTransferListener.toPersonInfo();
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
            mTransferListener = (OnFragmentInteractionListener) context;
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
        mTransferListener = null;
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
        void toPersonInfo();
    }
}
