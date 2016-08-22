package com.lunarsky.minipos.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityInUseException extends RuntimeException {
	private static final Logger log = LogManager.getLogger();
	static final long serialVersionUID = 1L;
	
    public EntityInUseException(String message) {
        super(message);
    }
    
    public EntityInUseException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
