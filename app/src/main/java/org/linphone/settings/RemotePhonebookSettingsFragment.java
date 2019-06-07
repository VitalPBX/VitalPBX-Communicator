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
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.linphone.LinphoneActivity;
import org.linphone.R;
import org.linphone.contacts.ContactsManager;
import org.linphone.contacts.RemotePhonebookParser;
import org.linphone.fragments.FragmentsAvailable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RemotePhonebookSettingsFragment extends Fragment {
    protected View mRootView;
    protected LinphonePreferences mPrefs;
    public static Context mContext;

    private EditText urlEditText;
    private Button retrieveButton;

    protected ContactsManager contactsManager;
    protected RemotePhonebookParser phonebookParser;
    protected AsyncTask<Void, Void, Void> asyncTask;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.settings_phonebook, container, false);
        mContext = getActivity().getApplicationContext();

        urlEditText = mRootView.findViewById(R.id.urlEditText);
        retrieveButton = mRootView.findViewById(R.id.retrieveButton);

        // phonebookParser = new RemotePhonebookParser();

        retrieveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncTask = new RemotePhonebookParser().execute();
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

    private void addRemoteContacts() {
        // create strings to test results
        String name;
        String sipNumber;

        try {

            URL url = new URL("http://192.168.25.12/phonebook.php?pb=huKpGJToE");
            // TODO: url would be retrieved from textEdit

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("VCommunicatorPhoneDirectory"); // TODO

            System.out.println("-----------------------------");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                System.out.println("Current item: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    name = element.getAttribute("Name");
                    sipNumber = element.getAttribute("Telephone");

                    System.out.println("Current contact name: " + name);
                    System.out.println("Current contact phone: " + sipNumber);
                }
            }

        } catch (Exception e) {
            System.out.println("XML parser exception: " + e);
        }
    }
}
