package za.co.abiri.abirilandmarks.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import za.co.abiri.abirilandmarks.Models.Post;
import za.co.abiri.abirilandmarks.R;

public class LocationActivity extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;

    private final static int PReqCode = 2;
    private final static int REQUESCODE = 2;
    private Uri pickedImageUri = null;

    ///Imported Layout Values
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    //Dialog popAddPost;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription,latitudeTextView;
    //Button getlocation;
    ProgressBar popupClickProgress;


    // Initializing other items
    // from layout file
    //TextView latitudeTextView, longitTextView;
    Button backBtn;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        latitudeTextView = findViewById(R.id.popup_coords1);
        //longitTextView = findViewById(R.id.lonTextView);
        backBtn = findViewById(R.id.backBtn1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Method to get the location
        getLastLocation();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
            }
        });

        //Popup Widgets Initializing
        popupUserImage = findViewById(R.id.popup_user_image);
        popupPostImage = findViewById(R.id.popup_img1);
        popupTitle = findViewById(R.id.popup_title1);
        popupDescription = findViewById(R.id.popup_description1);
        popupAddBtn = findViewById(R.id.popup_add1);
        popupClickProgress = findViewById(R.id.popup_progressBar1);

        //Loading User Image
        Glide.with(LocationActivity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        //Add Post Click Listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                //Testing All Input Fields
                if (!popupTitle.getText().toString().isEmpty()
                && !popupDescription.getText().toString().isEmpty()
                && !latitudeTextView.getText().toString().isEmpty()
                && pickedImageUri != null ) {

                    //No issues, no empty values
                    //TODO: Create Post object to add to firebase

                    //ACCESS FIREBASE STORAGE
                    //Storing in landmark_images
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("landmark_images");
                    StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();

                                    //CREATE POST OBJECT AND FILL WITH VALUES
                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            latitudeTextView.getText().toString(),
                                            imageDownloadLink,
                                            currentUser.getUid(),
                                            currentUser.getDisplayName(),
                                            currentUser.getPhotoUrl().toString());

                                    //ADD POST TO FIREBASE
                                    addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //ERROR WHEN UPLOADING PICTURE
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);


                                }
                            });

                        }
                    });




                }
                else {
                    showMessage("Please complete all fields and add an image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }



            }
        });

        //Adding an image
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkAndRequestForPermission();

            }
        });




    }

    //Add post method
    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myAppRef = database.getReference("Posts").push();

        //Obtain post ID and update Post Key
        String key = myAppRef.getKey();
        post.setPostKey(key);

        //Add Post to Firebase Database
        myAppRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showMessage("Post added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                //popAddPost.dismiss();
                Intent Home = new Intent(getApplicationContext(),Home.class);
                startActivity(Home);

            }
        });



    }

    //Show Message Method
    private void showMessage(String message) {

        Toast.makeText(LocationActivity.this,message,Toast.LENGTH_LONG).show();

    }

    ///Image Code
    private void checkAndRequestForPermission() {

        //Cases
        if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                //Toast
                Toast.makeText(LocationActivity.this, "Please accept required permissions",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
                //static int PReqCode
            }
        }
        else
            openGallery();
    }

    private void openGallery() {
        //open gallery intent and wait for user to pick image
        //code to open and access gallery on phone
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
        //Check if deprecated API causes any issues
    }

    //Override method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            //Image from gallery successfully picked by user
            //Reference needs to be saved to a Uri Variable
            pickedImageUri = data.getData();

            //To actually show image in field
            popupPostImage.setImageURI(pickedImageUri);

        }
    }




    //GEOLOCATION CODE//
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitudeTextView.setText(location.getLatitude() + "; " + location.getLongitude());
                            //longitTextView.setText(location.getLongitude() + "");
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    ///END OF GEOLOCATION CODE///


}