package org.onesun.sdi.core.api;

import java.util.List;
import java.util.Map;

import org.onesun.sdi.core.metadata.Metadata;

public interface DataProfiler {
	void setData(List<Map<String, String>> data);
	void setMetadata(Metadata metadata);
	void execute();
}
