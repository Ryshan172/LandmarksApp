package za.co.abiri.abirilandmarks.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import za.co.abiri.abirilandmarks.Adapters.PostAdapter;
import za.co.abiri.abirilandmarks.Models.Post;
import za.co.abiri.abirilandmarks.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeBFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeBFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Views
    RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;


    public HomeBFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeBFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeBFragment newInstance(String param1, String param2) {
        HomeBFragment fragment = new HomeBFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home_b,container,false);
        postRecyclerView = fragmentView.findViewById(R.id.postRecView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);

        //Firebase data
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        return fragmentView;


    }

    //OnStart added in
    @Override
    public void onStart() {
        super.onStart();

        //Retrieve post from firebase database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {

                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);

                }

                postAdapter = new PostAdapter(getActivity(),postList);
                postRecyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}

