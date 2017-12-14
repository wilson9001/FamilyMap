package a240.familymap.Activities;

import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import a240.familymap.R;
import a240.familymap.Tasks.SyncTask;

public class SettingsActivity extends AppCompatActivity implements SyncTask.Context // AdapterView.OnItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final AppData appData = AppData.getInstance();

        Spinner lifeStoryColor = findViewById(R.id.lifeStoryColor);
        Spinner familyTreeColor = findViewById(R.id.familyTreeColor);
        Spinner spouseLineColor = findViewById(R.id.spouseColor);
        Spinner mapType = findViewById(R.id.mapType);
        Switch lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        Switch familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        Switch spouselineSwitch = findViewById(R.id.spouseSwitch);


        ArrayAdapter<String> lifeStoryColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppData.COLORS);
        ArrayAdapter<String> familyTreeColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppData.COLORS);
        ArrayAdapter<String> spouseLineColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppData.COLORS);
        ArrayAdapter<String> mapTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AppData.MapTypes);

        lifeStoryColor.setAdapter(lifeStoryColorAdapter);
        familyTreeColor.setAdapter(familyTreeColorAdapter);
        spouseLineColor.setAdapter(spouseLineColorAdapter);
        mapType.setAdapter(mapTypeAdapter);

        //set initial value

        lifeStorySwitch.setChecked(appData.isShowLifeStoryLine());
        familyTreeSwitch.setChecked(appData.isShowFamilyTreeLines());
        spouselineSwitch.setChecked(appData.isShowSpouseLines());

        switch(appData.getLifeStoryLine())
        {
            case AppData.COLOR_BLACK_ARGB:
                lifeStoryColor.setSelection(0);
                break;
            case AppData.COLOR_WHITE_ARGB:
                lifeStoryColor.setSelection(1);
                break;
            case AppData.COLOR_GREEN_ARGB:
                lifeStoryColor.setSelection(2);
                break;
            case AppData.COLOR_PURPLE_ARGB:
                lifeStoryColor.setSelection(3);
                break;
            case AppData.COLOR_ORANGE_ARGB:
                lifeStoryColor.setSelection(4);
                break;
            case AppData.COLOR_BLUE_ARGB:
                lifeStoryColor.setSelection(5);
        }

        switch(appData.getFamilyTreeLineColor())
        {
            case AppData.COLOR_BLACK_ARGB:
                familyTreeColor.setSelection(0);
                break;
            case AppData.COLOR_WHITE_ARGB:
                familyTreeColor.setSelection(1);
                break;
            case AppData.COLOR_GREEN_ARGB:
                familyTreeColor.setSelection(2);
                break;
            case AppData.COLOR_PURPLE_ARGB:
                familyTreeColor.setSelection(3);
                break;
            case AppData.COLOR_ORANGE_ARGB:
                familyTreeColor.setSelection(4);
                break;
            case AppData.COLOR_BLUE_ARGB:
                familyTreeColor.setSelection(5);
        }

        switch (appData.getSpouseLineColor())
        {
            case AppData.COLOR_BLACK_ARGB:
                spouseLineColor.setSelection(0);
                break;
            case AppData.COLOR_WHITE_ARGB:
                spouseLineColor.setSelection(1);
                break;
            case AppData.COLOR_GREEN_ARGB:
                spouseLineColor.setSelection(2);
                break;
            case AppData.COLOR_PURPLE_ARGB:
                spouseLineColor.setSelection(3);
                break;
            case AppData.COLOR_ORANGE_ARGB:
                spouseLineColor.setSelection(4);
                break;
            case AppData.COLOR_BLUE_ARGB:
                spouseLineColor.setSelection(5);
        }

        switch(appData.getMaptype())
        {
            case GoogleMap.MAP_TYPE_NORMAL:
                mapType.setSelection(0);
                break;
            case GoogleMap.MAP_TYPE_HYBRID:
                mapType.setSelection(1);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mapType.setSelection(2);
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                mapType.setSelection(3);
        }

        //set listeners

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                AppData appData = AppData.getInstance();

                appData.setShowFamilyTreeLines(b);

            }
        });

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                AppData appData = AppData.getInstance();

                appData.setShowLifeStoryLine(b);
            }
        });

        spouselineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                AppData appData = AppData.getInstance();

                appData.setShowSpouseLines(b);
            }
        });

        lifeStoryColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:
                        appData.setLifeStoryLine(AppData.COLOR_BLACK_ARGB);
                        break;
                    case 1:
                        appData.setLifeStoryLine(AppData.COLOR_WHITE_ARGB);
                        break;
                    case 2:
                        appData.setLifeStoryLine(AppData.COLOR_GREEN_ARGB);
                        break;
                    case 3:
                        appData.setLifeStoryLine(AppData.COLOR_PURPLE_ARGB);
                        break;
                    case 4:
                        appData.setLifeStoryLine(AppData.COLOR_ORANGE_ARGB);
                        break;
                    case 5:
                        appData.setLifeStoryLine(AppData.COLOR_BLUE_ARGB);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        familyTreeColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:
                        appData.setFamilyTreeLineColor(AppData.COLOR_BLACK_ARGB);
                        break;
                    case 1:
                        appData.setFamilyTreeLineColor(AppData.COLOR_WHITE_ARGB);
                        break;
                    case 2:
                        appData.setFamilyTreeLineColor(AppData.COLOR_GREEN_ARGB);
                        break;
                    case 3:
                        appData.setFamilyTreeLineColor(AppData.COLOR_PURPLE_ARGB);
                        break;
                    case 4:
                        appData.setFamilyTreeLineColor(AppData.COLOR_ORANGE_ARGB);
                        break;
                    case 5:
                        appData.setFamilyTreeLineColor(AppData.COLOR_BLUE_ARGB);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        spouseLineColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:
                        appData.setSpouseLineColor(AppData.COLOR_BLACK_ARGB);
                        break;
                    case 1:
                        appData.setSpouseLineColor(AppData.COLOR_WHITE_ARGB);
                        break;
                    case 2:
                        appData.setSpouseLineColor(AppData.COLOR_GREEN_ARGB);
                        break;
                    case 3:
                        appData.setSpouseLineColor(AppData.COLOR_PURPLE_ARGB);
                        break;
                    case 4:
                        appData.setSpouseLineColor(AppData.COLOR_ORANGE_ARGB);
                        break;
                    case 5:
                        appData.setSpouseLineColor(AppData.COLOR_BLUE_ARGB);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        mapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch(i)
                {
                    case 0:
                        appData.setMaptype(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        appData.setMaptype(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 2:
                        appData.setMaptype(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        appData.setMaptype(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }

            //TODO: Resolve bug when going from personActivity to mapActivity after transferring to new person through personActivity.
            //TODO: Redraw lines automatically when going back to main activity.
            //TODO: Implement filter and then search Activities.
            //TODO: Create up button
            //TODO: Implement resync and Logout

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    public void resync(View v)
    {
        Toast.makeText(this, "Resync started", Toast.LENGTH_LONG).show();

        SyncTask syncTask = new SyncTask(this);

        syncTask.execute(false);
    }

    public void logout(View v)
    {
        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();

        AppData appData = AppData.getInstance();

        appData.setServerHost(null);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSyncComplete(Boolean success)
    {
        if(success)
        {
            Toast.makeText(this, "Sync complete", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Sync failed!", Toast.LENGTH_LONG).show();
        }
    }
}
