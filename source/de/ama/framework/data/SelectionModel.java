package de.ama.framework.data;

import de.ama.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Andreas Marochow
 * Date: 15.07.2003
 */
public class SelectionModel implements Serializable{

    private List   selections;
    private String condition;
    private String  type;

   /////////////////////////// C'tor //////////////////////////////////////

    public SelectionModel(SelectionModel sm) {
        setSelections(sm.getSelections());
        setCondition(sm.getCondition());
        type=sm.type;
    }


    public SelectionModel(Selection s) {
        setSelection(s);
    }

    public SelectionModel(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    ////////////////// Conditions ///////////////////////////////

    public String getCondition()                {    return Util.saveToString(condition);  }
    public void setCondition(String condition)  {    this.condition = condition;   }
    public boolean hasCondition()               {    return condition!=null && condition.length() > 0;    }

    /////////////////////////////////////////////////////


    public int getSelectionSize(){
        return getSelections().size();
    }

    public void setSelection(Selection selection){
        selections = null;
        getSelections().add(selection);
    }

    public void setSelections(List selectionList){
        selections=selectionList;
    }

    public Selection getSelection(int index){
        if( index>=0 && getSelectionSize()>index){
           return (Selection)getSelections().get(index);
        }else{
           throw new IllegalStateException("SelectionModel has only "+getSelectionSize()+ " entrys, index "+index+" is out of bounds");
        }
    }

    public List getSelections(){
        if(selections == null){
            selections = new ArrayList();
        }
        return selections;
    }

    public Selection getSingleSelection(){
        return getSelection(0);
    }

    public void clear() {
      getSelections().clear();
    }


    public String toString(){
        return "SelectionModel("+hashCode()+") size("+getSelectionSize()+") type:"+getType();
    }

    public void removeSelection(Selection selection) {
        getSelections().remove(selection);
    }

}
