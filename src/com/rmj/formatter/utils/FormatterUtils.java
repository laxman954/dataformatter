package com.rmj.formatter.utils;

import org.codehaus.jackson.map.ObjectMapper;

public class FormatterUtils {
	static ObjectMapper mapper = new ObjectMapper();;

	public static String fomatJSON(String data) {

		return getJSON(data);
	}

	private static String getJSON(String data) {
		String jsonData ;
		try {
			Object json = mapper.readValue(data, Object.class);
			jsonData = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (Exception e) {
			jsonData = "Invalid Input";
		}
		return jsonData;
	}

}
