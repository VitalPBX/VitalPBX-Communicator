package org.linphone.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import org.linphone.LinphoneActivity;
import org.linphone.R;
import org.linphone.contacts.RemotePhonebookParser;
import org.linphone.fragments.FragmentsAvailable;

public class RemotePhonebookSettingsFragment extends Fragment {
    protected View mRootView;
    protected LinphonePreferences mPrefs;
    public static Context mContext;

    private EditText urlEditText;
    private Button retrieveButton;

    protected AsyncTask<Void, Void, Boolean> asyncTask;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.settings_phonebook, container, false);
        mContext = getActivity().getApplicationContext();

        urlEditText = mRootView.findViewById(R.id.urlEditText);
        retrieveButton = mRootView.findViewById(R.id.retrieveButton);
        // parser = new RemotePhonebookParser();

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
            // TODO: title "Remote Phonebook" is too long for title bar
        }
    }

    private void onGetClick() {
        String enteredURL = urlEditText.getText().toString();
        RemotePhonebookParser phonebookParser = new RemotePhonebookParser();

        phonebookParser.setURL(enteredURL);
        asyncTask = phonebookParser.execute();
    }
}
