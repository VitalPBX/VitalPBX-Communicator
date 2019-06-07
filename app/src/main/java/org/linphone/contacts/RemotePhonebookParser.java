package org.linphone.contacts;

import android.os.AsyncTask;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.linphone.LinphoneActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RemotePhonebookParser extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        // create strings to test results
        String name;
        String sipNumber;

        ContactsManager contactsManager;
        LinphoneNumberOrAddress numberOrAddress;
        try {

            URL url = new URL("http://192.168.26.10/phonebook.php?pb=EdRbklpGk");
            // TODO: url would be retrieved from textEdit

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Contact"); // TODO

            System.out.println("-----------------------------");

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                System.out.println("Current item: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    name = getValue("Name", element);
                    sipNumber = getValue("Telephone", element);

                    LinphoneActivity.instance().asyncAddContact(name, sipNumber);
                }
            }

        } catch (Exception e) {
            System.out.println("XML parser exception: " + e);
        }

        return null;
    }

    // used to get specific attributes for each XML tag
    // in this case: pass the attribute name, and the populated element
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);

        return node.getNodeValue();
    }

    // use HttpURLConnection to create an advanced request
    // i.e; specify the type of header to retrieve remote phonebook
    /*private HttpURLConnection httpURLConnectionSetter(URL url) {
        HttpURLConnection connection;

        try {

            connection = (HttpURLConnection) url.openConnection();

        } catch (Exception e) {
            System.out.println("URL connection exception: " + e);
        }

        return connection;
    }*/
}
