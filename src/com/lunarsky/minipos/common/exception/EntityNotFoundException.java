package com.lunarsky.minipos.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityNotFoundException extends RuntimeException {
	private static final Logger log = LogManager.getLogger();
	static final long serialVersionUID = 1L;
	
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
