package com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.chat_actor_community.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * Created by Eleazar (eorono@protonmail.com) on 3/04/16.
 */
public class ChatActorDisconnectingFailedException extends FermatException {

    private static final String DEFAULT_MESSAGE = "CHAT ACTOR DISCONNECTING FAILED EXCEPTION";

    public ChatActorDisconnectingFailedException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public ChatActorDisconnectingFailedException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

}
