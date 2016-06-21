package com.twtr.processor;

import java.util.Map;

/**
 * 
 * @author pchaturvedi
 *
 */
public interface QueueService {
	public void enqueTweet(final String tweet);
	public boolean isTweetQueueEmpty();
	public String deQueueTweet();
	public void addHashTag(final String hashtag);
	Map<String, Integer> getTopNHashtags(int topNHashtags);
}
