package ru.artsec.cch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import ru.artsec.cch.fragments.EventFragment;
import ru.artsec.cch.fragments.SettingsFragment;
import ru.artsec.cch.fragments.TicketFragment;
import ru.artsec.cch.fragments.TicketSearchFragment;
import ru.artsec.cch.model.PairTicketProps;

public class Nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog pd;
    private static String ticketID;
    public static ArrayList<PairTicketProps> main;

    public static boolean isIsSearchViaCam() {
        return isSearchViaCam;
    }

    public static void setIsSearchViaCam(boolean isSearchViaCam) {
        Nav.isSearchViaCam = isSearchViaCam;
    }

    private static boolean isSearchViaCam = false;

    private static boolean isEventActive = false;
    public static ArrayList<android.app.Fragment> fList;

    public static boolean isEventActive() {
        return isEventActive;
    }

    public void setEventActive(boolean eventActive) {
        isEventActive = eventActive;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        main = new ArrayList<PairTicketProps>();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_active_events:
                setEventActive(true);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame
                                , new EventFragment())
                        .commit();
                break;
            case R.id.nav_disabled_events:
                setEventActive(false);
                getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame
                            , new EventFragment())
                    .commit();
                break;
            case R.id.nav_search:
                setIsSearchViaCam(false);
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame
                                , new TicketSearchFragment())
                        .commit();
                break;
            case R.id.nav_qr:
                setIsSearchViaCam(true);
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
            case R.id.nav_settings:
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame
                                , new SettingsFragment())
                        .commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AsyncLoad extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... args) {
            main = ServerProvider.getTicketProps(Nav.this, ticketID);
            return null;//returns what you want to pass to the onPostExecute()
        }

        protected void onPostExecute(String result) {
            if (main.get(0).getTicketValues().size() > 0){
                Toast.makeText(Nav.this, "Найден билет с идентификатором: " + main.get(0).getTicketValues().get(0).getKeyValue(), Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame
                                , new TicketFragment())
                        .commit();
            } else {
                Toast.makeText(Nav.this, "Билет с такими данными не найден", Toast.LENGTH_SHORT).show();
            }
            Nav.this.pd.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Сканирование отмененно", Toast.LENGTH_LONG).show();
            }
            else {
                try{
                    ticketID = result.getContents().replaceAll("\\s+","");
                    this.pd = ProgressDialog.show(this, "Поиск билета..", "Пожалуйста, подождите...", true, false);
                    new Nav.AsyncLoad().execute();
                } catch (Exception e) {
                    Log.wtf("MYTAG", String.valueOf(e));
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
