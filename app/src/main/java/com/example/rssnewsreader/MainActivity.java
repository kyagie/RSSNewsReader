package com.example.rssnewsreader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private NodeList nodelist;
    private TextView textWord, title, pubdate, link;
    ProgressDialog pDialog;
    private String uri = "https://news.yahoo.com/rss/topstories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 20) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initVar();
//        DownloadXML
        new DownloadXML().execute(uri);

    }
    public void initVar(){
        textWord = (TextView)findViewById(R.id.textWord);
        title = (TextView)findViewById(R.id.title);
//        pubdate = (TextView)findViewById(R.id.pubdate);
        link = (TextView)findViewById(R.id.link);

    }


    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
            pDialog.setTitle("Fetching RSSFeed headlines.");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }
//
        @Override
        protected Void doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("item");

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void args) {
            int dd = nodelist.getLength();
            Toast.makeText(MainActivity.this,dd +" "+ " RSSFeed Headlines Fetched.",
                    Toast.LENGTH_LONG).show();

            iterable(nodelist).forEach(node -> {
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                        
                    title.setText(title.getText() + getNode("title", eElement) + "\n" + "\n");
                    link.setText(link.getText() + getNode("link", eElement) + "\n" + "\n");

                }
                });
//            for (Node node : iterable(nodelist)) {
//                // ....
//
//                if (node.getNodeType() == Node.ELEMENT_NODE){
//                    Element eElement = (Element) node;
//
//                    title.setText(title.getText() + getNode("title", eElement) + "\n" + "\n");
//                    link.setText(link.getText() + getNode("link", eElement) + "\n" + "\n");
//
//                }
//            }


            pDialog.dismiss();
        }

    }
//     getNode function
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }
    public void onClick(View view) {
//            ...
        Intent intent = new Intent(this, NewsDetailsActivity.class);
        link = (TextView)findViewById(R.id.link);
        String Link = link.getText().toString();
        intent.putExtra("HEADLINE_URL", Link);
        startActivity(intent);

    }
}