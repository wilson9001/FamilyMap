package a240.familymap.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.R;

public class PersonActivity extends AppCompatActivity
{
    private RecyclerView lifeEventsView, relativesView;
    private eventAdapter mEventAdapter;
    private relativeAdapter mRelativeAdapter;
    //private TextView expandableEventsText, expandableRelativesText;
    private String personID;
    //private ArrayList<EventModel> Events;

    HashSet<String> eventTypesToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        lifeEventsView = findViewById(R.id.lifeEventsList);

        lifeEventsView.setLayoutManager(new LinearLayoutManager(this));

        personID = getIntent().getStringExtra(getString(R.string.personIDForIntent));

        relativesView = findViewById(R.id.relativesList);

        relativesView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        //expandableEventsText = findViewById(R.id.expand_events_header);

       //expandableRelativesText = findViewById(R.id.expand_relatives_header);
    }

    public void toggleEventsListVisibility(View v)
    {
        int eventsListVisibility = lifeEventsView.getVisibility();
        TextView eventListHeader = (TextView) v /*findViewById(R.id.expand_events_header)*/;

        switch (eventsListVisibility)
        {
            case View.GONE:
                lifeEventsView.setVisibility(View.VISIBLE);
                eventListHeader.setText("{fa-compress} Life Events");
                break;
            default:
                lifeEventsView.setVisibility(View.GONE);
                eventListHeader.setText("{fa-expand} Life Events");
        }
    }

    public void toggleRelativeListVisibility(View v)
    {
        int relativeListVisibility = relativesView.getVisibility();
        TextView relativeListHeader = (TextView) v/*findViewById(R.id.expand_relatives_header)*/;

        switch(relativeListVisibility)
        {
            case View.GONE:
                relativesView.setVisibility(View.VISIBLE);
                relativeListHeader.setText("{fa-compress} Relatives");
                break;
            case View.VISIBLE:
                relativesView.setVisibility(View.GONE);
                relativeListHeader.setText("{fa-expand} Relatives");
                break;
            default:

        }
    }

    private void updateUI()
    {
        AppData appData = AppData.getInstance();

        //HashMap<String, ArrayList<EventModel>> personIDToFilteredEvents = appData.getPersonIDToFilteredEvents();
        HashMap<String, ArrayList<EventModel>> personIDToEvents = appData.getPersonIDtoEvents();
        HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

        eventTypesToShow = appData.getEventTypesToShow();

        //ArrayList<EventModel> Events = personIDToFilteredEvents.get(personID);
        ArrayList<EventModel> unFilteredEvents = personIDToEvents.get(personID);
        ArrayList<EventModel> Events = new ArrayList<>();

        PersonModel mainPerson = personIdToPersonModel.get(personID);

        if((eventTypesToShow.contains(AppData.fatherSideFilterTitle) && appData.getPaternalAncestorIDs().contains(personID)) || (eventTypesToShow.contains(AppData.mothersideFilterTitle) && appData.getMaternalAncestorIDs().contains(personID)))
        {
            if((appData.isShowMale() && mainPerson.getGender().equals("m")) || (appData.isShowFemale() && mainPerson.getGender().equals("f")))
            {
                for(EventModel eventsIterator : unFilteredEvents)
                {
                    if(eventTypesToShow.contains(eventsIterator.getEventType().toLowerCase()))
                    {
                        Events.add(eventsIterator);
                    }
                }
            }
        }

        ArrayList<PersonModel> relatives = new ArrayList<>();

        String fatherID, motherID, spouseID;

        fatherID = mainPerson.getFather();
        motherID = mainPerson.getMother();
        spouseID = mainPerson.getSpouse();

        if(fatherID != null)
        {
            PersonModel father = personIdToPersonModel.get(fatherID);
            relatives.add(father);
        }
        if(motherID != null)
        {
            PersonModel mother = personIdToPersonModel.get(motherID);
            relatives.add(mother);
        }
        if(spouseID != null)
        {
            PersonModel spouse = personIdToPersonModel.get(spouseID);
            relatives.add(spouse);
        }

        /*if(Events == null)
        {
            Events = new ArrayList<>();
        }*/

        //HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

        TextView firstNameView = /*lifeEventsView.*/findViewById(R.id.personFirstName);
        TextView lastNameView = /*lifeEventsView.*/findViewById(R.id.personLaststName);
        TextView gender = /*lifeEventsView.*/findViewById(R.id.personGender);

        //PersonModel mainPerson = personIdToPersonModel.get(personID);

        firstNameView.setText(mainPerson.getFirstName());
        lastNameView.setText(mainPerson.getLastName());

        if(mainPerson.getGender().equals("m"))
        {
            gender.setText("Male");
        }
        else
        {
            gender.setText("Female");
        }

        mEventAdapter = new eventAdapter(Events);

        lifeEventsView.setAdapter(mEventAdapter);

        mRelativeAdapter = new relativeAdapter(relatives);

        relativesView.setAdapter(mRelativeAdapter);
    }

    private class eventHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView eventInfoText, personInfoText;
        //private HashMap<String, PersonModel> personIdToPersonModel;
        private EventModel mEventModel;
        //private String firstName, lastName;
        private PersonModel mainPerson;

        public eventHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_item_event, parent, false));

            itemView.setOnClickListener(this);

            eventInfoText = itemView.findViewById(R.id.ListEventInfo);
            personInfoText = itemView.findViewById(R.id.ListPersonView);

            AppData appData = AppData.getInstance();
            HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

            mainPerson = personIdToPersonModel.get(personID);
        }

        @Override
        public void onClick(View view)
        {
            //Toast.makeText(getBaseContext(), mEventModel.getEventID(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PersonActivity.this, MapsActivity.class);
            intent.putExtra(getString(R.string.eventIDForMapActivity), mEventModel.getEventID());
            intent.putExtra(getString(R.string.personIDForIntent), mEventModel.getPerson());

            startActivity(intent);
        }

        public void bind(EventModel eventModel)
        {
            mEventModel = eventModel;

            StringBuilder eventInfoBuilder = new StringBuilder();

            eventInfoBuilder.append(eventModel.getEventType());
            eventInfoBuilder.append(": ");
            eventInfoBuilder.append(eventModel.getCity());
            eventInfoBuilder.append(", ");
            eventInfoBuilder.append(eventModel.getCountry());
            eventInfoBuilder.append(" (");
            eventInfoBuilder.append(eventModel.getYear());
            eventInfoBuilder.append(")");

            eventInfoText.setText(eventInfoBuilder.toString());

            eventInfoBuilder.setLength(0);

            //PersonModel mainPerson = personIdToPersonModel.get(eventModel.getPerson());

            eventInfoBuilder.append(mainPerson.getFirstName());
            eventInfoBuilder.append(" ");
            eventInfoBuilder.append(mainPerson.getLastName());

            personInfoText.setText(eventInfoBuilder);
        }
    }

    private class eventAdapter extends RecyclerView.Adapter<eventHolder>
    {
        private List<EventModel> events;

        public eventAdapter(List<EventModel> events)
        {
            this.events = events;
        }

        /**
         * Called when RecyclerView needs a new {link ViewHolder} of the given type to represent
         * an item.
         * <p>
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         * <p>
         * The new ViewHolder will be used to display items of the adapter using
         * {link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #getItemViewType(int)
         * see #onBindViewHolder(ViewHolder, int)
         */
        @Override
        public eventHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());

            return new eventHolder(layoutInflater, parent);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {link ViewHolder#itemView} to reflect the item at the given
         * position.
         * <p>
         * Note that unlike {@link ListView}, RecyclerView will not call this method
         * again if the position of the item changes in the data set unless the item itself is
         * invalidated or the new position cannot be determined. For this reason, you should only
         * use the <code>position</code> parameter while acquiring the related data item inside
         * this method and should not keep a copy of it. If you need the position of an item later
         * on (e.g. in a click listener), use {link ViewHolder#getAdapterPosition()} which will
         * have the updated adapter position.
         * <p>
         * Override {link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
         * handle efficient partial bind.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(eventHolder holder, int position)
        {
            EventModel event = events.get(position);

            holder.bind(event);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount()
        {
            return events.size();
        }
    }

    private class relativeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView relativeName, relativeRelationShip, genderIcon;

        private PersonModel relativeModel, mainPerson;

        public relativeHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_item_person, parent, false));

            itemView.setOnClickListener(this);

            relativeName = itemView.findViewById(R.id.relativeNameText);
            relativeRelationShip = itemView.findViewById(R.id.relativeRelationShip);
            genderIcon = itemView.findViewById(R.id.relativeGenderIcon);

            AppData appData = AppData.getInstance();
            HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

            mainPerson = personIdToPersonModel.get(personID);
        }

        @Override
        public void onClick(View view)
        {
            //Toast.makeText(getBaseContext(), relativeModel.getPersonID(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
            intent.putExtra(getString(R.string.personIDForIntent), relativeModel.getPersonID());

            startActivity(intent);
        }

        public void bind(PersonModel personModel)
        {
            relativeModel = personModel;

            StringBuilder eventInfoBuilder = new StringBuilder();

            eventInfoBuilder.append(personModel.getFirstName());
            eventInfoBuilder.append(" ");
            eventInfoBuilder.append(personModel.getLastName());

            relativeName.setText(eventInfoBuilder.toString());

            eventInfoBuilder.setLength(0);

            //PersonModel mainPerson = personIdToPersonModel.get(eventModel.getPerson());

            String fatherID, motherID, spouseID;

            fatherID = mainPerson.getFather();
            motherID = mainPerson.getMother();
            spouseID = mainPerson.getSpouse();

            if(fatherID != null && fatherID.equals(personModel.getPersonID()))
            {
                relativeRelationShip.setText("Father");
                genderIcon.setText("{fa-male 25dp}");
            }
            else if(motherID != null && motherID.equals(personModel.getPersonID()))
            {
                relativeRelationShip.setText("Mother");
                genderIcon.setText("{fa-female 25dp}");
            }
            else
            {
                relativeRelationShip.setText("Spouse");
                String spouseGenderIcon = personModel.getGender().equals("m") ? "{fa-male 25dp}" : "{fa-female 25dp}";
                genderIcon.setText(spouseGenderIcon);
            }

        }
    }

    private class relativeAdapter extends RecyclerView.Adapter<relativeHolder>
    {
        private List<PersonModel> people;

        public relativeAdapter(List<PersonModel> people)
        {
            this.people = people;
        }

        /**
         * Called when RecyclerView needs a new {link ViewHolder} of the given type to represent
         * an item.
         * <p>
         * This new ViewHolder should be constructed with a new View that can represent the items
         * of the given type. You can either create a new View manually or inflate it from an XML
         * layout file.
         * <p>
         * The new ViewHolder will be used to display items of the adapter using
         * {link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #getItemViewType(int)
         * see #onBindViewHolder(ViewHolder, int)
         */
        @Override
        public relativeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());

            return new relativeHolder(layoutInflater, parent);
        }

        /**
         * Called by RecyclerView to display the data at the specified position. This method should
         * update the contents of the {link ViewHolder#itemView} to reflect the item at the given
         * position.
         * <p>
         * Note that unlike {@link ListView}, RecyclerView will not call this method
         * again if the position of the item changes in the data set unless the item itself is
         * invalidated or the new position cannot be determined. For this reason, you should only
         * use the <code>position</code> parameter while acquiring the related data item inside
         * this method and should not keep a copy of it. If you need the position of an item later
         * on (e.g. in a click listener), use {link ViewHolder#getAdapterPosition()} which will
         * have the updated adapter position.
         * <p>
         * Override {link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
         * handle efficient partial bind.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(relativeHolder holder, int position)
        {
            PersonModel person = people.get(position);

            holder.bind(person);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount()
        {
            return people.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}
