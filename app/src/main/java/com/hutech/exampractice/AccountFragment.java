package com.hutech.exampractice;

import static com.hutech.exampractice.DbQuery.g_userList;
import static com.hutech.exampractice.DbQuery.g_usersCount;
import static com.hutech.exampractice.DbQuery.myPerformance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private LinearLayout logoutB;
    private TextView profile_img_text, name, score, rank;
    private LinearLayout leaderB, profileB, bookmarksB;
    private BottomNavigationView bottomNavigationView;
    private Dialog progressDialog;
    private TextView dialogText;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading... ");


        String userName = DbQuery.myProfile.getName();
        profile_img_text.setText(userName.toUpperCase().substring(0,1));
        name.setText(userName);
        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if(DbQuery.g_userList.size() == 0)
        {
            progressDialog.show();
            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if(myPerformance.getScore() != 0)
                    {
                        if( ! DbQuery.isMeOnTopList)
                        {
                            calculateRank();
                        }

                        score.setText("Score: "+ myPerformance.getScore());
                        rank.setText("Rank - " + myPerformance.getRank());


                    }


                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Something went wrong ! Please try again.",
                            Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            });
        }
            else
        {

            score.setText("Score: "+ myPerformance.getScore());
            if(myPerformance.getScore() != 0)
            rank.setText("Rank - " + myPerformance.getRank());
        }

        logoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(getContext(), gso);

                mGoogleClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();


                    }
                });


            }
        });

        bookmarksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), BookmarkActivity.class);
                startActivity(intent);

            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                    startActivity(intent);
            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
            }
        });



        return view;
    }

    private void initViews(View view){
        logoutB = view.findViewById(R.id.logoutB);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.name);
        score = view.findViewById(R.id.total_score);
        rank = view.findViewById(R.id.rank);
        leaderB = view.findViewById(R.id.leaderB);
        bookmarksB = view.findViewById(R.id.bookmarkB);
        profileB = view.findViewById(R.id.profileB);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
    }

    private void calculateRank()
    {
        int lowTopScore = g_userList.get(g_userList.size() -1 ).getScore();

        int remaining_slots = g_usersCount - 20;

        int myslot = (myPerformance.getScore()*remaining_slots) / lowTopScore;

        int rank;

        if(lowTopScore != myPerformance.getScore())
        {
            rank = g_usersCount - myslot;
        }
        else
        {
            rank = 21;
        }

        myPerformance.setRank(rank);
    }


}