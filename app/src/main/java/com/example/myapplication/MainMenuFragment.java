package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuFragment extends Fragment {

    View view, diffView, instrView;

    ImageView btnStart, btnInstruction, btnEasy, btnMedium, btnHard, btnXDiff, btnXInstruction, btnName;

    String playerName;

    TextView tv_Greetings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        diffView = view.findViewById(R.id.difficulty);
        instrView = view.findViewById(R.id.instruction);

        tv_Greetings = view.findViewById(R.id.tv_greetings);

        btnName = view.findViewById(R.id.btn_login);
        btnName.setOnClickListener(v -> showAddEntryDialog());

        btnStart = view.findViewById(R.id.btnStartGame);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerName == null){
                    showAddEntryDialog();
                    tv_Greetings.setText("Please insert name before starting");
                } else{
                    openDifficulty();
                }

            }
        });

        btnInstruction = view.findViewById(R.id.btnInstruction);
        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstruction();
            }
        });

        btnEasy = view.findViewById(R.id.btnEasy);
        btnEasy.setClickable(false);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEasyGame();
            }
        });

        btnMedium = view.findViewById(R.id.btnMedium);
        btnMedium.setClickable(false);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMediumGame();
            }
        });

        btnHard = view.findViewById(R.id.btnHard);
        btnHard.setClickable(false);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newHardGame();
            }
        });

        btnXDiff = view.findViewById(R.id.btn_x_difficulty);
        btnXDiff.setClickable(false);
        btnXDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDiff();
            }
        });

        btnXInstruction = view.findViewById(R.id.btn_x_instruction);
        btnXInstruction.setClickable(false);
        btnXInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitInstr();
            }
        });

        return view;

    }

    private void showAddEntryDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_add_data, null);

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogTheme);
        builder.setView(dialogView)
                .setTitle("CHANGE NAME")
                .setPositiveButton("Change", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    if (name.isEmpty()) {
                        // If the name is empty, display a message to the user
                        Toast.makeText(getContext(), "Please add your name", Toast.LENGTH_SHORT).show();
                    } else if (name.equals(" ")) {
                        // If the name is empty, display a message to the user
                        Toast.makeText(getContext(), "Please add your name", Toast.LENGTH_SHORT).show();
                    } else {
                        // If the name is not empty, proceed with your action (e.g., saving the entry)
                        saveEntry(name);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

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

    private void saveEntry(String name) {
        playerName = name;
        updateGreetings();
    }

    private void updateGreetings(){
        tv_Greetings.setText("Nice to meet you, "+playerName);
    }

    private void openDifficulty() {
        diffView.setVisibility(view.VISIBLE);
        btnStart.setClickable(false);
        btnInstruction.setClickable(false);
        btnEasy.setClickable(true);
        btnMedium.setClickable(true);
        btnHard.setClickable(true);
        btnXDiff.setClickable(true);
    }

    private void openInstruction() {
        instrView.setVisibility(view.VISIBLE);
        btnStart.setClickable(false);
        btnInstruction.setClickable(false);
        btnXInstruction.setClickable(true);
    }

    private void newEasyGame(){
        Intent intent = new Intent(getActivity(), EasyGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        getActivity().finish();
    }

    private void newMediumGame(){
        Intent intent = new Intent(getActivity(), MediumGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        getActivity().finish();
    }

    private void newHardGame(){
        Intent intent = new Intent(getActivity(), HardGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        getActivity().finish();
    }

    private void exitDiff() {
        diffView.setVisibility(view.INVISIBLE);
        btnStart.setClickable(true);
        btnInstruction.setClickable(true);
        btnEasy.setClickable(false);
        btnMedium.setClickable(false);
        btnHard.setClickable(false);
        btnXDiff.setClickable(false);
    }

    private void exitInstr() {
        instrView.setVisibility(view.INVISIBLE);
        btnStart.setClickable(true);
        btnInstruction.setClickable(true);
        btnXInstruction.setClickable(false);
    }
}

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
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
    }*/