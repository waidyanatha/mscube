/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ms.cube;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Madhuka
 */
public class Connector {
private final String serverURL = "http://202.69.197.115:9763/headsup/";
    HttpConnection connection = null;
    InputStream is = null;
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    String response = null;

    public String getContain(String uri) {
        String strOut = "";
        try {
            connection = (HttpConnection) javax.microedition.io.Connector.open(serverURL+uri, javax.microedition.io.Connector.READ);
            connection.setRequestMethod(HttpConnection.GET); // Get Method
            connection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.1");

            if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
                is = connection.openInputStream();

                if (is != null) {
                    int ch = -1;

                    while ((ch = is.read()) != -1) {
                        bos.write(ch);
                    }

                    response = bos.toString(); // The XML Response
                    System.out.println(response);
                    strOut = response;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return strOut;
    }
}
