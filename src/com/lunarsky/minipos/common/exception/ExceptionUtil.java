package com.lunarsky.minipos.common.exception;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

public class ExceptionUtil {
	private static final Logger log = LogManager.getLogger();
	
	private static final String NAME_CONTRAINT_REGEX = "Duplicate entry '.*' for key 'NAME_CONSTRAINT'";
	private static final String PASSWORD_CONTRAINT_REGEX = "Duplicate entry '.*' for key 'PASSWORD_CONSTRAINT'";
	
	public static void translate(final RuntimeException exception) {

		Throwable throwable = exception;
		
		//Traverse Causes
		do {
			//Constraint Violation Exceptions
			if(throwable instanceof ConstraintViolationException) {
				//Workaround - ConstraintViolationException.getConstraintName() returns null for MySQL
				final ConstraintViolationException cve = (ConstraintViolationException)throwable;
				final SQLException sqle = cve.getSQLException();
				final String constraintName = sqle.getMessage();

				if(constraintName.matches(NAME_CONTRAINT_REGEX)) {
					throw new NameInUseException(constraintName,exception);
				}
				
				if(constraintName.matches(PASSWORD_CONTRAINT_REGEX)) {
					throw new PasswordInUseException(constraintName,exception);
				}				
			}
			throwable = throwable.getCause();
		} while (null != throwable);

		//else throw the original exception
		throw exception;
	}

	//Prevent instantiation
	private ExceptionUtil() {}

}
