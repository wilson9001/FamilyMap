package a240.familymap.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//import com.google.common.collect.ComparisonChain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import a240.familymap.Activities.AppData;
import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.Responses.EventResponse;
import a240.familymap.Responses.PersonResponse;
import a240.familymap.ServerProxy;

/**
 * Created by Alex on 11/17/2017.
 */

public class SyncTask extends AsyncTask</*String*/Boolean, Void, /*Pair<List<PersonModel>, List<PersonModel>>*/Boolean>
{
    public interface Context
    {
        void onSyncComplete(/*Pair<List<PersonModel>, List<PersonModel>> fatherMotherPair*/Boolean success);
    }

    private Context context;

    public SyncTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected /*Pair<List<PersonModel>, List<PersonModel>>*/Boolean doInBackground(Boolean... booleans/*String... strings*/)
    {
        ServerProxy serverProxy = new ServerProxy();
        AppData appData = AppData.getInstance();

        String serverHost = appData.getServerHost(); //strings[0];
        int serverPort = appData.getPort(); //Integer.valueOf(strings[1]);
        String personID = "/person/" + (booleans[0] ? appData.getUserPersonID() : "");/*appData.getUserPersonID();*/ //strings[3];
        String authToken = appData.getAuthToken(); //strings[2];

        try
        {
            PersonResponse personResponse= serverProxy.getPeople(new URL("http", serverHost, serverPort, personID), authToken);

            //LoginResponse loginResponse = serverProxy.login(new URL("http", serverHost, serverPort, "/"), new LoginRequest(userName, password));

            if(personResponse == null)
            {
                return false;
            }

            String errorMessage = personResponse.getErrorMessage();

            if(errorMessage == null)
            {
                ArrayList<PersonModel> people = personResponse.getPeople();

               // Pair fatherMotherPair = new Pair<List<PersonModel>, List<PersonModel>>;
                /*if(people == null)
                {
                    people = new ArrayList<>();

                    people.add(new PersonModel(personResponse.getPersonID(), personResponse.getDescendant(), personResponse.getFirstName(), personResponse.getLastName(), personResponse.getGender(), personResponse.getFather(), personResponse.getMother(), personResponse.getSpouse()));

                    return new Pair<List<PersonModel>, List<PersonModel>>(people, null);
                }*/

                String userPersonID = appData.getUserPersonID();

                HashMap<String, PersonModel> personIDToPerson = new HashMap<>();

                for(PersonModel currentPerson : people)
                {
                   personIDToPerson.put(currentPerson.getPersonID(), currentPerson);
                }

                PersonModel userPerson = personIDToPerson.get(userPersonID);

                if(userPerson == null)
                {
                    return false;
                }

                appData.setUserPersonModel(userPerson);
                appData.setPersonIDToPersonModel(personIDToPerson);

                appData.setMaternalAncestorIDs(new ArrayList<String>());
                appData.setPaternalAncestorIDs(new ArrayList<String>());

                partitionParentalAncestors(userPerson.getFather(), userPerson.getMother());

                return getEventsFromServer();
                //return people;
            }
            else
            {
                return false;
                //return null;
                //responseStrings.add("@string/signInFailedText");
            }
        }
        catch(MalformedURLException ex)
        {
            Log.e("LoginTask", ex.getMessage(), ex);

            //responseStrings.add("@string/signInFailedText");
            //return null;
            return false;
        }
    }

    protected void onPostExecute(/*Pair<List<PersonModel>, List<PersonModel>>*/Boolean success)
    {
        context.onSyncComplete(success);
    }

    private void partitionParentalAncestors(String personIDFather, String personIDMother)
    {
        AppData appData = AppData.getInstance();

        if(personIDFather != null)
        {
            appData.getPaternalAncestorIDs().add(personIDFather);
            HashMap<String, PersonModel> personIDToPerson = appData.getPersonIDToPersonModel();
            PersonModel father = personIDToPerson.get(personIDFather);

            partitionParentalAncestors(father.getFather(), father.getMother());
        }

        if(personIDMother != null)
        {
            appData.getMaternalAncestorIDs().add(personIDMother);
            HashMap<String, PersonModel> personIDToPerson = appData.getPersonIDToPersonModel();
            PersonModel mother = personIDToPerson.get(personIDMother);

            partitionParentalAncestors(mother.getFather(), mother.getMother());
        }
    }

    private boolean getEventsFromServer()
    {
        ServerProxy serverProxy = new ServerProxy();

        AppData appData = AppData.getInstance();

        String serverHost = appData.getServerHost(); //strings[0];
        int serverPort = appData.getPort(); //Integer.valueOf(strings[1]);
        String eventRoute = "/event/";
        String authToken = appData.getAuthToken(); //strings[2];

        try
        {
            EventResponse eventResponse = serverProxy.getEvents(new URL("http", serverHost, serverPort, eventRoute), authToken);

            if(eventResponse == null)
            {
                return false;
            }

            String eventError = eventResponse.getMessage();

            if(eventError != null)
            {
                return false;
            }

            ArrayList<EventModel> events = eventResponse.getEvents();

            if(events == null)
            {
                return false;
            }

            //appData.setEventTypesToShow(new HashSet<String>());
            appData.setAllEventTypes(new HashSet<String>());
            appData.setPersonIDtoEvents(new HashMap<String, ArrayList<EventModel>>());
            //appData.setPersonIDToFilteredEvents(new HashMap<String, List<EventModel>>());

            //Set<String> eventTypesToShow = appData.getEventTypesToShow();
            HashSet<String> allEventTypes = appData.getAllEventTypes();
            HashMap PersonIDToEvents = appData.getPersonIDtoEvents();

            ArrayList<EventModel> eventList;

            //appData.setEventIDToEvent(new HashMap<String, EventModel>());

            //HashMap<String, EventModel> eventIDToEvents = appData.getEventIDToEvent();

            for (EventModel event : events)
            {
                if (!PersonIDToEvents.containsKey(event.getPerson()))
                {
                    eventList = new ArrayList<>();
                    eventList.add(event);
                    PersonIDToEvents.put(event.getPerson(), eventList);
                } else
                {
                    eventList = (ArrayList<EventModel>) PersonIDToEvents.get(event.getPerson());

                    eventList.add(event);
                }

                allEventTypes.add(event.getEventType().toLowerCase());

               // eventIDToEvents.put(event.getEventID(), event);
            }

            /*Set<String> personIDs = PersonIDToEvents.keySet();
            ArrayList<EventModel> eventsForPerson;

            Comparator eventComparator = new Comparator<EventModel>()
            {
                @Override
                public int compare(EventModel e1, EventModel e2)
                {
                    return ComparisonChain.start().compare(e1.getYear(), e2.getYear()).compare(e1.getEventType(), e2.getEventType()).result();
                }
            };

            for(String personID : personIDs)
            {
                eventsForPerson = (ArrayList<EventModel>) PersonIDToEvents.get(personID);

                //Collections.sort(eventsForPerson, Comparator.comparing(EventModel::getYear).thenComparing(EventModel::getEventType));
                Collections.sort(eventsForPerson, eventComparator);
            }*/

            appData.setEventTypeColor(new HashMap<String, Float>());
            HashMap<String, Float> eventTypeToFloat = appData.getEventTypeColor();

            Float rightLimit = 360F;
            Float generatedFloat;
            Random random = new Random();

            for(String eventType : allEventTypes)
            {
                generatedFloat = random.nextFloat() * rightLimit;

                eventTypeToFloat.put(eventType, generatedFloat);
            }

            appData.setEventTypesToShow(new HashSet<>(allEventTypes));
            appData.setPersonIDToFilteredEvents(new HashMap<>(PersonIDToEvents));
            //alter later to return dynamic value
            return true;
        }
        catch(MalformedURLException ex)
        {
            Log.e("EventTask", ex.getMessage(), ex);
            return false;
        }
    }
}
