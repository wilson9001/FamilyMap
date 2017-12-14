package a240.familymap.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import a240.familymap.Models.EventModel;
import a240.familymap.Models.PersonModel;
import a240.familymap.R;

public class SearchActivity extends AppCompatActivity
{
    private EditText searchEditText;
    private RecyclerView lifeEventsView, relativesView;
    private eventAdapter meventAdapter;
    private relativeAdapter mRelativeAdapter;
    private HashSet<String> eventTypesToShow;

    private String searchString;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lifeEventsView = findViewById(R.id.lifeEventsList);

        lifeEventsView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText = findViewById(R.id.searchActivitySearch);

        searchString = null;

        relativesView = findViewById(R.id.relativesList);

        relativesView.setLayoutManager(new LinearLayoutManager(this));

        updateUI(null);
    }

    public void updateUI(View v)
    {
        searchString = searchEditText.getText().toString();

        if (!searchString.isEmpty())
        {
            AppData appData = AppData.getInstance();

            HashMap<String, PersonModel> personIDToPersonModel = appData.getPersonIDToPersonModel();
            HashMap<String, ArrayList<EventModel>> personIDToEvents = appData.getPersonIDtoEvents();

            eventTypesToShow = appData.getEventTypesToShow();

            ArrayList<String> personIDs = new ArrayList<>();
            ArrayList<PersonModel> people = new ArrayList<>();
            ArrayList<EventModel> personEvents = new ArrayList<>();
            ArrayList<EventModel> Events = new ArrayList<>();

            if (eventTypesToShow.contains(AppData.fatherSideFilterTitle))
            {
                personIDs.addAll(appData.getPaternalAncestorIDs());
            }
            if (eventTypesToShow.contains(AppData.mothersideFilterTitle)) ;
            {
                personIDs.addAll(appData.getMaternalAncestorIDs());
            }

            PersonModel person;
            for (String id : personIDs)
            {
                person = personIDToPersonModel.get(id);
                if ((appData.isShowMale() && person.getGender().equals("m")) || (appData.isShowFemale() && person.getGender().equals("f")))
                {
                    people.add(person);
                }
            }


            for (PersonModel personIterator : people)
            {
                personEvents = personIDToEvents.get(personIterator.getPersonID());

                for (EventModel event : personEvents)
                {
                    if (eventTypesToShow.contains(event.getEventType().toLowerCase()))
                    {
                        Events.add(event);
                    }
                }
            }

            ArrayList<PersonModel> finalPeople = new ArrayList<>();
            ArrayList<EventModel> finalEvents = new ArrayList<>();

            for (PersonModel personIterator : people)
            {
                if (personIterator.getFirstName().toLowerCase().contains(searchString) || personIterator.getLastName().toLowerCase().contains(searchString))
                {
                    finalPeople.add(personIterator);
                }
            }

            for (EventModel event : Events)
            {
                if (event.getEventType().toLowerCase().contains(searchString) || event.getCountry().toLowerCase().contains(searchString) || event.getCity().toLowerCase().contains(searchString) || String.valueOf(event.getYear()).contains(searchString))
                {
                    finalEvents.add(event);
                }
            }

            meventAdapter = new eventAdapter(finalEvents);

            lifeEventsView.setAdapter(meventAdapter);

            mRelativeAdapter = new relativeAdapter(finalPeople);

            relativesView.setAdapter(mRelativeAdapter);
        }
    }

    private class eventHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView eventInfoText, personInfoText;
        private HashMap<String, PersonModel> personIdToPersonModel;
        private EventModel mEventModel;
        //private String firstName, lastName;
        //private PersonModel mainPerson;

        public eventHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_item_event, parent, false));

            itemView.setOnClickListener(this);

            eventInfoText = itemView.findViewById(R.id.ListEventInfo);
            personInfoText = itemView.findViewById(R.id.ListPersonView);

            AppData appData = AppData.getInstance();
            /*HashMap<String, PersonModel>*/ personIdToPersonModel = appData.getPersonIDToPersonModel();

            //mainPerson = personIdToPersonModel.get(personID);
        }

        @Override
        public void onClick(View view)
        {
            //Toast.makeText(getBaseContext(), mEventModel.getEventID(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SearchActivity.this, MapsActivity.class);
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

            PersonModel mainPerson = personIdToPersonModel.get(eventModel.getPerson());

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
        private TextView relativeName, /*relativeRelationShip,*/ genderIcon;

        private PersonModel relativeModel;//, mainPerson;

        public relativeHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_item_person, parent, false));

            itemView.setOnClickListener(this);

            relativeName = itemView.findViewById(R.id.relativeNameText);
            //relativeRelationShip = itemView.findViewById(R.id.relativeRelationShip);
            genderIcon = itemView.findViewById(R.id.relativeGenderIcon);

            //AppData appData = AppData.getInstance();
            //HashMap<String, PersonModel> personIdToPersonModel = appData.getPersonIDToPersonModel();

            //mainPerson = personIdToPersonModel.get(personID);
        }

        @Override
        public void onClick(View view)
        {
            //Toast.makeText(getBaseContext(), relativeModel.getPersonID(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
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

            //eventInfoBuilder.setLength(0);

            //PersonModel mainPerson = personIdToPersonModel.get(eventModel.getPerson());

            //String fatherID, motherID, spouseID;

            //fatherID = mainPerson.getFather();
            //motherID = mainPerson.getMother();
            //spouseID = mainPerson.getSpouse();

            if(relativeModel.getGender().equals("m") /*fatherID != null && fatherID.equals(personModel.getPersonID())*/)
            {
                //relativeRelationShip.setText("Father");
                genderIcon.setText("{fa-male 25dp}");
            }
            else //if(motherID != null && motherID.equals(personModel.getPersonID()))
            {
                //relativeRelationShip.setText("Mother");
                genderIcon.setText("{fa-female 25dp}");
            }
            /*else
            {
                relativeRelationShip.setText("Spouse");
                String spouseGenderIcon = personModel.getGender().equals("m") ? "{fa-male 25dp}" : "{fa-female 25dp}";
                genderIcon.setText(spouseGenderIcon);
            }*/
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
}
