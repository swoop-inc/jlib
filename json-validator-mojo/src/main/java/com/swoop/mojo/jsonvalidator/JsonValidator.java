package com.swoop.mojo.jsonvalidator;

import com.fasterxml.jackson.core.JsonFactory;
import static com.fasterxml.jackson.core.JsonParser.Feature.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonValidator
{
	private JsonFactory jsonFactory;

	public JsonFactory getJsonFactory()
	{
		if (jsonFactory == null) {
			jsonFactory = new JsonFactory();
			jsonFactory.setCodec(new ObjectMapper() {});
			jsonFactory.enable(ALLOW_UNQUOTED_FIELD_NAMES);
			jsonFactory.enable(ALLOW_COMMENTS);
		}
		return jsonFactory;
	}

	public void validate(File file)
		throws IOException
	{
		getJsonFactory().createJsonParser(file).readValueAs(Object.class);
	}
}
