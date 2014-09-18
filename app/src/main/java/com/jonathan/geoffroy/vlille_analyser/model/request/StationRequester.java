package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.os.AsyncTask;

import com.jonathan.geoffroy.vlille_analyser.model.Station;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

    public static final String URL = "http://www.vlille.fr/stations/xml-stations.aspx";

    @Override
    protected ArrayList<Station> doInBackground(String... urls) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
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
                stations = parseXml(parser);
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

    private ArrayList<Station> parseXml(XmlPullParser parser) throws XmlPullParserException, IOException {
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
                    }
                    break;
            }
            event = parser.next();
        }
        return  stations;
    }
}
