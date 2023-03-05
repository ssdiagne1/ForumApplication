/*
 * Forum Fragment
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import edu.uncc.hw07.databinding.CommentRowItemBinding;
import edu.uncc.hw07.databinding.FragmentForumBinding;


public class ForumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";
 

    // TODO: Rename and change types of parameters
    String commentID;
    String commentCreator;
    String commentText;
    String dateCreated;
    private Forums forum;
    CommentsAdapter commentAdapter;
    ArrayList<Comments> mCommentsList = new ArrayList<>();
    FragmentForumBinding binding;
    FirebaseAuth mAuth;
    Comments commentObj;
    int count = 0;

    public ForumFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance( Forums f) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, f);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forum = (Forums) getArguments().getSerializable(ARG_PARAM_FORUM);
         
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForumBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textViewForumCreatedBy.setText(forum.getForumCreatedBy());
        binding.textViewForumText.setText(forum.getForumText());
        binding.textViewForumTitle.setText(forum.getForumTitle());

        mAuth = FirebaseAuth.getInstance();

        binding.buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment = binding.editTextComment.getText().toString();

                if (newComment.isEmpty() ) {
                    Toast.makeText(getActivity(), "new comment missing", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a");
                    HashMap<String, Object> comment = new HashMap<>();
                    comment.put("commentCreator", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    comment.put("dateCreated", sdf.format(Calendar.getInstance().getTime()));
                    comment.put("commentText", newComment);
                    db.collection("Forums").document(forum.getForumId()).collection("Comments").add(comment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Unable to create your post\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Forums").document(forum.getForumId()).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mCommentsList.clear();
                for (QueryDocumentSnapshot comment: value){
                    commentCreator= comment.getString("commentCreator");
                    commentID = comment.getId().toString();
                    commentText =  comment.getString("commentText");
                    dateCreated = comment.getString("dateCreated");

                    commentObj = new Comments(commentID,commentCreator,commentText,dateCreated);
                    mCommentsList.add(commentObj);


                }
                count = value.size();
                Log.d("demo", "onEvent: size inside of the firebase  "+count);

                commentAdapter.notifyDataSetChanged();
                binding.textViewCommentsCount.setText(Integer.toString(value.size())+" Comments");
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentsAdapter(mCommentsList);
        binding.recyclerView.setAdapter(commentAdapter);
    }

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsListViewHolder> {
        ArrayList<Comments> commentsList = new ArrayList<>();
        Boolean clicked = true;

        public CommentsAdapter(ArrayList<Comments> commentsData) {
            this.commentsList = commentsData;
        }

        @NonNull
        @Override
        public CommentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CommentRowItemBinding binding = CommentRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new CommentsListViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsAdapter.CommentsListViewHolder holder, int position) {
            Comments comments = mCommentsList.get(position);
            holder.setupUI(comments);

        }

        @Override
        public int getItemCount() {
            return mCommentsList.size();
        }

        class CommentsListViewHolder extends RecyclerView.ViewHolder {
            CommentRowItemBinding mBinding;
            Comments mComments;

            public CommentsListViewHolder(CommentRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;

            }


            public void setupUI(Comments comments) {
                mComments = comments;
                mBinding.textViewCommentText.setText(comments.commentText);
                mBinding.textViewCommentCreatedAt.setText(comments.dateCreated);
                mBinding.textViewCommentCreatedBy.setText(comments.commentCreator);

                if (mComments.getCommentCreator().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                    mBinding.imageViewDelete.setVisibility(VISIBLE);
                } else {
                    mBinding.imageViewDelete.setVisibility(INVISIBLE);
                }
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        //see if it will know which comment to delete
                        db.collection("Forums").document(forum.getForumId()).collection("Comments").document(comments.getCommentID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("demo", "onSuccess: deleted successfully");
                            }
                        });
                    }
                });

            }

        }
    }

}