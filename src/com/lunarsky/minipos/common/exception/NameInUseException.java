package com.lunarsky.minipos.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NameInUseException extends RuntimeException {
	private static final Logger log = LogManager.getLogger();
	static final long serialVersionUID = 1L;
	
    public NameInUseException(String message, Throwable cause) {
        super(message,cause);
    }
}
