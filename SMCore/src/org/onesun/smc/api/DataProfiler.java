package org.onesun.smc.api;

import java.util.List;
import java.util.Map;

import org.onesun.smc.core.metadata.Metadata;

public interface DataProfiler {
	void setData(List<Map<String, String>> data);
	void setMetadata(Metadata metadata);
	void execute();
}
