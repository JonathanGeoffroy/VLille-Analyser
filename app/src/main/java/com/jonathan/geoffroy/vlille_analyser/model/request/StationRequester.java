package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.os.AsyncTask;
import android.util.Log;

import com.jonathan.geoffroy.vlille_analyser.model.Station;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by geoffroy on 15/09/14.
 */
public class StationRequester extends AsyncTask<String, Void, ArrayList<Station>> {

    public static final String ALL_STATIONS_URL = "http://www.vlille.fr/stations/xml-stations.aspx",
    STATION_DETAILS_URL="http://www.vlille.fr/stations/xml-station.aspx?borne=";
    private DefaultHttpClient client;

    public StationRequester() {
        super();
        client = new DefaultHttpClient();
    }

    @Override
    protected ArrayList<Station> doInBackground(String... urls) {
        StringBuilder builder = new StringBuilder();
        HttpGet httpGet = new HttpGet(urls[0]);

        ArrayList<Station> stations = null;
        try {
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
                while((line = stream.readLine()) != null) {
                    xmlContent.append(line);
                }

                // parse this stream
                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlFactoryObject.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES
                        , false);
                parser.setInput(new ByteArrayInputStream(xmlContent.toString().getBytes("utf-16")), null);
                stations = parseAllStations(parser);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            stations = new ArrayList<Station>();
        } catch (IOException e) {
            e.printStackTrace();
            stations = new ArrayList<Station>();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return stations;
    }

    private ArrayList<Station> parseAllStations(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Station> stations = new ArrayList<Station>();
        Station parsedStation;
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT)
        {
            String name=parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.END_TAG:
                    if(name.equals("marker")) {
                        parsedStation = new Station(
                                Integer.parseInt(parser.getAttributeValue(null, "id")),
                                parser.getAttributeValue(null, "name")
                           );
                        stations.add(parsedStation);
                        findDetails(parsedStation);
                    }
                    break;
            }
            event = parser.next();
        }
        return  stations;
    }

    private void findDetails(Station station) throws IOException, XmlPullParserException {
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
            while((line = stream.readLine()) != null) {
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
        while (event != XmlPullParser.END_DOCUMENT)
        {
            String name=parser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if(name.equals("bikes")) {
                        event = parser.next();
                        if(event == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            try {
                                station.setNbBikes(Integer.parseInt(text));
                            } catch(Exception e) {
                                Log.e("John", "enable to parse int: " + text);
                            }
                        }
                    }
                    else if(name.equals("attachs")) {
                        event = parser.next();
                        if(event == XmlPullParser.TEXT) {
                            String text = parser.getText();
                            try {
                                station.setNbFree(Integer.parseInt(text));
                            } catch(Exception e) {
                                Log.e("John", "enable to parse int: " + text);
                            }
                        }
                    }
                    break;
            }
            event = parser.next();
        }
    }
}
