package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.util.Log;

import com.jonathan.geoffroy.vlille_analyser.model.Station;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Parse le XML de VLille permettant de récupérer les détails d'une station<br/>
 * <p/>
 * Created by geoffroy on 15/09/14.
 */
public class StationDetailsParser {

    public static final String STATION_DETAILS_URL = "http://www.vlille.fr/stations/xml-station.aspx?borne=";

    private HttpClient client;

    public StationDetailsParser(HttpClient client) {
        this.client = client;
    }

    /**
     * Lance une réquête sur <code>STATION_DETAILS_URL</code>.<br/>
     * Parse les données XML récupérées afin de trouver les détails de la station
     *
     * @throws IOException            en cas de problème réseau
     * @throws XmlPullParserException si il est impossible de parser le résultat obtenu
     */
    public void execute(Station station) throws IOException, XmlPullParserException {
        HttpGet httpGet = new HttpGet(STATION_DETAILS_URL + station.getId());
        HttpResponse response = client.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            // get response stream
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();

            BufferedReader stream = new BufferedReader(new InputStreamReader(content));
            String line;
            StringBuffer xmlContent = new StringBuffer();
            while ((line = stream.readLine()) != null) {
                xmlContent.append(line);
            }

            // parse this stream
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                    , false);
            parser.setInput(new ByteArrayInputStream(xmlContent.toString().getBytes("utf-16")), null);
            parseStationDetails(parser, station);
        }
    }

    private void parseStationDetails(XmlPullParser parser, Station station) throws XmlPullParserException, IOException {
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (name.equals("bikes")) {
                        event = parser.next();
                        if (event == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            try {
                                station.setNbBikes(Integer.parseInt(text));
                            } catch (Exception e) {
                                Log.e("John", "enable to parse int: " + text);
                            }
                        }
                    } else if (name.equals("attachs")) {
                        event = parser.next();
                        if (event == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            try {
                                station.setNbFree(Integer.parseInt(text));
                            } catch (Exception e) {
                                Log.e("Parser", "enable to parse int: " + text);
                            }
                        }
                    }
                    break;
            }
            event = parser.next();
        }
    }
}
