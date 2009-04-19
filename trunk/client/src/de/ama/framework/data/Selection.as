package de.ama.framework.data {

public class Selection {
    private var oidString:String;

    public function Selection(data:Data = null) {
        if (data) {
            setData(data);
        }
    }

    public function getOidString():String {
        return oidString;
    }

    public function setData(data:Data) {
        this.oidString = data.oidString;
    }

}
}