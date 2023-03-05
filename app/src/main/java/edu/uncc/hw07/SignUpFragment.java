package edu.uncc.hw07;
/*
 * SignUp Fragment
 * Samba Diagne
 */
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import edu.uncc.hw07.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        // Required empty public constructor
    }
    FirebaseAuth mauth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.login();
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid name!", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid password!", Toast.LENGTH_SHORT).show();
                } else {
                    mauth= FirebaseAuth.getInstance();
                    mauth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = mauth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    mListener.goToForumsFragment(mauth.getCurrentUser().getUid());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error while trying to create a new profile\n"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.login();
            }
        });

        getActivity().setTitle(R.string.create_account_label);

    }

    SignUpListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SignUpListener) context;
    }

    interface SignUpListener {
        void login();
        void goToForumsFragment(String id);
    }
}