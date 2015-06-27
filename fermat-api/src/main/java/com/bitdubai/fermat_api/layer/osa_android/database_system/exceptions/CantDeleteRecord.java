package com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions;

/**
 * Created by mati
 */
public class CantDeleteRecord extends DatabaseSystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1055830576804455604L;

	public static final String DEFAULT_MESSAGE = "CAN'T DELETE RECORD";

	public CantDeleteRecord(final String message, final Exception cause, final String context, final String possibleReason) {
		super(message, cause, context, possibleReason);
	}

	public CantDeleteRecord(final String message, final Exception cause) {
		this(message, cause, "", "");
	}

	public CantDeleteRecord(final String message) {
		this(message, null);
	}

	public CantDeleteRecord(final Exception exception) {
		this(exception.getMessage());
		setStackTrace(exception.getStackTrace());
	}

	public CantDeleteRecord() {
		this(DEFAULT_MESSAGE);
	}
}