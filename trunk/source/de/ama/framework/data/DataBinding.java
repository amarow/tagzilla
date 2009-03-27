/*
 * Binding.java
 *
 * Created on 31. August 2003, 12:03
 */

package de.ama.framework.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  ama
 * DataBinding hält alle MethodBindinds in einer Hashmap zusammen. Die ganze Sache ist nur wegen der schlechten Performance
 * bei reflection unter java 1.3.x entstanden. Es gibt für jede Bo- respective Data-Klasse ein MethodBinding. In einem MethodBinding
 * werden die benötigten Methoden eines Bo's und die Fields eines Data's zusammengehalten.
 *   Bo  <--- MethodBinding---> Data 
 */
public class DataBinding {

    ////////////////////////////////////////////////////////////////////////////////

    private Map methodBindings = new HashMap();
    
    public MethodBinding getMethodeBinding(String key, Object bo, Object data)throws MappingException{
        if(bo==null){
            throw new MappingException("Bo == null " + data!=null?data.getClass().getName():" and data == null\n No Arg Constructor available ???");
        }
        if(data==null){
            throw new MappingException("Data == null" + bo!=null?bo.getClass().getName():" and bo == null");
        }

        MethodBinding mb = (MethodBinding)methodBindings.get(key);
        if(mb==null){
            mb = new MethodBinding(bo.getClass() , data.getClass() , key );
            methodBindings.put(key,mb);
        }
        return mb;
    }

    //////////////////////////////////////////////////////////////////////////////////
    
    
    private static Map bindings = new HashMap();
    public  static DataBinding getBinding(Data data){
        DataBinding db = (DataBinding)bindings.get(data.getClass());
        if(db==null){
            db=new DataBinding();
            bindings.put(data.getClass(),db);
        }
        return db;
    }
    
}


//    /**
//     * Methode zum automatischen Mappen von Bo-values in Data Values
//     * @param bo  das Bo aus dem gelesen wird
//     * @param key  der Methoden-Schlüssel. Er wird benutzt um Getter/Setter-Namen zu generieren
//     */
//    public static void readBo(Object bo, String key, Data data)throws MappingException{
//        DataBinding db = getBinding(data);
//        MethodBinding mb = db.getMethodeBinding(key,bo, data);
//        mb.setDataValue(data, mb.getBoValue(bo));
//    }
//    
//    public static void writeBo(Object bo, String key, Data data)throws MappingException{
//        DataBinding db = getBinding(data);
//        MethodBinding mb = db.getMethodeBinding(key,bo, data);
//        mb.setBoValue(bo, mb.getDataValue(data));
//    }
