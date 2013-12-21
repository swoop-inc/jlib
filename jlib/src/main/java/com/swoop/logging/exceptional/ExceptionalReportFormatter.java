package com.swoop.logging.exceptional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.SystemUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/** 
 * From an ExceptionalReport, produce a string suitable for POSTing to the exceptional API.
 */
public class ExceptionalReportFormatter
{
	private final static SimpleDateFormat dateFormatter =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private Pattern envExcludePattern;

	/**
	 * Filter out environment variables whose names match the given regular expression.
	 */
    public ExceptionalReportFormatter excludeFromEnv(String regex)
	{
        this.envExcludePattern = Pattern.compile(regex);
		return this;
	}

	/**
	 * @param report The exceptional report to format.
	 */
	public String format(final ExceptionalReport report)
	{
		final JSONObject json = new JSONObject();
		json.put("request", getRequestData());
		json.put("application_environment", getAppData());
		json.put("exception", getExceptionData(report));
		json.put("client", getClientData());
		return json.toJSONString();
	}

	private JSONObject getRequestData()
	{
		// Request data is not yet implemented.
		return new JSONObject();
	}

	private JSONObject getAppData()
	{
		final JSONObject appData = new JSONObject();
		// As demonstrated in exceptional.io documentation:
		put(appData, "language", "java");
		put(appData, "language_version", SystemUtils.JAVA_VERSION);
		put(appData, "application_root_directory", System.getProperty("user.dir"));
		put(appData, "env", getEnvData());
		// Extensions:
		put(appData, "os", SystemUtils.OS_NAME);
		put(appData, "os_version", SystemUtils.OS_VERSION);
		put(appData, "arch", SystemUtils.OS_ARCH);
		put(appData, "local_language", SystemUtils.USER_LANGUAGE);
		put(appData, "local_country", SystemUtils.USER_COUNTRY);
		put(appData, "time_zone", SystemUtils.USER_TIMEZONE);
		return appData;
	}

	private JSONObject getEnvData()
	{
		final JSONObject envData = new JSONObject();
		for (Map.Entry<String,String> entry : System.getenv().entrySet()) {
			if (envExcludePattern == null || !envExcludePattern.matcher(entry.getKey()).matches()) {
				put(envData, entry.getKey(), entry.getValue());
			}
		}
		return envData;
	}

	private JSONObject getExceptionData(final ExceptionalReport report)
	{
		final JSONObject exceptionData = new JSONObject();
		// Required:
		put(exceptionData, "message", report.getMessage());
		put(exceptionData, "exception_class", report.getExceptionClass());
		put(exceptionData, "backtrace", toJsonArray(report.getBacktrace()));
		put(exceptionData, "occurred_at", getIso8601Time());
		// Extensions:
		put(exceptionData, "log_level", report.getLogLevel());
		put(exceptionData, "method_name", report.getMethodName());
		put(exceptionData, "line_number", report.getLineNumber());
		put(exceptionData, "thread_name", report.getThreadName());
		return exceptionData;
	}

	public String getIso8601Time()
	{
		// Java doesn't handle ISO8601 nicely: need to add ':' manually
		final String result = dateFormatter.format(new Date());
		return result.substring(0, result.length() - 2) + ":"
				+ result.substring(result.length() - 2);
	}
	
	private JSONObject getClientData()
	{
		final JSONObject clientData = new JSONObject();
		// As demonstrated in exceptional.io documentation:
		clientData.put("name", ExceptionalClient.NAME);
		clientData.put("version", ExceptionalClient.VERSION);
		clientData.put("protocol_version", ExceptionalClient.PROTOCOL_VERSION);
		return clientData;
	}

	public static JSONArray toJsonArray(String[] array)
	{
		JSONArray jArray = new JSONArray();
		for (String str : array) {
			jArray.add(str);
		}
		return jArray;
	}

	private static void put(JSONObject json, String key, Object value)
	{
		if (value != null) {
			json.put(key, value);
		}
	}
}
