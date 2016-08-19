package com.lunarsky.minipos.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordInUseException extends RuntimeException {
	private static final Logger log = LogManager.getLogger();
	static final long serialVersionUID = 1L;
	
    public PasswordInUseException(String message, Throwable cause) {
        super(message,cause);
    }
}
