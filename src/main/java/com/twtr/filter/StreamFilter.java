package com.twtr.filter;

import twitter4j.FilterQuery;

/**
 * 
 * @author pchaturvedi
 *
 */
public class StreamFilter {

	private String[] trackingKeyWords;
	
	public StreamFilter(String[] keywords){
		this.trackingKeyWords = keywords;
	}
	
	public FilterQuery getFilterQuery(){
		FilterQuery filter = new FilterQuery();
		filter.track(trackingKeyWords);
		return filter;
	}
}
