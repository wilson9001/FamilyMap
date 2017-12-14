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

    public static final int COLOR_BLACK_ARGB = 0xff000000;
    public static final int COLOR_WHITE_ARGB = 0xffffffff;
    public static final int COLOR_GREEN_ARGB = 0xff388E3C;
    public static final int COLOR_PURPLE_ARGB = 0xff81C784;
    public static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    public static final int COLOR_BLUE_ARGB = 0xffF9A825;

    public static final String[] COLORS = {"Black", "White", "Green", "Purple", "Orange", "Blue"};
    public static final String[] MapTypes = {"Normal", "Hybrid", "Satellite", "Terrain"};

    public static final String fatherSideFilterTitle = "Father's Side";
    public static final String mothersideFilterTitle = "Mother's Side";
    public static final String maleEventsFilterTitle = "Male Events";
    public static final String femaleEventsFilterTitle = "Female Events";

    private int maptype;

    private String serverHost, authToken, userPersonID;
    private int port, spouseLineColor,familyTreeLineColor, lifeStoryLine;
    private PersonModel userPersonModel;
    private HashMap<String, ArrayList<EventModel>> personIDtoEvents, personIDToFilteredEvents;
    private HashMap<String, Float> eventTypeColor;
    private HashMap<String, PersonModel> personIDToPersonModel;
    private HashSet<String> eventTypesToShow, allEventTypes;
    private ArrayList<String> maternalAncestorIDs, paternalAncestorIDs;
    private boolean showPaternal, showMaternal, showMale, showFemale, showSpouseLines, showFamilyTreeLines, showLifeStoryLine;

    public int getMaptype()
    {
        return maptype;
    }

    public void setMaptype(int maptype)
    {
        this.maptype = maptype;
    }

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

    public int getSpouseLineColor()
    {
        return spouseLineColor;
    }

    public void setSpouseLineColor(int spouseLineColor)
    {
        this.spouseLineColor = spouseLineColor;
    }

    public int getFamilyTreeLineColor()
    {
        return familyTreeLineColor;
    }

    public void setFamilyTreeLineColor(int familyTreeLineColor)
    {
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public int getLifeStoryLine()
    {
        return lifeStoryLine;
    }

    public void setLifeStoryLine(int lifeStoryLine)
    {
        this.lifeStoryLine = lifeStoryLine;
    }

    public boolean isShowLifeStoryLine()
    {
        return showLifeStoryLine;
    }

    public void setShowLifeStoryLine(boolean showLifeStoryLine)
    {
        this.showLifeStoryLine = showLifeStoryLine;
    }

    public boolean isShowFamilyTreeLines()
    {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines)
    {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowSpouseLines()
    {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines)
    {
        this.showSpouseLines = showSpouseLines;
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
