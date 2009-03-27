package de.ama.framework.data;

/**
 *
 * @author  ama
 */
public class StringData extends Data{
    public String value="";

    public StringData() {
        value="";
    }

    public StringData(Object o) {
        if(o!=null){
          value=o.toString();
        }else{
            value="";
        }
    }

    public String getGuiRepresentation() {
        return value;
    }

    public String getBoClassName(){
        return "java.lang.String";
    }

    public String toString() {
        return value.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringData)) return false;

        final StringData stringData = (StringData) o;

        if (value != null ? !value.equals(stringData.value) : stringData.value != null) return false;

        return true;
    }

    public int hashCode() {
        return (value != null ? value.hashCode() : 0);
    }

    /**
     * Ist nur für absolut seltene Fälle von Nöten, in denen die Build
     * die Zusätzlichen Prüfungen bezüglich Reflection  BO <->  Data unterlaesst.
     * Es gibt kein getValue() in java.lang.String
     * @return
     */
    public boolean excludeFromBuildValidation() {
        return true;
    }
}
