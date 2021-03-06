package com.bitdubai.fermat_cbp_plugin.layer.network_service.negotiation_transmission.developer.bitdubai.version_1.exceptions;

import com.bitdubai.fermat_api.FermatException;

/**
 * The exception <code>com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.network_service.exceptions.CantReadRecordDataBaseException</code>
 * is thrown when there is an error trying to read a record from database.
 * <p>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 06/10/2015.
 */
public class CantReadRecordDataBaseException extends FermatException {

    public static final String DEFAULT_MESSAGE = "CAN'T READ RECORD DATABASE EXCEPTION";

    public CantReadRecordDataBaseException(String message, Exception cause, String context, String possibleReason) {
        super(message, cause, context, possibleReason);
    }

    public CantReadRecordDataBaseException(Exception cause, String context, String possibleReason) {
        this(DEFAULT_MESSAGE, cause, context, possibleReason);
    }

    public CantReadRecordDataBaseException(String message, String context, String possibleReason) {
        this(message, null, context, possibleReason);
    }

}
