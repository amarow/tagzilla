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
    static final long serialVersionUID = -1L;
 
    private List   selections;
    private String containerId;
    private String condition;
    private Class  type;

   /////////////////////////// C'tor //////////////////////////////////////

    public SelectionModel(SelectionModel sm) {
        setSelections(sm.getSelections());
        setContainerId(sm.getContainerId());
        setCondition(sm.getCondition());
        type=sm.type;
    }


    public SelectionModel(Selection s) {
        setSelection(s);
    }

    public SelectionModel(Class type) {
        this.type = type;
    }

    public Class getType(){
        if(type==null && getSelectionSize()>0){
            return getSelection(0).getType();
        }
        return type;
    }

    ////////////////// Conditions ///////////////////////////////

    public String getCondition()                {    return Util.saveToString(condition);  }
    public void setCondition(String condition)  {    this.condition = condition;   }
    public boolean hasCondition()               {    return condition!=null && condition.length() > 0;    }

    public String getContainerId()                {  return containerId;    }
    public void setContainerId(String containerId){  this.containerId = containerId;    }
    public boolean inContainer()                  {  return containerId!=null && containerId.length()> 0;    }

    /////////////////////////////////////////////////////


    public int getSelectionSize(){
        return getSelections().size();
    }

    public void setSelection(Selection selection){
        selections = null;
        getSelections().add(selection);
    }

    public void setSelections(List selectionList){
//        if(selectionList!=null){
//           this.selections=new ArrayList( selectionList );
//        }else{
//           this.selections=new ArrayList();
//        }
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
      setContainerId(null);
    }


    public String toString(){
        return "SelectionModel("+hashCode()+") size("+getSelectionSize()+") type:"+getType();
    }

    public void removeSelection(Selection selection) {
        getSelections().remove(selection);
    }

}
