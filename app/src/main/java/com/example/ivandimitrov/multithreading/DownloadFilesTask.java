package com.example.ivandimitrov.multithreading;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ivandimitrov.multithreading.NodeTypes.SiteNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hugo.weaving.DebugLog;

/**
 * Created by Ivan Dimitrov on 12/7/2016.
 */

public class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
    public final static String MY_URL = "http://stackoverflow.com/questions/35846544/java-lang-runtimeexception-an-error-occured-while-executing-doinbackground";
    public static final String LINK_NAME = "Link";
    private Activity activity;
    private StringBuffer buffer;
    private File htmlFile;
    private int linkCount = 0;
    ArrayList<SiteNode> allNodes = new ArrayList<SiteNode>();
    CustomListener listener;

    public DownloadFilesTask(Activity activity, CustomListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    protected Long doInBackground(URL... urls) {
        long totalSize = 0;
        URL urlObj = null;
        try {
            urlObj = new URL(MY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SiteNode root = new SiteNode(null, 0, urlObj);
        walkin(root);
        for (SiteNode tempNode : allNodes) {
            Log.d("MYLINKS", "" + tempNode.getNodeName());
        }
        return totalSize;
    }

    @DebugLog
    private void walkin(SiteNode node) {
        linkCount++;
        node.setNodeName(LINK_NAME + linkCount);
        String htmData = extractHTMLData(node);
        node.setFile(writeToInternalFile(htmData, node.getNodeName()));
        extractLinksFromHTML(node);
        if (node.getDebth() > 1) {
            return;
        }
        List<SiteNode> childNodes = node.getChildNodes();
        if (childNodes.size() > 3) {
            childNodes = childNodes.subList(0, 3);
        }
        for (SiteNode tempNode : childNodes) {
            allNodes.add(tempNode);
            publishProgress();
            walkin(tempNode);
        }
    }

    @DebugLog
    private String extractHTMLData(SiteNode node) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        try {
            try {
                urlConnection = (HttpURLConnection) node.getUrl().openConnection();
                is = urlConnection.getInputStream();
            } catch (Exception e) {
                return "";
            }
            buffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @DebugLog
    private File writeToInternalFile(String data, String fileName) {
        try {
            FileOutputStream fOut = activity.openFileOutput(fileName + ".html", activity.getApplicationContext().MODE_PRIVATE);
            String path = activity.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + fileName + ".html";
            htmlFile = new File(path);
            htmlFile.setReadable(true, false);
            try {
                fOut.write(data.getBytes());
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return htmlFile;
    }

    @DebugLog
    private void extractLinksFromHTML(SiteNode node) {
        Scanner scan = null;
        StringBuffer buffer = new StringBuffer();
        StringBuffer links = new StringBuffer();
        String regex = "<a href\\s?=\\s?\"([^\"]+)\">";
        Pattern pattern = Pattern.compile(regex);
        try {
            scan = new Scanner(node.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scan.hasNext()) {
            buffer.append(scan.nextLine());
        }
        Matcher matcher = pattern.matcher(buffer.toString());
        while (matcher.find()) {
            String link = matcher.group(1);
            links.append(link + "\n");

            try {
                URL urlObj = new URL(link);
                node.addChild(new SiteNode(node, node.getDebth() + 1, urlObj));

            } catch (MalformedURLException e) {
                try {
                    node.addChild(new SiteNode(node, node.getDebth() + 1, new URL("http://" + link)));
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    protected void onProgressUpdate(Integer... progress) {
        listener.onElementReceived(allNodes.get(allNodes.size() - 1));
//        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //listener.onDataReceived(allNodes);
    }

    public interface CustomListener {
        public void onDataReceived(ArrayList<SiteNode> siteNodes);

        public void onElementReceived(SiteNode siteNode);
    }
}

