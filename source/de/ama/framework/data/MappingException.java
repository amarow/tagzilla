/*
 * DBException.java
 *
 * Created on 22. November 2002, 21:59
 */

package de.ama.framework.data;

import de.ama.util.Util;

/**
 *
 * @author  Andreas Marochow
 */
public class MappingException extends RuntimeException {
    public static final int MAPPING_ERROR = 1;
    public static final int OPTIMISTIC_LOCKING_INVALID = 2;
    private int error = MAPPING_ERROR;

    public MappingException(String msg, Throwable e, int error) {
        super(msg + Util.excAsString(e));
        this.error = error;
    }

    public MappingException(String msg, Throwable e){
        this(msg,e,MAPPING_ERROR);
    }

    public MappingException(Throwable e){
        this(e.getMessage(),e,MAPPING_ERROR);
    }

    public MappingException(String msg) {
        this(msg,null,MAPPING_ERROR);
    }

    public MappingException(String msg, int error) {
        this(msg,null,error);
    }


    public boolean hasError(int error) {
        return this.error==error;
    }
}
