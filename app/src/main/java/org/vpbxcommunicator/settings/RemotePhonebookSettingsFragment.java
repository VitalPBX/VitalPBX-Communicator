package org.vpbxcommunicator.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import org.vpbxcommunicator.LinphoneActivity;
import org.vpbxcommunicator.R;
import org.vpbxcommunicator.contacts.RemotePhonebookParser;
import org.vpbxcommunicator.fragments.FragmentsAvailable;

/*
 * Written by Farhat Samara */

public class RemotePhonebookSettingsFragment extends Fragment {
    protected View mRootView;
    protected LinphonePreferences mPrefs;
    public static Context mContext;

    private EditText urlEditText;
    private Button retrieveButton;

    protected AsyncTask<Void, Void, Boolean> asyncTask;
    protected String enteredURL;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.settings_phonebook, container, false);
        mContext = getActivity().getApplicationContext();

        urlEditText = mRootView.findViewById(R.id.urlEditText);
        retrieveButton = mRootView.findViewById(R.id.retrieveButton);

        if (getLastURL() != null) {
            urlEditText.setText(getLastURL());
        }

        retrieveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGetClick();
                    }
                });

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPrefs = LinphonePreferences.instance();
        if (LinphoneActivity.isInstanciated()) {
            LinphoneActivity.instance()
                    .selectMenu(FragmentsAvailable.SETTINGS_SUBLEVEL, "Phonebook");
        }
    }

    private void onGetClick() {
        enteredURL = urlEditText.getText().toString();
        RemotePhonebookParser phonebookParser = new RemotePhonebookParser();

        // save URL for next time the user desires to sync phonebook
        saveURL(enteredURL);

        phonebookParser.setURL(enteredURL);
        asyncTask = phonebookParser.execute();
    }

    private void saveURL(String enteredURL) {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("savedURL", enteredURL);
        editor.apply();
    }

    private String getLastURL() {
        SharedPreferences preferences =
                getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        String url = preferences.getString("savedURL", null);

        return url;
    }
}
