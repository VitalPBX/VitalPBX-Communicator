package org.linphone.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import org.linphone.LinphoneActivity;
import org.linphone.R;
import org.linphone.fragments.FragmentsAvailable;
import org.linphone.settings.widget.BasicSetting;
import org.linphone.settings.widget.TextSetting;

public class RemotePhonebookSettingsFragment extends Fragment {
    protected View mRootView;
    protected LinphonePreferences mPrefs;

    // TODO
    private TextSetting mRemoteProvisioningUrl, mDisplayName, mUsername, mDeviceName;
    private BasicSetting mAndroidAppSettings;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.settings_phonebook, container, false);

        loadSettings();

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

        updateValues();
    }

    protected void loadSettings() {}

    protected void setListeners() {}

    protected void updateValues() {}
}
