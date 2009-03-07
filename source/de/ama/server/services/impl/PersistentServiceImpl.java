
package de.ama.server.services.impl;

import de.ama.db.DB;
import de.ama.db.OidIterator;
import de.ama.db.Query;
import de.ama.server.services.PersistentService;
import de.ama.server.services.UserService;
import de.ama.util.Ini;

import java.util.List;

/**
 * User: x
 * Date: 19.05.2008
 */
public class PersistentServiceImpl implements PersistentService {

    public PersistentServiceImpl() {
        start();
    }


    public void start(){
        String host = Ini.getString("db.host","localhost");
        String user = Ini.getString("db.user","root");
        String catalog = Ini.getString("db.catalog","tagzilla");
        String pwd = Ini.getString("db.pwd","");

        new DB(host, user, catalog, pwd);

        join("tagzilla");
        DB.session().createSequenze(UserService.USER_ID_SEQUENZE, 1);
        commit();
        leave();
    }

    public void stop(){
        DB.get().disconnect();
    }

    public void join(String catalog){
        DB.joinCatalog(catalog);
    }

    public void leave(){
        DB.leaveCatalog();
    }

    public Object getObject(String oid) {
        return DB.session().getObject(oid);
    }

    public List getObjects(Query query) {
        return DB.session().getOidIterator(query).asList();
    }

    public long getObjectCount(Query q) {
        return DB.session().getObjectCount(q);
    }

    public Object getObject(Query query, boolean exact) {
        OidIterator oidIterator = DB.session().getOidIterator(query);
        if(exact && oidIterator.size()>1) {
            throw new RuntimeException("more than one Object for ["+ query.toString()+"] found in DB");
        }
        if(oidIterator.size()<1) {
            if(exact){
                throw new RuntimeException("no Object for ["+ query.toString()+"] found in DB");
            }
            return null;
        }
        return oidIterator.next();
    }

    public String makePersistent(Object o) {
        DB.session().setObject(o);
        return DB.session().getOidString(o);
    }

    public String getOidString(Object o) {
        return DB.session().getOidString(o);
    }

    public void delete(String oid) {
       DB.session().delete(oid);
    }

    public void commit(){
       DB.session().commit();
    }

    public void rollback() {
        DB.session().rollback();
    }

    public long getNextNumber(String key) {
        return DB.session().getSequenzeNext(key);
    }
}
