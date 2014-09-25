package com.jonathan.geoffroy.vlille_analyser.model.request;

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
import java.util.List;

/**
 * Created by geoffroy on 15/09/14.
 */
public class AllStationsParser {

    public static final String ALL_STATIONS_URL = "http://www.vlille.fr/stations/xml-stations.aspx",
            STATION_DETAILS_URL = "http://www.vlille.fr/stations/xml-station.aspx?borne=";

    private HttpClient client;

    public AllStationsParser(HttpClient client) {
        this.client = client;
    }

    public void execute(List<Station> stations) throws IOException, XmlPullParserException {
        HttpGet httpGet = new HttpGet(ALL_STATIONS_URL);
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
            parseAllStations(parser, stations);
        }
    }

    private void parseAllStations(XmlPullParser parser, List<Station> stations) throws XmlPullParserException, IOException {
        Station parsedStation;
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();
            switch (event) {
                case XmlPullParser.START_TAG:
                    break;
                case XmlPullParser.END_TAG:
                    if (name.equals("marker")) {
                        parsedStation = new Station(
                                Integer.parseInt(parser.getAttributeValue(null, "id")),
                                parser.getAttributeValue(null, "name"),
                                Double.parseDouble(parser.getAttributeValue(null, "lat")),
                                Double.parseDouble(parser.getAttributeValue(null, "lng"))
                        );
                        stations.add(parsedStation);
                    }
                    break;
            }
            event = parser.next();
        }
    }
}
