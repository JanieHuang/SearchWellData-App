package com.example.mostafa.myapplication1;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    // XML node names
    static final String WELL_DATA = "welldata";
    static final String WELL_LOC = "location";
    static final String WELL_DEPTH = "welldepth";
    static final String WELL_PERFDEPTH = "perfdepth";
    static final String WELL_PERFZONE = "perfzone";
    static final String WELL_STROKE = "stroke";
    static final String WELL_STROKEPERMIN = "strokepermin";
    public ArrayList wellList = new ArrayList();
    public ArrayList searchList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getData();
        Button searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.searchEditText);
                String search = et.getText().toString();
                searchWellData(search);
                printResults();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            getData(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        URL url = null; HttpURLConnection urlConnection;
        String et = "http://10.0.2.2/wellsites/welldata.xml";
        try {
            url = new URL(et);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            readStream(in);
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            TextView tv = (TextView) findViewById(R.id.locationTextView);
            tv.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            TextView tv = (TextView) findViewById(R.id.locationTextView);
            tv.setText(e.getMessage());
        } finally {

        }


    }

    private void readStream(InputStream in) {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            parseXML(total.toString());
            //TextView tv = (TextView) findViewById(R.id.locationTextView);
            //tv.setText(total.toString());

        } catch (IOException e) {
            e.printStackTrace();
            TextView tv = (TextView) findViewById(R.id.locationTextView);
            tv.setText(e.getMessage());
        }
    }

    public void parseXML(String total) {
        String locationText ="";
        String depthText = "";
        String perfDepthText = "";
        String perfZoneText = "";
        String strokeText = "";
        String strokeperminText = "";
        try {
            DocumentBuilder db = null;
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(total));

            Document doc = null;

            doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(WELL_DATA);

            for (int i = 0; i < nodes.getLength(); i++)
            {
                Element element = (Element) nodes.item(i);

                NodeList location = element.getElementsByTagName(WELL_LOC);
                Element line = (Element) location.item(0);
                locationText = getCharacterDataFromElement(line);

                NodeList depth = element.getElementsByTagName(WELL_DEPTH);
                line = (Element) depth.item(0);
                depthText = getCharacterDataFromElement(line);

                NodeList perfdepth = element.getElementsByTagName(WELL_PERFDEPTH);
                line = (Element) perfdepth.item(0);
                perfDepthText = getCharacterDataFromElement(line);

                NodeList perfzone = element.getElementsByTagName(WELL_PERFZONE);
                line = (Element) perfzone.item(0);
                perfZoneText = getCharacterDataFromElement(line);

                NodeList stroke = element.getElementsByTagName(WELL_STROKE);
                line = (Element) stroke.item(0);
                strokeText = getCharacterDataFromElement(line);

                NodeList strokepermin = element.getElementsByTagName(WELL_STROKEPERMIN);
                line = (Element) strokepermin.item(0);
                strokeperminText = getCharacterDataFromElement(line);

                double depthDb = Double.parseDouble(depthText);
                double perfdepthDb = Double.parseDouble(perfDepthText);
                double perfZoneDb = Double.parseDouble(perfZoneText);
                double strokeDb = Double.parseDouble(strokeText);
                int strokeperminInt = Integer.parseInt(strokeperminText);

                WellData wd = new WellData(locationText, depthDb, perfdepthDb, perfZoneDb,
                        strokeDb, strokeperminInt);
                wellList.add(wd);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public void searchWellData(String search)
    {

        for(int i=0; i < wellList.size(); i++)
        {
            WellData wd = (WellData) wellList.get(i);

            if(wd.getLocation().equalsIgnoreCase(search))
            {
                searchList.add(wd);
            }
        }
    }

    public void printResults()
    {
        TextView tv = (TextView) findViewById(R.id.locationTextView);
        if(searchList.size() ==0)
        {
            tv.setText("No results");
        }
        else
        {
            tv.setText(searchList.toString());
        }

    }

}
