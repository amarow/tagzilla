package de.ama.framework.data;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 12.11.2003
 * Time: 17:24:38
 * To change this template use Options | File Templates.
 */
public class QueryDescription {

    private String startCondition;
    private String methodKey;
    private String path;
    private Class  attributeType;


    public QueryDescription(String methodKey, Class attributeType) {
        this(methodKey,methodKey,attributeType, null);
    }

    public QueryDescription(String methodKey, String path, Class attributeType, String startCondition) {
        this.startCondition = startCondition;
        this.methodKey = methodKey;
        this.path = path;
        this.attributeType = attributeType;
    }

    public QueryDescription(String methodKey, String path, Class attributeType) {
        this(methodKey,path,attributeType, "");
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getPath() {
        return path;
    }

    public Class getAttributeType() {
        return attributeType;
    }

    public String getStartCondition() {
        return startCondition;
    }

    public boolean hasStartCondition(){
        return startCondition != null;
    }
}
