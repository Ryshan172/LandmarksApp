package za.co.abiri.abirilandmarks.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import za.co.abiri.abirilandmarks.Adapters.CommentAdapter;
import za.co.abiri.abirilandmarks.Models.Comment;
import za.co.abiri.abirilandmarks.R;

public class PostDetailActivity extends AppCompatActivity {

    //Declarations
    ImageView imgPost,imgUserPost,imgCurrentUser;
    TextView txtPostDesc,txtPostCoords,txtPostDateName,txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        //Status bar transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();


        //Initializing
        RvComment = findViewById(R.id.rv_comment);
        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_userimg);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostCoords = findViewById(R.id.post_detail_coords);
        txtPostDateName = findViewById(R.id.post_detail_date);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_addbtn);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Comment Button
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl().toString();
                Comment comment = new Comment(comment_content,uid,uimg,uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("comment added");
                        editTextComment.setText("");
                        btnAddComment.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("failed to add comment : "+e.getMessage());

                    }
                });


            }
        });



        //Binding all data
        //Need to send post data to this activity from PostAdapter
        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userpostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImage).into(imgUserPost);

        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        String postCoordinates = getIntent().getExtras().getString("coordinates");
        txtPostCoords.setText(postCoordinates);

        //UserImage for comments
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

        //get Post Id
        PostKey = getIntent().getExtras().getString("postKey");

        //RecyclerView for comments Method
        initRvComment();


    }

    private void initRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showMessage(String message) {

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    /*
    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyy",calendar).toString();
        return date;


    }

     */
}