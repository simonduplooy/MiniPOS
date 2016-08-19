package com.lunarsky.minipos.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Util {
	
	public static void setLogLevel(final String module, final Level level) {
		
		String moduleRenamed = module.replaceAll("/", ".");
		LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
		AbstractConfiguration configuration = (AbstractConfiguration) ctx.getConfiguration();
		if (configuration.getLogger(moduleRenamed) != null) {
			LoggerConfig loggerConfig = configuration.getLoggerConfig(moduleRenamed);
			loggerConfig.setLevel(level);
		} else {
			LoggerConfig loggerConfig = new LoggerConfig(moduleRenamed, level, true);
			configuration.addLogger(moduleRenamed, loggerConfig);
		}
		ctx.updateLoggers(configuration);
		
	}
	
}
