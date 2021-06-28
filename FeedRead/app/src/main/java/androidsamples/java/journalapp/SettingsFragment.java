package androidsamples.java.journalapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SettingsViewModel mSettingsViewModel;
    Bundle bundle;
    String TAG="test";
    String _source="default";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        bundle=getArguments();
      //  String S = (String)bundle.getSerializable("test_id");

        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mSettingsViewModel= new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {

        if(key.equals("ESPN")){
            Preference exercisesPref = findPreference(key);
            Boolean s = sharedPreferences.getBoolean("ESPN",false);
            if(s)
                FeedData("ESPN","https://www.espn.com/espn/rss/news");
            else
                deleteData("ESPN");
        }
        else if(key.equals("WSJ")){
            Preference exercisesPref = findPreference(key);
            Boolean s = sharedPreferences.getBoolean("WSJ",false);
            if(s)
                FeedData("WSJ","https://feeds.a.dj.com/rss/RSSWSJD.xml");
            else
                deleteData("WSJ");
        }
        else if(key.equals("Life_Hacker")){
            Preference exercisesPref = findPreference(key);
            Boolean s = sharedPreferences.getBoolean("Life_Hacker",false);

            if(s)
                FeedData("Life_Hacker","https://lifehacker.com/rss");
            else
                deleteData("Life_Hacker");
        }
        else if (key.equals("sort"))
        {
            // Set summary to be the user-description for the selected value
            Preference exercisesPref = findPreference(key);

            String s =sharedPreferences.getString("sort","Title");
            Log.d("test",s);
            bundle.putSerializable("sort",s);
            // exercisesPref.setSummary(sharedPreferences.getString(key, ""));
        }
        else if (key.equals("time"))
        {
            // Set summary to be the user-description for the selected value
            Preference exercisesPref = findPreference(key);

            String s =sharedPreferences.getString("time","15");
            Log.d("test",s);
            bundle.putSerializable("time",s);
            // exercisesPref.setSummary(sharedPreferences.getString(key, ""));
        }




    }



    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void FeedData(String source, String url){
        _source=source;
        List<JournalEntry> list = new ArrayList<JournalEntry>();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    readXML(list,url);
                    for(int i=0; i< list.size();i++){
                        mSettingsViewModel.insert(list.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();





     //   readXML(list,file);
        //add to database

    }

    private void deleteData(String source) {
        mSettingsViewModel.deleteEntries(source);
    }


    ///////////////////////////////////////////
    public void readXML(List<JournalEntry> list,String url) {
       /*
        JournalEntry entry = new JournalEntry("", "10-10-1","http","false","false","BBC");
        mEntryListViewModel.insert(entry);
        mCallbacks.onEntrySelected(entry.getUid());

        */
        try{

        //    InputStream inputStream=getActivity().getAssets().open(file);
           // URL url = new URL("http://feeds.bbci.co.uk/news/rss.xml?edition=uk");

            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(3000);

            if(connection.getResponseCode()==200) {

                int s =connection.getResponseCode();
            }
            else {
                int s = connection.getResponseCode();
            }
            InputStream input = connection.getInputStream();


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);



            XmlPullParser xmlPullParser= factory.newPullParser();
            xmlPullParser.setInput(input ,"utf-8");
            xmlPullParser.nextTag();
            int eventType=xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){

                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String tagName=xmlPullParser.getName();
                        if(tagName.equals("item"))
                            catchItem(xmlPullParser, list);
                            /*
                            Log.d(TAG ,xmlPullParser.nextText());
                        else if(tagName.equals("item"))
                            Log.d(TAG ,"item start");
                        else if(tagName.equals("link"))
                            Log.d(TAG ,xmlPullParser.nextText());
                        else if(tagName.equals("pubDate"))
                            Log.d(TAG ,xmlPullParser.nextText());*/


                        break;
                    //  case XmlPullParser.END_TAG:
                    //      String tagName_2=xmlPullParser.getName();
                    //  if(tagName_2.equals("item"))
                    //    Log.d(TAG ,"item end");


                    //  break;
                    default:
                        break;
                }
                eventType=xmlPullParser.next();
            }


        }catch (Exception e){
            String stttt=e.getMessage();
            Log.d("test",e.getMessage());
        }
    }

    private void catchItem(XmlPullParser parser, List<JournalEntry> list) throws IOException, XmlPullParserException {
        String title,link,date;
        title="";
        link="";
        date="";
        int eventType=parser.getEventType();
        while(true){

            switch (eventType){
                case XmlPullParser.START_TAG:
                    String tagName=parser.getName();
                    if(tagName.equals("title")){
                        title=parser.nextText().trim();
                        Log.d(TAG ,title);}
                    else if(tagName.equals("link")) {
                        link=parser.nextText().trim();
                        Log.d(TAG, link);
                    }
                    else if(tagName.equals("pubDate")) {
                        date=parser.nextText().trim();
                        Log.d(TAG, date);
                    }

                    break;

                case XmlPullParser.END_TAG:
                    String tagName_2=parser.getName();
                    if(tagName_2.equals("item")) {
                        Log.d(TAG, "item end");
                        JournalEntry e= new JournalEntry(title,date,link,"false","false",_source);
                        list.add(e);
                        return;
                    }
                    break;
                default:
                    break;
            }
            eventType=parser.next();
        }
    }
}