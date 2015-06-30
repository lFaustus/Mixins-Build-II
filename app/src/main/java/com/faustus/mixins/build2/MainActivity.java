package com.faustus.mixins.build2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.faustus.mixins.build2.fragments.CreateLiquor;
import com.faustus.mixins.build2.fragments.MainMenu;
import com.faustus.mixins.build2.fragments.MixOnTheSpot;


public class MainActivity extends ActionBarActivity implements OnFragmentChangeListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
            getFragmentManager().beginTransaction().add(R.id.root_view, MainMenu.newInstance("MainMenu")).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnFragmentChange(Fragments fragment) {

        if(fragment == Fragments.CREATELIQUOR)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.root_view,CreateLiquor.newInstance(fragment.getTAG()))
                    .addToBackStack(null).commit();

        }
        else if(fragment == Fragments.MIXONTHESPOT)
        {
            getFragmentManager().beginTransaction()
                    .replace(R.id.root_view, MixOnTheSpot.newInstance(fragment.getTAG()))
                    .addToBackStack(null).commit();
        }

    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount() > 0)
        {
            getFragmentManager().popBackStack();
            return;
        }

        super.onBackPressed();
        finish();
    }
  }
