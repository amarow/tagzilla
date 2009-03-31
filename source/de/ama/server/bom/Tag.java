package de.ama.server.bom;

import de.ama.db.PersistentMarker;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 14:53:38
 * To change this template use File | Settings | File Templates.
 */
public class Tag implements PersistentMarker {
    private String path;
    private String tag;
    private int weight;
    private int x;
    private int y;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
