package ovh.exception.watchdogzz.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import ovh.exception.watchdogzz.R;
import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.data.UserManager;
import ovh.exception.watchdogzz.network.DownloadImageTask;
import ovh.exception.watchdogzz.network.IWSConsumer;
import ovh.exception.watchdogzz.network.NetworkManager;
import ovh.exception.watchdogzz.network.PostitionManager;
import ovh.exception.watchdogzz.network.WebServiceTask;
import ovh.exception.watchdogzz.view.WDRenderer;
import ovh.exception.watchdogzz.view.WDSurfaceView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IWSConsumer {

    private WDSurfaceView glView;
    private UserManager users;
    private PostitionManager postitionManager;
    private NetworkManager networkManager;

    @Override
    public void consume(JSONObject json) {
        if(json!=null)
            Snackbar.make(findViewById(R.id.content_main), json.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else
            Log.w("COUCOU", "JSON is null");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // doit etre avant tout appel au layout
        glView = (WDSurfaceView) findViewById(R.id.main_map);
        WDRenderer renderer = new WDRenderer(this);
        glView.setRenderer(renderer); // Use a custom renderer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Position: " + users.getMe().getPosition(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new WebServiceTask(MainActivity.this, MainActivity.this).execute("https://randomuser.me/api");
            }
        });




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.postitionManager = new PostitionManager(this);
        this.networkManager = NetworkManager.getInstance(this.getApplicationContext());
        setUsers(new UserManager());
        this.users.setMe((User) getIntent().getParcelableExtra("user"));
        getUsers().addObserver(renderer.getMap());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView t = (TextView) findViewById(R.id.complete_name);
                t.setText(users.getMe().getName());
                t = (TextView) findViewById(R.id.email_adresse);
                t.setText(users.getMe().getEmail());
                ImageView im = (ImageView) findViewById(R.id.photo_profil);
                new DownloadImageTask(im).execute(users.getMe().getPhotoUrl().toString());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
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
        getMenuInflater().inflate(R.menu.main, menu);
        TextView t = (TextView) findViewById(R.id.complete_name);
        t.setText(this.users.getMe().getName());
        t = (TextView) findViewById(R.id.email_adresse);
        t.setText(this.users.getMe().getEmail());
        ImageView im = (ImageView) findViewById(R.id.photo_profil);
        im.setImageURI(this.getUsers().getMe().getPhotoUrl());
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
        int id = item.getItemId();

        if (id == R.id.rv_mode) {
            // Handle the camera action
        } else if (id == R.id.normal_mode) {

        } else if (id == R.id.configuration) {

        } else if (id == R.id.share_position) {

        } else if (id == R.id.send_message) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public UserManager getUsers() {
        return users;
    }

    public void setUsers(UserManager users) {
        this.users = users;
    }
}
