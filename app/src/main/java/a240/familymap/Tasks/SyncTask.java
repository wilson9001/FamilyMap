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
import java.util.List;
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

                String paternalID = userPerson.getFather();
                String maternalID = userPerson.getMother();

                if(paternalID != null)
                {
                    appData.getPaternalAncestorIDs().add(paternalID);

                    PersonModel father = personIDToPerson.get(paternalID);

                    partitionParentalAncestors(father.getFather(), father.getMother(), true);
                }
                if(maternalID != null)
                {
                    appData.getMaternalAncestorIDs().add(maternalID);

                    PersonModel mother = personIDToPerson.get(maternalID);

                    partitionParentalAncestors(mother.getFather(), mother.getMother(), false);
                }

                //partitionParentalAncestors(userPerson.getFather(), userPerson.getMother());

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

    private void partitionParentalAncestors(String personIDFather, String personIDMother, boolean fatherSide)
    {
        AppData appData = AppData.getInstance();

        if(personIDFather != null)
        {
            if(fatherSide)
            {
                appData.getPaternalAncestorIDs().add(personIDFather);
            }
            else
            {
                appData.getMaternalAncestorIDs().add(personIDFather);
            }

            HashMap<String, PersonModel> personIDToPerson = appData.getPersonIDToPersonModel();
            PersonModel father = personIDToPerson.get(personIDFather);

            partitionParentalAncestors(father.getFather(), father.getMother(), fatherSide);
        }

        if(personIDMother != null)
        {
            if(fatherSide)
            {
                appData.getPaternalAncestorIDs().add(personIDMother);
            }
            else
            {
                appData.getMaternalAncestorIDs().add(personIDMother);
            }

            HashMap<String, PersonModel> personIDToPerson = appData.getPersonIDToPersonModel();
            PersonModel mother = personIDToPerson.get(personIDMother);

            partitionParentalAncestors(mother.getFather(), mother.getMother(), fatherSide);
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

            appData.setAllEventTypes(new HashSet<String>());
            appData.setPersonIDtoEvents(new HashMap<String, ArrayList<EventModel>>());

            HashSet<String> allEventTypes = appData.getAllEventTypes();
            HashMap PersonIDToEvents = appData.getPersonIDtoEvents();

            ArrayList<EventModel> eventList;

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
            }

            allEventTypes.add(AppData.fatherSideFilterTitle);
            allEventTypes.add(AppData.mothersideFilterTitle);
            allEventTypes.add(AppData.maleEventsFilterTitle);
            allEventTypes.add(AppData.femaleEventsFilterTitle);

            Set<String> personIDSet = PersonIDToEvents.keySet();
            ArrayList<EventModel> eventsForPerson;
            ArrayList<EventModel> birthAndDeath;
            //String eventType;
            EventModel event;

            for(String personID : personIDSet)
            {
                eventsForPerson = (ArrayList<EventModel>) PersonIDToEvents.get(personID);
                String eventType;

                birthAndDeath = new ArrayList<>();

                for(int i = 0; i < eventsForPerson.size(); i++/*EventModel event : eventsForPerson*/)
                {
                    event = eventsForPerson.get(i);

                    eventType = event.getEventType().toLowerCase();
                    if(eventType.equals("birth") || eventType.equals("death"))
                    {
                        birthAndDeath.add(event);
                        eventsForPerson.remove(i);
                    }
                }

                Collections.sort(eventsForPerson, new eventComparator());

                for(EventModel birthOrDeath : birthAndDeath)
                {
                    eventType = birthOrDeath.getEventType().toLowerCase();
                    if(eventType.equals("birth"))
                    {
                        eventsForPerson.add(0, birthOrDeath);
                    }
                    else
                    {
                        eventsForPerson.add(birthOrDeath);
                    }
                }
            }

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

            return true;
        }
        catch(MalformedURLException ex)
        {
            Log.e("EventTask", ex.getMessage(), ex);
            return false;
        }
    }

    public class eventComparator implements Comparator<EventModel>
    {
        public int compare(EventModel e1, EventModel e2)
        {
            int yearValue = e1.getYear() - e2.getYear();

            if(yearValue != 0)
            {
                return yearValue;
            }

            int nameValue = e1.getEventType().toLowerCase().compareTo(e2.getEventType().toLowerCase());

            return nameValue;
        }
    }
}
