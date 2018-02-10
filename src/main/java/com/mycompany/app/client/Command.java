package com.mycompany.app.client;

import com.mycompany.app.json.Req;

/**
 * Created by admin on 20.06.17.
 */
@FunctionalInterface
public interface Command {
    public Req apply(int rndSequence, String input);


}
