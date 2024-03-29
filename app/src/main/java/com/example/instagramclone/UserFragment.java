package com.example.instagramclone;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{

    private ListView listView;
    private ParseQuery<ParseUser> parseQuery;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private TextView tvLoadingUsers;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user, container, false);

        listView = (ListView) view.findViewById(R.id.lvListView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,arrayList);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        tvLoadingUsers = (TextView) view.findViewById(R.id.tvLoadingUsers);

        parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e!=null){
                    FancyToast.makeText(getContext(),e.getMessage(),
                            FancyToast.LENGTH_LONG,FancyToast.ERROR,
                            false).show();
                }else{
                    if(users.size()>0){
                        for(ParseUser user : users){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        tvLoadingUsers.animate().alpha(0f).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getContext(), UsersPostActivity.class);
        intent.putExtra("username", arrayList.get(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(i));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e!=null){
                    FancyToast.makeText(getContext(),e.getMessage(),
                            FancyToast.LENGTH_LONG,FancyToast.ERROR,
                            false).show();
                }else{
                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setIcon(R.drawable.person);
                    prettyDialog.setTitle("User Info")
                            .setMessage(user.get("profileName") + "").show();
                }
            }
        });

        return true;
    }
}
