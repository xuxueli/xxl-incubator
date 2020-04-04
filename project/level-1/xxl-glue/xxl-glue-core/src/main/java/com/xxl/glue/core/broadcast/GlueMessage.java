package com.xxl.glue.core.broadcast;

import java.util.Set;

/**
 * Message for Glue broadcast 
 * @author xuxueli 2016-5-20 22:21:06
 */
public class GlueMessage {

	private String glueName;		// glue key (group_name)
	private Set<String> appnames;	// appnames, for glue need to fresh
	private long version;			// version, for cache

	/*public enum GlueMessageType{ CLEAR_CACHE, DELETE }
	private GlueMessageType type; */

	public String getGlueName() {
		return glueName;
	}

	public void setGlueName(String glueName) {
		this.glueName = glueName;
	}

	public Set<String> getAppnames() {
		return appnames;
	}

	public void setAppnames(Set<String> appnames) {
		this.appnames = appnames;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
