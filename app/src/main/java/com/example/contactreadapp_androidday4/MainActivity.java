package com.example.contactreadapp_androidday4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView contactTV;
    Boolean isFirst = true;
    private String[] mColumnProjections = new String[]
            {
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.CONTACT_STATUS,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactTV = findViewById(R.id.contactTV);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void readContacts(View view)
    {
         // Request to Read the Permission
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS
        )!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1001);
            return;
        }
        if(isFirst)
        {
            LoaderManager.getInstance(this).initLoader(1,null,this);
        }
        else {
            LoaderManager.getInstance(this).restartLoader(1, null, this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PackageManager.PERMISSION_GRANTED != grantResults[0])
        {
            Toast.makeText(getApplicationContext(),"Access to Contact Required to Read",Toast.LENGTH_LONG).show();
        }
        else
        {
            readContacts(null);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,ContactsContract.Contacts.CONTENT_URI,mColumnProjections,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(data != null)
        {
            StringBuilder result = new StringBuilder();
            if(data.getCount()>0)
            {
                while(data.moveToNext())
                {
                    result.append(data.getString(0)+""+data.getString(1)+""+data.getString(2)+"\n");
                }
                contactTV.setText(result);
            }

        }
        else
        {
            contactTV.setText("No Data Found");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}