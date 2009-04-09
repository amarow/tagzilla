package de.ama.server.actions;

import de.ama.framework.data.Data;
import de.ama.framework.data.DataMapper;
import de.ama.framework.data.DataTable;
import de.ama.framework.data.MappingException;
import de.ama.server.bom.User;
import de.ama.server.services.Environment;
import de.ama.server.services.PersistentService;
import de.ama.util.Util;

import java.util.Collection;


/**
 * User: x
 * Date: 28.04.2008
 *

 *
 */
public class ServerAction {

    // ************** ActionScript Members *********************

    public Long   userId;
    public String catalog;

    public Object data;
    public String message;
    public String detailErrorMessage;

    // *********************************************************

    private static ThreadLocal currentActionHolder;
    private User    user;

    private boolean versionMismatch;
    private boolean dontCommit;

    public ServerAction() {
    }


    public static void setCurrent(ServerAction a) {
        if (currentActionHolder == null) {
            currentActionHolder = new ThreadLocal();
        }
        currentActionHolder.set(a);
    }

    public static ServerAction getCurrent() {
        return (ServerAction)currentActionHolder.get();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void execute() throws Exception {
    }

    public String getCatalog(){
        return "tagzilla";
    }

    public void commit(){
        Environment.getPersistentService().commit();
    }

    public void rollback(){
        Environment.getPersistentService().rollback();
    }



    public boolean needsUser() {
        return true;
    }

    public String getName() {
        return Util.getUnqualifiedClassName(getClass());
    }


    public void setMessage(String m) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void mapBoToData(Object bo, Data data)  {
        data.getMapper().readDataFromBo(bo, data, DataMapper.FULL_OBJECT);
    }


    public DataTable mapBosToDataTable(Collection bos, Data data){
        return data.getMapper().createFromBoList(data,bos,false);
    }


     /**
     * transferiert die Daten eines Data-Graphen in den Bo-Graphen.
     * Wichtig ! Um Auskunft über Versionsmismatch zu bekommen, muß nach dem save() über die
     * Methode hasVersionMismatch() kontrolliert werden ob ein Versionsmismatch vorlag.
     * @param data ,das gemapped werden soll
     * @return ein refeshter Data_Graph.
     */

    public Data mapDataToBo(Data data, boolean commit)  {
        versionMismatch = false;

        if (data == null) {
            throw new RuntimeException("NO DATA in SaveBoAction");
        }

        Object obj = null;

        String msg = data.getBoClassName() + " " + data.getOidString();
        if (data.isNew()) {
            obj = data.createEmptyBo();
            if (obj == null) {
                throw new RuntimeException("could not CREATE BO : " + msg);
            }
//            getPersistentService().makePersistent(obj);
        } else {
            obj = Environment.getPersistentService().getObject(data.getOidString());
            if (obj == null) {
                throw new RuntimeException("could not FIND BO TO UPDATE : " + msg);
            }
        }

        try {
            DataMapper mapper = data.getMapper();
            mapper.checkVersion(obj, data);
//          checkUniqueness(obj,data);
            if (data.isNew()) {
                Environment.getPersistentService().makePersistent(obj);
            }
            mapper.writeDataToBo(obj, data);

            if(!commit){
                return data;
            }

            commit();
            return reload(data.getClass(), obj);
        } catch (MappingException me) {
            rollback();

            // SonderFall MappingException mit Konkurierendem Zugriff.
            if (me.hasError(MappingException.OPTIMISTIC_LOCKING_INVALID)) {
                versionMismatch = true;
                return reload(data.getClass(), obj);
            }

            throw new RuntimeException("MAPPING EXCEPTION : ", me);
        }

    }

    public Data reload(Class type, Object obj)  {
        try {
            Object o = Environment.getPersistentService().getObject(getOid(obj));
            Data data = Data.createEmptyData(type);
            data = data.getMapper().readDataFromBo(obj, data, DataMapper.FULL_OBJECT);
            return data;
        } catch (Exception e) {
            rollback();
            throw new RuntimeException("could not reload Bo from DB <" + type.getName() + ">  ",e);
        }
    }

    private String getOid(Object obj) {
        return Environment.getPersistentService().getOidString(obj);
    }

    public  void refreshObject(Object toRefresh) {
        if (dontCommit) {
//            if(TRACE.ON()){ TRACE.add(this,"refreshObject: dontCommit=true !"); }
            return;
        } else {
            Environment.getPersistentService().refresh(toRefresh);
        }
    }
}
