/*
 * Create Forum Fragment
 * Samba Diagne
 */
package edu.uncc.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import edu.uncc.hw07.databinding.FragmentCreateForumBinding;

public class CreateForumFragment extends Fragment {

    FragmentCreateForumBinding binding;

    public CreateForumFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forumTitle = binding.editTextForumTitle.getText().toString();
                String forumDescription = binding.editTextForumDescription.getText().toString();

                if (forumTitle.isEmpty() || forumDescription.isEmpty()) {
                    Toast.makeText(getActivity(), "forum title or forum description missing", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
                    HashMap<String, Object> post = new HashMap<>();
                    post.put("forumCreatedBy", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    post.put("forumCreatedAt", sdf.format(Calendar.getInstance().getTime()));
                    post.put("forumTitle", forumTitle);
                    post.put("forumLiked", FieldValue.arrayUnion());
                    post.put("forumText", forumDescription);
                    post.put("userID", FirebaseAuth.getInstance().getUid());
                    db.collection("Forums").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mListener.goToForumsFragment(FirebaseAuth.getInstance().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Unable to create a forum\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
    
    CreateForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateForumListener) context;
    }

    interface CreateForumListener {
        void goToForumsFragment(String id);

    }


}


