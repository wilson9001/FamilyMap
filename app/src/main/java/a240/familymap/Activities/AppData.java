package a240.familymap.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
//import a240.familymap.Models.UserModel;

/**
 * Created by Alex on 12/2/2017.
 */

public class AppData
{
    private static final AppData ourInstance = new AppData();

    public static AppData getInstance()
    {
        return ourInstance;
    }

    private AppData()
    {
    }

    private String serverHost, authToken, userPersonID;
    private int port;
    private PersonModel userPersonModel;
    //private UserModel userModel;
    private HashMap<String, ArrayList<EventModel>> personIDtoEvents, personIDToFilteredEvents;
    private HashMap<String, Float> eventTypeColor;
    private HashMap<String, PersonModel> personIDToPersonModel;
   // private HashMap<String, EventModel> eventIDToEvent;
    private HashSet<String> eventTypesToShow, allEventTypes;
    private ArrayList<String> maternalAncestorIDs, paternalAncestorIDs;
    private boolean showPaternal, showMaternal, showMale, showFemale;

    public String getServerHost()
    {
        return serverHost;
    }

    public void setServerHost(String serverHost)
    {
        this.serverHost = serverHost;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

   /* public UserModel getUserModel()
    {
        return userModel;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }*/

    public HashMap<String, ArrayList<EventModel>> getPersonIDtoEvents()
    {
        return personIDtoEvents;
    }

    public void setPersonIDtoEvents(HashMap<String, ArrayList<EventModel>> personIDtoEvents)
    {
        this.personIDtoEvents = personIDtoEvents;
    }

    public HashMap<String, ArrayList<EventModel>> getPersonIDToFilteredEvents()
    {
        return personIDToFilteredEvents;
    }

    public void setPersonIDToFilteredEvents(HashMap<String, ArrayList<EventModel>> personIDToFilteredEvents)
    {
        this.personIDToFilteredEvents = personIDToFilteredEvents;
    }

    public ArrayList<String> getMaternalAncestorIDs()
    {
        return maternalAncestorIDs;
    }

    public void setMaternalAncestorIDs(ArrayList<String> maternalAncestors)
    {
        this.maternalAncestorIDs = maternalAncestors;
    }

    public ArrayList<String> getPaternalAncestorIDs()
    {
        return paternalAncestorIDs;
    }

    public void setPaternalAncestorIDs(ArrayList<String> paternalAncestors)
    {
        this.paternalAncestorIDs = paternalAncestors;
    }

    public boolean isShowPaternal()
    {
        return showPaternal;
    }

    public void setShowPaternal(boolean showPaternal)
    {
        this.showPaternal = showPaternal;
    }

    public boolean isShowMaternal()
    {
        return showMaternal;
    }

    public void setShowMaternal(boolean showMaternal)
    {
        this.showMaternal = showMaternal;
    }

    public boolean isShowMale()
    {
        return showMale;
    }

    public void setShowMale(boolean showMale)
    {
        this.showMale = showMale;
    }

    public boolean isShowFemale()
    {
        return showFemale;
    }

    public void setShowFemale(boolean showFemale)
    {
        this.showFemale = showFemale;
    }

    public HashSet<String> getEventTypesToShow()
    {
        return eventTypesToShow;
    }

    public void setEventTypesToShow(HashSet<String> eventTypesToShow)
    {
        this.eventTypesToShow = eventTypesToShow;
    }

    public HashSet<String> getAllEventTypes()
    {
        return allEventTypes;
    }

    public void setAllEventTypes(HashSet<String> allEventTypes)
    {
        this.allEventTypes = allEventTypes;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public String getUserPersonID()
    {
        return userPersonID;
    }

    public void setUserPersonID(String userPersonID)
    {
        this.userPersonID = userPersonID;
    }

    public PersonModel getUserPersonModel()
    {
        return userPersonModel;
    }

    public void setUserPersonModel(PersonModel userPersonModel)
    {
        this.userPersonModel = userPersonModel;
    }

    public HashMap<String, PersonModel> getPersonIDToPersonModel()
    {
        return personIDToPersonModel;
    }

    public void setPersonIDToPersonModel(HashMap<String, PersonModel> personIDToPersonModel)
    {
        this.personIDToPersonModel = personIDToPersonModel;
    }

    public HashMap<String, Float> getEventTypeColor()
    {
        return eventTypeColor;
    }

    public void setEventTypeColor(HashMap<String, Float> eventTypeColor)
    {
        this.eventTypeColor = eventTypeColor;
    }

   /* public HashMap<String, EventModel> getEventIDToEvent()
    {
        return eventIDToEvent;
    }

    public void setEventIDToEvent(HashMap<String, EventModel> eventIDToEvent)
    {
        this.eventIDToEvent = eventIDToEvent;
    }*/
}
