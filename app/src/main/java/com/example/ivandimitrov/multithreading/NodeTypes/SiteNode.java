package com.example.ivandimitrov.multithreading.NodeTypes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ivan Dimitrov on 12/8/2016.
 */

public class SiteNode implements Parcelable {
    public static final int MAX_DEBTH = 3;
    private SiteNode perentNode;
    private File file;
    private ArrayList<SiteNode> childNodes = new ArrayList<SiteNode>();
    private int debth;
    private URL url;
    private String nodeName = "DUMMY";

    public SiteNode(SiteNode perentNode, int debth, URL url) {
        this.perentNode = perentNode;
        this.debth = debth;
        this.url = url;
    }

    protected SiteNode(Parcel in) {
        perentNode = in.readParcelable(SiteNode.class.getClassLoader());
        childNodes = in.createTypedArrayList(SiteNode.CREATOR);
        debth = in.readInt();
        nodeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(perentNode, flags);
        dest.writeTypedList(childNodes);
        dest.writeInt(debth);
        dest.writeString(nodeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SiteNode> CREATOR = new Creator<SiteNode>() {
        @Override
        public SiteNode createFromParcel(Parcel in) {
            return new SiteNode(in);
        }

        @Override
        public SiteNode[] newArray(int size) {
            return new SiteNode[size];
        }
    };

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public URL getUrl() {
        return url;
    }

    public void addChild(SiteNode newChildNode) {
        childNodes.add(newChildNode);
    }

    public ArrayList<SiteNode> getChildNodes() {
        return childNodes;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public int getDebth() {
        return debth;
    }

}
