package com.ashudevs.facebookurlextractor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FacebookExtractor extends AsyncTask<Void,Integer, FacebookFile> {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36";
    private Context context;
    String url;
    private long startTime = 0L;
    private boolean showLogs = false;

    //For Errors
    private Exception exception = null;


    protected abstract void onExtractionComplete(FacebookFile facebookFile);
    protected abstract void onExtractionFail(Exception Error);

    private FacebookFile parseHtml(String url)
    {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();
            BufferedReader reader = null;
            urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            StringBuilder streamMap= new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line= reader.readLine()) != null) {
                    streamMap.append(line);
                }
            } catch (Exception E) {
                E.printStackTrace();
                if (reader != null)
                    reader.close();
                urlConnection.disconnect();
                onCancelled();
            } finally {
                if (reader != null)
                    reader.close();
                urlConnection.disconnect();
            }

            if (streamMap.toString().contains("You must log in to continue.")) {
                exception = new RuntimeException("You must log in to continue.");
                return null;
            }
            else
            {

                Pattern metaTAGTitle = Pattern.compile("<meta property=\"og:title\"(.+?)\" />");
                Matcher metaTAGTitleMatcher = metaTAGTitle.matcher(streamMap);

                Pattern metaTAGDescription = Pattern.compile("<meta property=\"og:description\"(.+?)\" />");
                Matcher metaTAGDescriptionMatcher = metaTAGDescription.matcher(streamMap);

                String authorName = "";
                String fileName = "";

                if(metaTAGTitleMatcher.find())
                {
                    String author = streamMap.substring(metaTAGTitleMatcher.start(),metaTAGTitleMatcher.end());
                    Log.e("Extractor","AUTHOR :: "+author);

                    author = author.replace("<meta property=\"og:title\" content=\"","").replace("\" />","");

                    authorName = author;
                }
                else
                {
                    authorName = "N/A";
                }

                if(metaTAGDescriptionMatcher.find())
                {
                    String name = streamMap.substring(metaTAGDescriptionMatcher.start(),metaTAGDescriptionMatcher.end());

                    Log.e("Extractor","FILENAME :: "+name);


                    name = name.replace("<meta property=\"og:description\" content=\"","").replace("\" />","");

                    fileName = name;
                }
                else
                {
                    fileName = "N/A";
                }

                Pattern sdVideo = Pattern.compile("(sd_src):\"(.+?)\"");
                Matcher sdVideoMatcher = sdVideo.matcher(streamMap);

                Pattern hdVideo = Pattern.compile("(hd_src):\"(.+?)\"");
                Matcher hdVideoMatcher = hdVideo.matcher(streamMap);


                FacebookFile facebookFile = new FacebookFile();
                facebookFile.setAuthor(authorName);
                facebookFile.setFilename(fileName);
                facebookFile.setExt("mp4");

                if(sdVideoMatcher.find())
                {
                    String vUrl = sdVideoMatcher.group();
                    vUrl = vUrl.substring(8,vUrl.length()-1); //sd_scr: 8 char
                    facebookFile.setSdUrl(vUrl);


                   if(showLogs){ Log.e("Extractor","SD_URL :: "+vUrl);}
                }
                else
                {
                    facebookFile.setSdUrl(null);
                }

                if(hdVideoMatcher.find())
                {
                    String vUrl = hdVideoMatcher.group();
                    vUrl = vUrl.substring(8,vUrl.length()-1); //hd_scr: 8 char

                   if(showLogs){ Log.e("Extractor","HD_URL :: "+vUrl);}
                }
                else
                {
                    facebookFile.setHdUrl(null);
                }

                if(facebookFile.getSdUrl() == null && facebookFile.getHdUrl()==null)
                {
                    exception = new RuntimeException("Url Not Valid");
                    return null;
                }

                return facebookFile;
            }
        }
        catch (Exception E)
        {
            exception = E;
            return null;
        }
    }

    @Override
    protected FacebookFile doInBackground(Void... voids) {
        FacebookFile Ff = parseHtml(url);
        return Ff;
    }

    @Override
    protected void onPostExecute(FacebookFile facebookFiles) {
        super.onPostExecute(facebookFiles);
        if(showLogs){
            Log.e("Extractor","Extraction Time Taken "+(System.currentTimeMillis()-startTime)+" MS");
        }
        if(facebookFiles!=null) {
            onExtractionComplete(facebookFiles);
        }
        else
        {
            onExtractionFail(exception);
        }
    }



    public FacebookExtractor(Context context, String url,boolean showLogs) {
        this.context = context;
        this.url = url;
        this.showLogs = showLogs;
        startTime = System.currentTimeMillis();

        if(showLogs){ Log.e("Extractor","Extraction Started "+startTime+" MS");}
        this.execute();
    }
}
