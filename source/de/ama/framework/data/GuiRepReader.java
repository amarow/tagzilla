package de.ama.framework.data;

import de.ama.util.Util;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ama
 * Date: 08.09.2003
 * Time: 15:30:02
 * Die Klasse dient nur zum wrappen von DataProxy und Data. Sie liefert insbesondere
 * in ihrer toString)(-Methode immer die kurze Gui-Representation. Wird in ComboBoxField und LabelField verweendet.
 */

public class GuiRepReader {
        private Object val=null;

        public Object getVal() {
            return val;
        }

        public GuiRepReader(Object val) {
            this.val = val;
        }

        public String toString() {
             if(val instanceof Data){
                return ((Data)val).getGuiRepresentation();
            }else if(val instanceof Date){
                return Util.asString((Date)val);
            }else{
                return val.toString();
            }
        }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuiRepReader)) return false;

        final GuiRepReader guiRepReader = (GuiRepReader) o;

        if (val != null ? !val.equals(guiRepReader.val) : guiRepReader.val != null) return false;

        return true;
    }

    public int hashCode() {
        return (val != null ? val.hashCode() : 0);
    }
}
