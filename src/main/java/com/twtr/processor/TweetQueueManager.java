package com.twtr.processor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;

/**
 * A message queue and the hashtags extracted.
 */
public class TweetQueueManager implements QueueService {
    private final static Logger LOGGER = Logger.getLogger(TweetQueueManager.class);
    private final Multiset<String> hashTags = TreeMultiset.create();
    private final Queue<String> tweetQueue = new LinkedList<String>();
    

    /**
     * Add a message to the queue to be processed.
     *
     * @param message the message.
     */
    public void enqueTweet(final String tweet) {
    	tweetQueue.add(tweet);
    	LOGGER.debug(String.format("Tweet added to Queue %s", tweet));
    }


    /**
     * Returns true if the message queue is empty.
     *
     * @return is queue empty.
     */
    public boolean isTweetQueueEmpty() {
        return tweetQueue.isEmpty();
    }

    /**
     * Returns and removes a message from the queue.
     *
     * @return the message.
     */
    public String deQueueTweet() {    	
        return tweetQueue.remove();
    }

    /**
     * Adds a hashtag to the collection.
     *
     * @param hashtag the hashtag.
     */
    public void addHashTag(final String hashtag) {
        hashTags.add(hashtag);
    }

    /**
     * Get the top hashtags.
     *
     * @return the top hashtags and occurrence of each.
     */
    public Map<String, Integer> getTopNHashtags(int topNHashtags) {
        Set<String> sortedSet = Multisets.copyHighestCountFirst(hashTags).elementSet();
        Iterator<String> iterator = sortedSet.iterator();
        Map<String, Integer> topNHashTags = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < topNHashtags; i++) {
            if (iterator.hasNext()) {
                String term = iterator.next();
                topNHashTags.put(term, hashTags.count(term));
            } else {
                break;
            }
        }

        return topNHashTags;
    }

}
