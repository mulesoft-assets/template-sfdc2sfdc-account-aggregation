package org.mule.templates.builders;

import java.util.HashMap;
import java.util.Map;

public class SfdcEntityBuilder {

	private Map<String, String> fields;

	public SfdcEntityBuilder() {
		this.fields = new HashMap<String, String>();
	}

	public static SfdcEntityBuilder aContact() {
		return new SfdcEntityBuilder();
	}

	public SfdcEntityBuilder with(String field, String value) {
		SfdcEntityBuilder contactCopy = new SfdcEntityBuilder();
		contactCopy.fields.putAll(this.fields);
		contactCopy.fields.put(field, value);
		return contactCopy;
	}

	public Map<String, String> build() {
		return fields;
	}
}
