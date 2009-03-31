/*
 * Binding.java
 *
 * Created on 31. August 2003, 12:03
 */

package de.ama.framework.data;

import de.ama.util.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author  ama
 * DataBinding hält alle MethodBindinds in einer Hashmap zusammen. Die ganze Sache ist nur wegen der schlechten Performance
 * bei reflection unter java 1.3.x entstanden.
 * Es gibt für jede Bo- respective Data-Klasse ein MethodBinding. In einem MethodBinding werden die benötigten Methoden
 * eines Bo's und die Fields eines Data's zusammengehalten.
 *
 *      Bo  <--- MethodBinding---> Data
 *
 */

public class MethodBinding {

    private Method boGetter,boSetter;
    private Field dataField;
    private Class setterParamClass;
    private String key;

    public MethodBinding(Class boClass, Class dataClass, String key) throws MappingException {
        //System.out.println("try to create new MethodBinding for "+key+".......");
        this.key = key;
        // Data-Field
        dataField = Data.findField(dataClass, key);
        if (dataField == null) {
            throw new MappingException("field " + key + " in Class " + dataClass.getName() + " not found.");
        }
        String methodName = "???";
        // Bo-Getter
        try {
            methodName = Util.getMethodeName("get", key);
            boGetter = Util.findMethod(boClass, methodName);
        } catch (Exception e) {
            boGetter = null;
        }

        try {
            if (boGetter == null && (dataField.getType() == Boolean.class || dataField.getType() == boolean.class)) {
                methodName = Util.getMethodeName("is", key);
                boGetter = Util.findMethod(boClass, methodName);
            }
        } catch (Exception e) {
            boGetter = null;
        }

        if (boGetter == null) {
            throw new MappingException("BO-GETTER NOT FOUND [" + methodName + "] in Class " + boClass.getName() + " not found.");
        }

        // Bo-Setter
        // Wenn es keinen Setter gibt wird einer nur Leseoperation ausgegeangen.
        methodName = Util.getMethodeName("set", key);
        boSetter = Util.findMethod(boClass, methodName);
        //System.out.print(" OK. ");
    }

    public String toString() {
        return "BINDING " + key +
                " getter=" + (boGetter != null ? boGetter.getName() : "? ") +
                " setter=" + (boSetter != null ? boSetter.getName() : "? ") +
                " data=" + (dataField != null ? dataField.getName() : "? ");
    }

    public Object getBoValue(Object bo) throws MappingException{
        if (boGetter == null) {
            throw new MappingException("BO-GETTER NOT AVAILABLE :  -------> " + getKey() + " in Class " + bo.getClass().getName());
        }

        try {
            Object value = boGetter.invoke(bo);
            return value;
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
            throw new MappingException("MAPPING EXCEPTION while invoking BO-GETTER [" + Util.getMethodeName("get", getKey()) + "] from " + bo.getClass().getName(), target);
        } catch (Exception e) {
                throw new MappingException("MAPPING EXCEPTION while invoking BO-GETTER [" + Util.getMethodeName("get", getKey()) + "] from " + bo.getClass().getName(), e);
        }
    }

    public void setBoValue(Object bo, Object value) throws MappingException{
        if (boSetter == null) {
            System.out.println("*************************************************************");
            System.out.println("*   no BO-SETTER FOR " + Util.getMethodeName("set", getKey()) + " PROBLEM ???");
            System.out.println("*************************************************************");
            return;
            //throw new MappingException("there is no Setter in BoClass: set -------> "+getKey()+" in "+bo.getClass().getName());
        }

        if (bo == null) {
            throw new MappingException("BO is null can not SET value -------> " + Util.getMethodeName("set", getKey()));
        }

        try {
            boSetter.invoke(bo, new Object[]{value});
        } catch (InvocationTargetException e) {
            Throwable target = e.getTargetException();
                throw new MappingException("MAPPING EXCEPTION while invoking BO-SETTER [" + Util.getMethodeName("set", getKey()) + "] from " + bo.getClass().getName(), target);
        } catch (Exception e) {
                if (value != null) {
                    throw new MappingException("MAPPING EXCEPTION while invoking BO-SETTER [" + Util.getMethodeName("set", getKey()) + "] from " + bo.getClass().getName() + " value-class=" + value.getClass().getName(), e);
                } else {
                    throw new MappingException("MAPPING EXCEPTION while invoking BO-SETTER [" + Util.getMethodeName("set", getKey()) + "] from " + bo.getClass().getName() + " value=null ", e);
                }
        }
    }

    public Object getDataValue(Data data) throws MappingException {
        Object val;
        try {
            val = dataField.get(data);
        } catch (Exception e) {
            throw new MappingException("MAPPING EXCEPTION while READING from " + data.getClass().getName() + " in Field [" + key + "]", e);
        }
        return val;
    }

    public void setDataValue(Data data, Object value) throws MappingException {
        //System.out.println("setDataValue "+toString());
        try {
            dataField.set(data, value);
        } catch (Exception e) {
            throw new MappingException("MAPPING EXCEPTION while WRITING to " + data.getClass().getName() + " in Field [" + key + "]", e);
        }
    }

    public Class getBoSetterType() throws MappingException {
         if(!hasBoSetter()){
             return null;
         }
        try {

         if (setterParamClass == null) {
             Class[] types = boSetter.getParameterTypes();
             if (types==null) {
                 throw new MappingException("BO-SETTER has NO parameters, " + Util.getMethodeName("set", getKey()));
             }else if (types.length != 1) {
                     throw new MappingException("BO-SETTER has not EXACTLY 1 parameter, " + Util.getMethodeName("set", getKey()));
             } else {
                 setterParamClass = types[0];
             }
         }
        } catch (MappingException me) {
            throw me;
        } catch (Exception e) {
            throw new MappingException("\ngetBoSetterType() Fehler bei " + toString(), e);
        }
         return setterParamClass;
     }

     public boolean hasBoSetter(){
       return boSetter!=null;
     }


    public Class getBoGetterType() {
        return boGetter.getReturnType();
    }

    public Class getDataFieldType() {
        return dataField.getType();
    }

    public String getKey() {
        return key;
    }

}


