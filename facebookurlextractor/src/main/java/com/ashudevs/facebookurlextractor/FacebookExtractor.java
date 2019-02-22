package com.ashudevs.facebookurlextractor;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public abstract class FacebookExtractor extends AsyncTask<Void,Integer, FacebookFile> {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36";
    Context mContext;
    String url;



    protected abstract void onExtractionComplete(FacebookFile vimeoFile);
    protected abstract void onExtractionFail(String Error);

    private FacebookFile parseHtml(String url)
    {
        String result="";
        String filename="";
        FacebookFile Ff=new FacebookFile();
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
                result = "Not Public Video";
            } else {

                /*Pattern videoPattern = Pattern.compile("<div class=\"_53mw\" (.+?)</div>");
                Matcher videoPAtternMatcher = videoPattern.matcher(streamMap);

                if (videoPAtternMatcher.find()) {

                    result = streamMap.substring(videoPAtternMatcher.start(), videoPAtternMatcher.end());
                    Pattern VideoSrcPattern = Pattern.compile("\"src\":\"(.+?)\"");
                    Matcher VideoSrcPatternMatcher = VideoSrcPattern.matcher(result);
                    if (VideoSrcPatternMatcher.find()) {
                        result = result.substring(VideoSrcPatternMatcher.start(), VideoSrcPatternMatcher.end());
                        result = result.replace("\"src\":\"", "").replace("\"", "");
                        Pattern VideoSrcPatternExt = Pattern.compile("\\.([a-z,0-9]{3,4})\\?");
                        Matcher VideoSrcPatternMatcherExt = VideoSrcPatternExt.matcher(result);
                        if (VideoSrcPatternMatcherExt.find()) {
                            String ext = result.substring(VideoSrcPatternMatcherExt.start(), VideoSrcPatternMatcherExt.end());
                            ext = ext.replace("?", "");
                            Ff.setFilename(filename);
                            Ff.setExt(ext);
                            Ff.setUrl(result);
                        }
                    }
                    //Log.e("HTML", "" + result);

                } else {

                    Pattern videoPattern2 = Pattern.compile("<div class=\"_53mw _2g8e\" (.+?)</div>");
                    Matcher videoPAtternMatcher2 = videoPattern2.matcher(streamMap);
                    if (videoPAtternMatcher2.find()) {

                        result = streamMap.substring(videoPAtternMatcher2.start(), videoPAtternMatcher2.end());
                        result = result.replace("&quot;", "\"");
                        Pattern VideoSrcPattern = Pattern.compile("\"src\":\"(.+?)\"");
                        Matcher VideoSrcPatternMatcher = VideoSrcPattern.matcher(result);
                        if (VideoSrcPatternMatcher.find()) {
                            result = result.substring(VideoSrcPatternMatcher.start(), VideoSrcPatternMatcher.end());
                            result = result.replace("\"src\":\"", "").replace("\"", "");
                            Pattern VideoSrcPatternExt = Pattern.compile("\\.([a-z,0-9]{3,4})\\?");
                            Matcher VideoSrcPatternMatcherExt = VideoSrcPatternExt.matcher(result);
                            if (VideoSrcPatternMatcherExt.find()) {
                                String ext = result.substring(VideoSrcPatternMatcherExt.start(), VideoSrcPatternMatcherExt.end());
                                ext = ext.replace("?", "");
                                Ff.setFilename(filename);
                                Ff.setExt(ext);
                                Ff.setUrl(result);

                                try {
                                    HttpsURLConnection videoDetails =(HttpsURLConnection) new URL(result).openConnection();
                                    videoDetails.connect();

                                    long x = videoDetails.getContentLength();
                                    long fileSizeInKB = x / 1024;
                                    long fileSizeInMB = fileSizeInKB / 1024;
                                    //Log.e("File Size", "Getted File Size : " + fileSizeInMB);

                                    Ff.setSize((fileSizeInMB > 1) ? fileSizeInMB + " MB" : fileSizeInKB + " KB");

                                    videoDetails.disconnect();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        //Log.e("HTML", "" + result);

                    } else {
                        result = null;
                    }
                }*/



                Pattern metaTAGVideoSRC = Pattern.compile("<meta property=\"og:video\"(.+?)\" />");
                Matcher metaTAGVideoSRCPatternMatcher= metaTAGVideoSRC.matcher(streamMap);

                Pattern metaTAGTitle = Pattern.compile("<meta property=\"og:title\"(.+?)\" />");
                Matcher metaTAGTitleMatcher = metaTAGTitle.matcher(streamMap);

                Pattern metaTAGDescription = Pattern.compile("<meta property=\"og:description\"(.+?)\" />");
                Matcher metaTAGDescriptionMatcher = metaTAGDescription.matcher(streamMap);

                Pattern metaTAGType = Pattern.compile("<meta property=\"og:video:type\"(.+?)\" />");
                Matcher metaTAGTypeMatcher = metaTAGType.matcher(streamMap);


                if(metaTAGVideoSRCPatternMatcher.find())
                {
                    String metaTAG = streamMap.substring(metaTAGVideoSRCPatternMatcher.start(),metaTAGVideoSRCPatternMatcher.end());
                    Pattern srcFind = Pattern.compile("content=\"(.+?)\"");
                    Matcher srcFindMatcher= srcFind.matcher(metaTAG);
                    if(srcFindMatcher.find())
                    {
                        String src = metaTAG.substring(srcFindMatcher.start(),srcFindMatcher.end()).replace("content=","").replace("\"","");
                        Ff.setUrl(src.replace("&amp;","&"));

                        HttpsURLConnection openUrl =(HttpsURLConnection) new URL(src).openConnection();
                        openUrl.connect();
                        long x = openUrl.getContentLength();
                        long fileSizeInKB = x / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        Ff.setSize((fileSizeInMB > 1) ? fileSizeInMB + " MB" : fileSizeInKB + " KB");
                        openUrl.disconnect();
                    }
                }
                else
                {
                    return null;
                }
                if(metaTAGTitleMatcher.find())
                {
                    String author = streamMap.substring(metaTAGTitleMatcher.start(),metaTAGTitleMatcher.end());
                    Log.e("Extractor","AUTHOR :: "+author);

                    author = author.replace("<meta property=\"og:title\" content=\"","").replace("\" />","");

                    Ff.setAuthor(author);
                }
                else
                {
                    Ff.setAuthor("fbdescription");
                }

                if(metaTAGDescriptionMatcher.find())
                {
                    String name = streamMap.substring(metaTAGDescriptionMatcher.start(),metaTAGDescriptionMatcher.end());

                    Log.e("Extractor","FILENAME :: "+name);


                    name = name.replace("<meta property=\"og:description\" content=\"","").replace("\" />","");

                    Ff.setFilename(name);
                }
                else
                {
                    Ff.setFilename("fbdescription");
                }

                if(metaTAGTypeMatcher.find())
                {
                    String ext = streamMap.substring(metaTAGTypeMatcher.start(),metaTAGTypeMatcher.end());
                    Log.e("Extractor","EXT :: "+ext);

                    ext = ext.replace("<meta property=\"og:video:type\" content=\"","").replace("\" />","").replace("video/","");

                    Ff.setExt(ext);
                }
                else
                {
                    Ff.setExt("mp4");
                }

                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(Ff.getUrl(), new HashMap<String, String>());
                    Ff.setDuration(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                }catch (Exception E)
                {
                    Ff.setDuration(0);
                }

            }

        return Ff;
        }
        catch (Exception E)
        {
            E.printStackTrace();
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
        if(facebookFiles!=null) {


            Log.e("FB","URL :: "+facebookFiles.getUrl());
            Log.e("FB","Author :: "+facebookFiles.getAuthor());
            Log.e("FB","Ext :: "+facebookFiles.getExt());
            onExtractionComplete(facebookFiles);
        }
        else
        {
            onExtractionFail("Somthing Wrong...!!");
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        onExtractionFail("Somthing Wrong...!!");
    }

    public void Extractor(Context mContext, String url) {
        this.mContext = mContext;
        this.url = url;
        this.execute();
    }
}
