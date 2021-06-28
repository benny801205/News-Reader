package androidsamples.java.journalapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements EntryListFragment.Callbacks {
    private static final String TAG = "MainActivity";
     static final String KEY_ENTRY_ID = "KEY_ENTRY_ID";
    public static int REQUEST_INTERNET=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check_permission();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            Fragment fragment = new EntryListFragment(); //EntryDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable("sort","Title");
            //args.putSerializable("FavoriteOnly","");

            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }



    @Override
    public void onEntrySelected(UUID entryId) {
        Log.d(TAG, "Entry selected: " + entryId);
        //doesnt call this method

    }

    public void check_permission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.d(TAG, "going to ask permission");

            ActivityCompat.
                    requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET,
                    }, REQUEST_INTERNET);
        }


    }


}