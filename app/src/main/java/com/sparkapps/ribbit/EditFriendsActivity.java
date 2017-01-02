package com.sparkapps.ribbit;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditFriendsActivity extends AppCompatActivity {


    protected ListView mEditList;
    protected ProgressBar mProgressbar;
    protected DatabaseReference mDatabase;
    protected ArrayList<String> mUserNames;
    protected final String TAG = EditFriendsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        mEditList = (ListView) findViewById(R.id.edit_friends);
        mProgressbar = (ProgressBar) findViewById(R.id.edit_progressBar);
        mUserNames = new ArrayList<String>();
        mEditList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    protected void onResume() {

        super.onResume();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(FireBaseConstants.NODE_USERS);

        mProgressbar.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgressbar.setVisibility(View.INVISIBLE);
                Log.i(TAG + " Count " ,""+dataSnapshot.getChildrenCount());

                for (DataSnapshot data: dataSnapshot.getChildren())
                {
                    User user = data.getValue(User.class);
                    Log.i(TAG + "Get Data ", user.username);
                    mUserNames.add(user.username);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditFriendsActivity.this,
                        android.R.layout.simple_list_item_checked, mUserNames);

                mEditList.setAdapter(adapter);
                Log.i(TAG + "ArrayList Count", String.valueOf(mUserNames.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());

                AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                builder.setTitle(getString(R.string.signup_error_title));
                builder.setMessage(databaseError.getMessage());
                builder.setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }
}
