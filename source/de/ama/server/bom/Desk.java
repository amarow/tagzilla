package de.ama.server.bom;

import de.ama.db.PersistentMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 26.03.2009
 * Time: 14:53:38
 * To change this template use File | Settings | File Templates.
 */
public class Desk implements PersistentMarker {
    private  List objects = new ArrayList();
    private  String name;
    private  int sliderPos;

    public int getSliderPos() {
        return sliderPos;
    }

    public void setSliderPos(int sliderPos) {
        this.sliderPos = sliderPos;
    }

    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}