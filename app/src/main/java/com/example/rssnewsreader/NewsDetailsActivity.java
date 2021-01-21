package com.example.rssnewsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
    }


    public static class RSSFeedXmlParser {
        // We don't use namespaces
        private static final String ns = null;

        public List parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                in.close();
            }
        }

        private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            List channels = new ArrayList();

            parser.require(XmlPullParser.START_TAG, ns, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("item")) {
                    channels.add(readChannel(parser));
                } else {
                    skip(parser);
                }
            }
            return channels;

        }

        public static class Channel {
            public final String title;
            public final String link;

            private Channel(String title, String link) {
                this.title = title;
                this.link = link;
            }
        }

        // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
        private Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "channel");
            String title = null;
//            String summary = null;
            String link = null;
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    title = readTitle(parser);
                } else if (name.equals("link")) {
                    link = readLink(parser);
                } else {
                    skip(parser);
                }
            }
            return new Channel(title, link);
        }

        private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
            String link = "";
            parser.require(XmlPullParser.START_TAG, ns, "link");
            String tag = parser.getName();
            String relType = parser.getAttributeValue(null, "rel");
            if (tag.equals("link")) {
                if (relType.equals("alternate")){
                    link = parser.getAttributeValue(null, "href");
                    parser.nextTag();
                }
            }
            parser.require(XmlPullParser.END_TAG, ns, "link");
            return link;
        }

        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "title");
            return title;
        }

        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException{
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }

        }

    }

    public static class NetworkActivity extends Activity {
        public static final String WIFI = "Wi-Fi";
        public static final String ANY = "Any";
        private static final String URL = "http://rss.news.yahoo.com/rss/topstories";

        // Whether there is a Wi-Fi connection.
        private static boolean wifiConnected = false;
        // Whether there is a mobile connection.
        private static boolean mobileConnected = false;
        // Whether the display should be refreshed.
        public static boolean refreshDisplay = true;
        public static String sPref = null;

        // Uses AsyncTask to download the XML feed from stackoverflow.com.
        public void loadPage() {

            if ((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) {
                new DownloadXmlTask().execute(URL);
            } else if ((sPref.equals(WIFI)) && (wifiConnected)) {
                new DownloadXmlTask().execute(URL);
            } else {
                // show error
            }
        }
        // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
        private class DownloadXmlTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {
                try {
                    return loadXmlFromNetwork(urls[0]);
                } catch (IOException e) {
                    return getResources().getString(R.string.connection_error);
                } catch (XmlPullParserException e) {
                    return getResources().getString(R.string.xml_error);
                }
            }

            @Override
            protected void onPostExecute(String result) {
                setContentView(R.layout.activity_news_details);
                // Displays the HTML string in the UI via a WebView
                WebView myWebView = (WebView) findViewById(R.id.webview);
                myWebView.loadData(result, "text/html", null);
            }
        }

        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            RSSFeedXmlParser rssFeedXmlParser = new RSSFeedXmlParser();
            List<RSSFeedXmlParser.Channel> channels = null;
            String title = null;
            String url = null;
            Calendar rightNow = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");


            StringBuilder htmlString = new StringBuilder();
            htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
            htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                    formatter.format(rightNow.getTime()) + "</em>");

            try {
                stream = downloadUrl(urlString);
                channels = rssFeedXmlParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            for (RSSFeedXmlParser.Channel channel : channels) {
                htmlString.append("<p><a href='");
                htmlString.append(channel.link);
                htmlString.append("'>" + channel.title + "</a></p>");
                // If the user set the preference to include summary text,
                // adds it to the display.
//                if (pref) {
//                    htmlString.append(entry.summary);
//                }
            }
            return htmlString.toString();
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }

}