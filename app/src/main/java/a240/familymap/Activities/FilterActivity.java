package a240.familymap.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import a240.familymap.R;

public class FilterActivity extends AppCompatActivity
{
    private RecyclerView filterView;
    private filterAdapter mFilterAdapter;

    private String fatherSideSubtitle = "FILTER BY FATHER'S SIDE OF FAMILY";
    private String motherSideSubtitle = "FILTER BY MOTHER'S SIDE OF FAMILY";
    private String genderFilterSubtitle = "FILTER EVENTS BASED ON GENDER";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterView = findViewById(R.id.filterRecyclerView);

        filterView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
    }

    private void updateUI()
    {
        AppData appData = AppData.getInstance();

        HashSet<String> allEventTypes = appData.getAllEventTypes();

        ArrayList<String> allEventTypesArray = new ArrayList<>();

        for(String eventType : allEventTypes)
        {
            if(!eventType.equals(AppData.fatherSideFilterTitle) && !eventType.equals(AppData.mothersideFilterTitle) && !eventType.equals(AppData.maleEventsFilterTitle) && !eventType.equals(AppData.femaleEventsFilterTitle))
            {
                allEventTypesArray.add(eventType);
            }
        }

        allEventTypesArray.add(AppData.fatherSideFilterTitle);
        allEventTypesArray.add(AppData.mothersideFilterTitle);
        allEventTypesArray.add(AppData.maleEventsFilterTitle);
        allEventTypesArray.add(AppData.femaleEventsFilterTitle);

        mFilterAdapter = new filterAdapter(allEventTypesArray);

        filterView.setAdapter(mFilterAdapter);
    }

    private class filterHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener //implements View.OnClickListener
    {
        private TextView filterTitle, filterSubtitle;
        private HashSet<String> eventTypesToShow;
        private String mEventType;
        private Switch filterSwitch;

        public filterHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_item_filter, parent, false));

            //itemView.setOnClickListener(this);

            filterTitle = itemView.findViewById(R.id.FilterEventTitle);
            filterSubtitle = itemView.findViewById(R.id.FilterEventSubtitle);
            filterSwitch = itemView.findViewById(R.id.filterSwitch);

            AppData appData = AppData.getInstance();

            //allEventTypes = appData.getAllEventTypes();
            eventTypesToShow = appData.getEventTypesToShow();

            filterSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            if(b)
            {
                Toast.makeText(getBaseContext(), "Filter " + mEventType + " activated", Toast.LENGTH_SHORT).show();
               // filterSwitch.setChecked(true);
                eventTypesToShow.add(mEventType);
            }
            else
            {
                Toast.makeText(getBaseContext(), "Filter " + mEventType + " deactivated", Toast.LENGTH_SHORT).show();
                //filterSwitch.setChecked(false);
                eventTypesToShow.remove(mEventType);
            }
        }
        /*
        @Override
        public void onClick(View v)
        {
            if(filterSwitch.isChecked())
            {
                Toast.makeText(getBaseContext(), "Filter deactivated", Toast.LENGTH_SHORT).show();
                filterSwitch.setChecked(false);
                eventTypesToShow.remove(mEventType);
            }
            else
            {
                Toast.makeText(getBaseContext(), "Filter activated", Toast.LENGTH_SHORT).show();
                filterSwitch.setChecked(true);
                eventTypesToShow.add(mEventType);
            }
        }*/

        public void bind(String eventType)
        {
            mEventType = eventType;

            //filterTitle.setText(eventType);
            if(eventTypesToShow.contains(eventType))
            {
                filterSwitch.setChecked(true);
            }
            else
            {
                filterSwitch.setChecked(false);
            }

            if(eventType.equals(AppData.fatherSideFilterTitle))
            {
                filterTitle.setText(AppData.fatherSideFilterTitle);
                filterSubtitle.setText(fatherSideSubtitle);
            }
            else if(eventType.equals(AppData.mothersideFilterTitle))
            {
                filterTitle.setText(AppData.mothersideFilterTitle);
                filterSubtitle.setText(motherSideSubtitle);
            }
            else if(eventType.equals(AppData.maleEventsFilterTitle) || eventType.equals(AppData.femaleEventsFilterTitle))
            {
                filterSubtitle.setText(genderFilterSubtitle);
                if(eventType.equals(AppData.maleEventsFilterTitle))
                {
                    filterTitle.setText(AppData.maleEventsFilterTitle);
                }
                else
                {
                    filterTitle.setText(AppData.femaleEventsFilterTitle);
                }
            }
            else
            {
                String filterTitleString = eventType.substring(0, 1).toUpperCase() + eventType.substring(1) + " Events";
                String filterSubtitleString = "FILTER BY " + eventType.toUpperCase() + " EVENTS";

                filterTitle.setText(filterTitleString);
                filterSubtitle.setText(filterSubtitleString);
            }
        }
    }

    private class filterAdapter extends RecyclerView.Adapter<filterHolder>
    {
        private ArrayList<String> allEventTypes;

        public filterAdapter(ArrayList<String> allEventTypes)
        {
            this.allEventTypes = allEventTypes;
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
        public filterHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());

            return new filterHolder(layoutInflater, parent);
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
        public void onBindViewHolder(filterHolder holder, int position)
        {
            String eventType = allEventTypes.get(position);

            holder.bind(eventType);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount()
        {
            return allEventTypes.size();
        }
    }
}
