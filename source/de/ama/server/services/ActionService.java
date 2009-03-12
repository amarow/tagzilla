
package de.ama.server.services;

import de.ama.server.actions.ServerAction;

/**
 * User: x
 * Date: 13.10.2008
 */
public interface ActionService {
    public static String NAME = "ActionService";

    public ServerAction execute(ServerAction asa);
}
