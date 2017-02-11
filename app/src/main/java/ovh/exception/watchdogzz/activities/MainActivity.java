package ovh.exception.watchdogzz.activities;

import android.content.Intent;
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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ovh.exception.watchdogzz.R;
import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.JUser;
import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.data.UserManager;
import ovh.exception.watchdogzz.network.DownloadImageTask;
import ovh.exception.watchdogzz.network.IWSConsumer;
import ovh.exception.watchdogzz.network.NetworkManager;
import ovh.exception.watchdogzz.network.PostWebServiceTask;
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
        if(json!=null) {
            Snackbar.make(findViewById(R.id.content_main), json.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Gson gson = new Gson();
            JUser[] serverUsers = new JUser[1];
            try {
                serverUsers = gson.fromJson(json.getJSONArray("list").toString(), JUser[].class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

/**
            for (String s : serverUsernames) {
                try {
                    User tmpU = new User(Integer.toString(s.length()),s,"","",null,false,new GPSPosition(0f,0f,0f));
                    new WebServiceTask(MainActivity.this, new IWSConsumer() {
                        @Override
                        public void consume(JSONObject json) {
                            Gson gson = new Gson();
                            JUser u = gson.fromJson(json.toString(), JUser.class);
                            User nouv = new User(Integer.toString(u.name.length()),u.name,"","",null,false, new GPSPosition(
                                    u.location.length > 2 ? u.location[0] : 0.0f,
                                    u.location.length > 2 ? u.location[1] : 0.0f,
                                    u.location.length > 2 ? u.location[2] : 0.0f));   // necessaire apres serialisation
                            if(u.name != users.getMe().getName()) {     // on ne s'update pas sois meme
                                if (users.contains(nouv)) {         //  faire l'update
                                    users.updateUser(nouv.getName(), nouv);
                                } else {                        // faire l'ajout
                                    users.addUser(nouv);
                                }
                            }
                        }
                    }).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/where");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }**/

            for (JUser u : serverUsers) {    // on traite les nouveaux utilisateurs
                User nouv = new User(Integer.toString(u.name.length()),u.name,"","",null,false, new GPSPosition(
                        u.location.length > 2 ? u.location[0] : 0.0f,
                        u.location.length > 2 ? u.location[1] : 0.0f,
                        u.location.length > 2 ? u.location[2] : 0.0f));   // necessaire apres serialisation
                if(u.name != users.getMe().getName()) {     // on ne s'update pas sois meme
                    if (users.contains(nouv)) {         //  faire l'update
                        users.updateUser(nouv.getName(), nouv);
                    } else {                        // faire l'ajout
                        users.addUser(nouv);
                    }
                }
            }

        }
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
                new PostWebServiceTask(MainActivity.this, MainActivity.this, users.getMe()).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/where");
                new WebServiceTask(MainActivity.this, MainActivity.this).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/where");
            }
        });




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.postitionManager = new PostitionManager(this);
        this.networkManager = NetworkManager.getInstance(this.getApplicationContext());
        setUsers(new UserManager());
        User futurMe = (User) getIntent().getParcelableExtra("user");
        this.users.setMe(futurMe);
        getUsers().addObserver(renderer.getMap());
        //this.users.addUser(new User("tito","Bob","","",null,false, new GPSPosition(3.111185f, 45.759231f, 0.0f)));
        //this.users.addUser(new User("tata","Alice","","",null,false, new GPSPosition(3.111185f, 45.759271f, 0.5f)));


        // login sur le serveur
        new PostWebServiceTask(MainActivity.this, new IWSConsumer() {
            @Override
            public void consume(JSONObject json) {
                if(json==null) {
                    new PostWebServiceTask(MainActivity.this, new IWSConsumer() {
                        @Override
                        public void consume(JSONObject json) {
                            if(json==null) {

                            }
                        }
                    }, users.getMe()).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/login");
                }
            }
        }, users.getMe()).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/login");

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
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.default_share_message) + users.getMe().getPosition().toString());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
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
        new WebServiceTask(MainActivity.this, new IWSConsumer() {
            @Override
            public void consume(JSONObject json) {

            }
        }).execute("http://ec2-35-157-1-159.eu-central-1.compute.amazonaws.com/logout/");
    }

    public UserManager getUsers() {
        return users;
    }

    public void setUsers(UserManager users) {
        this.users = users;
    }
}
