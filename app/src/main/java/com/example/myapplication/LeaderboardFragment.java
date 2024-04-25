package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class LeaderboardFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private TableLayout leaderboardTable;

    ImageView btnClearAll;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        View rootView = inflater.inflate(R.layout.dialog_add_data, container, false);

        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        leaderboardTable = view.findViewById(R.id.leaderboardTable);

        btnClearAll = view.findViewById(R.id.btnClear);

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeletion();
            }
        });

        displayLeaderboard();
        return view;
    }

    private void confirmDeletion() {
        TextView messageTextView = new TextView(getContext());
        messageTextView.setText("\n\t\t\t\t\tThis will delete all data in leaderboard!");
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setGravity(Gravity.CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogTheme);
        builder .setTitle("CLEAR DATA?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    clearData();
                    displayLeaderboard();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        LinearLayout layout = new LinearLayout(getContext());
        /*layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);*/
        layout.addView(messageTextView);

        builder.setView(layout);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(Color.GREEN);
        }
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(Color.RED);
        }
    }


    private void displayLeaderboard() {
        leaderboardTable.removeAllViews();

        Map<String, ?> allEntries = sharedPreferences.getAll();

        List<LeaderboardEntry> entriesList = new ArrayList<>();

        // Convert SharedPreferences entries to LeaderboardEntry objects
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String name = entry.getKey();
            String name1 = entry.getKey();
            String number = entry.getValue().toString();
            entriesList.add(new LeaderboardEntry(name, Integer.parseInt(number)));
        }

        // Sort the list based on the number (descending order)
        Collections.sort(entriesList, new Comparator<LeaderboardEntry>() {
            @Override
            public int compare(LeaderboardEntry entry1, LeaderboardEntry entry2) {
                return Integer.compare(entry2.getNumber(), entry1.getNumber());
            }
        });


        // Display sorted entries in the leaderboard table
        for (LeaderboardEntry entry : entriesList) {
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            Log.d("LeaderboardFragment", "Name: " + entry.getName() + ", Number: " + entry.getNumber());

            TextView nameTextView = new TextView(getContext());
            nameTextView.setText(entry.getName());
            nameTextView.setTextColor(Color.WHITE);
            //nameTextView.setGravity(Gravity.CENTER);
            nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            TextView numberTextView = new TextView(getContext());
            numberTextView.setText(String.valueOf(entry.getNumber()));
            numberTextView.setTextColor(Color.WHITE);
            numberTextView.setGravity(Gravity.CENTER);
            numberTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            row.addView(nameTextView);
            row.addView(numberTextView);

            leaderboardTable.addView(row);
        }
    }

    private void clearData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Apply changes
    }
}