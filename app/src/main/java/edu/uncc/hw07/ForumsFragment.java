/*
 * Forums Fragment
 * Samba Diagne
 */
package edu.uncc.hw07;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uncc.hw07.databinding.CommentRowItemBinding;
import edu.uncc.hw07.databinding.ForumRowItemBinding;
import edu.uncc.hw07.databinding.FragmentForumsBinding;

public class ForumsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAMID = "ARG_PARAMID";


    // TODO: Rename and change types of parameters
    String userId; 
    String forumCreatedBy;
    String forumID;
    String text;
    String title;
    String createdAt;
    Forums forumObj;
    FragmentForumsBinding binding;
    FirebaseAuth mAuth;
    String forumliked;
    ArrayList<String> forumlikedlist = new ArrayList<>();
    int countLikes = 0;


    public ForumsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForumsFragment newInstance(String id) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAMID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAMID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForumsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Forums").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mforumsList.clear();
                for (QueryDocumentSnapshot forum: value){
                    forumCreatedBy = forum.getString("forumCreatedBy");
                    forumID = forum.getId().toString();
                    userId =  forum.getString("userID");
                    text = forum.getString("forumText");
                    title = forum.getString("forumTitle");
                    createdAt =  forum.getString("forumCreatedAt");
                    forumlikedlist = (ArrayList<String>) forum.get("forumLiked");
                    Log.d("demo44", "likes from firebase "+forumlikedlist);


                    Log.d("demo", "onEvent: "+forumliked);


                    forumObj = new Forums(forumID,title, forumCreatedBy, text, createdAt, userId,forumlikedlist);
                    mforumsList.add(forumObj);
                }



                forumsAdapter.notifyDataSetChanged();
            }
        });


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forumsAdapter = new ForumsAdapter(mforumsList);
        binding.recyclerView.setAdapter(forumsAdapter);
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                mListener.login();
            }
        });
        binding.buttonCreateForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToCreateForum();
            }
        });

    }
    ForumsAdapter forumsAdapter;
    ArrayList<Forums> mforumsList = new ArrayList<>();

    class ForumsAdapter extends RecyclerView.Adapter<ForumsFragment.ForumsAdapter.ForumsListViewHolder> {
        ArrayList<Forums> forumsList = new ArrayList<>();
        Boolean clicked = true;

        public ForumsAdapter(ArrayList<Forums> ForumsData) {
            this.forumsList = ForumsData;
        }

        @NonNull
        @Override
        public ForumsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ForumRowItemBinding binding = ForumRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForumsListViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsFragment.ForumsAdapter.ForumsListViewHolder holder, int position) {
            Forums Forums = mforumsList.get(position);
            holder.setupUI(Forums);

        }

        @Override
        public int getItemCount() {
            return mforumsList.size();
        }

        class ForumsListViewHolder extends RecyclerView.ViewHolder {
            ForumRowItemBinding mBinding;
            Forums mForums;

            public ForumsListViewHolder(ForumRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.goToDetailForm(mforumsList.get(getAdapterPosition()));
                    }
                });
            }

            int count = 0;

            public void setupUI(Forums forums) {
                mForums = forums;
                mBinding.textViewForumCreatedBy.setText(forums.forumCreatedBy);
                mBinding.textViewForumText.setText(forums.getForumText());
                mBinding.textViewForumTitle.setText(forums.getForumTitle());
                mBinding.textViewForumLikesDate.setText(forums.forumLiked.size()+" Likes | "+forums.getForumCreatedAt());
                Log.d("demo", "setupUI: userID from login " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                Log.d("demo", "setupUI: forumliked from firebase " + forums.forumLiked);
                for (String like: mForums.forumLiked) {
                    if ((!like.isEmpty()) && (like.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()))) {
                        mBinding.imageViewLike.setImageResource(R.drawable.like_favorite);

                    }
                }

                if (mForums.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    mBinding.imageViewDelete.setVisibility(VISIBLE);
                } else {
                    mBinding.imageViewDelete.setVisibility(INVISIBLE);
                }
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Forums").document(forums.getForumId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("demo", "onSuccess: deleted successfully");
                            }
                        });
                    }
                });

                mBinding.imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clicked) {
                            clicked = false;
                            mBinding.imageViewLike.setImageResource(R.drawable.like_favorite);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference likeRef = db.collection("Forums").document(mForums.forumId);
                            likeRef.update("forumLiked", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                        } else {
                            clicked = true;

                            mBinding.imageViewLike.setImageResource(R.drawable.like_not_favorite);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference forumRef = db.collection("Forums").document(mForums.forumId);
                            forumRef.update("forumLiked", FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                        }

                    }
                });
            }


        }
    }

    ForumsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumsListener) context;
    }

    interface ForumsListener {
        void login();
        void goToCreateForum();
        void goToDetailForm(Forums forum);

    }
}