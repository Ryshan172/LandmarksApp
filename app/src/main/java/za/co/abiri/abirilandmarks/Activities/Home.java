package za.co.abiri.abirilandmarks.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import za.co.abiri.abirilandmarks.Activities.Fragments.HomeBFragment;
import za.co.abiri.abirilandmarks.Activities.Fragments.ProfileBFragment;
import za.co.abiri.abirilandmarks.Activities.Fragments.SettingsBFragment;
import za.co.abiri.abirilandmarks.R;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddPost;
    ImageView HomeUserImage,popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription,popupCoords;
    Button makePost;
    ProgressBar popupClickProgress;



    //private AppBarConfiguration mAppBarConfiguration;
    //private ActivityHome2Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////NEW CODE THAT MIGHT NOT WORK///
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ///ORIGINAL CODE///
        //binding = ActivityHome2Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarHome.toolbar);

        //Added Code
        //Init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        //POPUP CODE INIT
        initPopup();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popAddPost.show();

            }
        });

        /*
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */

        /* ORIGINAL CODE
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_settings, R.id.nav_signout)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

         */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Added code
        updateNavHeader();

        //Set Home Fragment as Default
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeBFragment()).commit();





    }



    ///POPUP METHOD///
    private void initPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //Popup Widgets Initializing
        HomeUserImage = popAddPost.findViewById(R.id.homeUserImg);

        /*
        popupPostImage = popAddPost.findViewById(R.id.popup_img1);
        popupTitle = popAddPost.findViewById(R.id.popup_title1);
        popupDescription = popAddPost.findViewById(R.id.popup_description1);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add1);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar1);
        popupCoords = popAddPost.findViewById(R.id.popup_coords1);

         */

        //Loading User Image
        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(HomeUserImage);

        makePost = popAddPost.findViewById(R.id.postViewBtn);
        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LocationActivity.class);
                startActivity(i);

            }

        });

        /*
        //Add Post Click Listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);
            }
        });

         */


    }
    ///End of Popup method



    ///NEW CODE THAT MIGHT NOT WORK
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    ///NEW CODE///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    /* ORIGINAL CODE
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
     */

    //NEW CODE
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


    //NEW CODE
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeBFragment()).commit();

        } else if (id == R.id.nav_profile) {

            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProfileBFragment()).commit();

        } else if (id == R.id.nav_settings) {

            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new SettingsBFragment()).commit();


        }
        else if (id == R.id.nav_signout) {

            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //Added Code
    public void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

        //User Glide to load user photo
        //Imported library https://github.com/bumptech/glide
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);


    }
}