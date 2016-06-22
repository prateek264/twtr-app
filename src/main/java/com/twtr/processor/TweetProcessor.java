package com.twtr.processor;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;


/**
 * Extracts hashtags from messages.
 */
public class TweetProcessor implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(TweetProcessor.class);
    private final TweetQueueManager tweetQueueManager;


    public TweetProcessor(final TweetQueueManager tweetQueueManager) {
        this.tweetQueueManager = tweetQueueManager;
    }

    /**
     * this method reads tweet from the queue
     */
    public void run() {
        synchronized (tweetQueueManager) {
            while (true) {
                if (!tweetQueueManager.isTweetQueueEmpty()) {
                    LOGGER.debug("Extracting hashtags from tweet queue.");
                    extractHashtagsFromTweet(tweetQueueManager.deQueueTweet());
                } else {
                    LOGGER.debug("No tweets in the queue, waiting...");
                    try {
                    	tweetQueueManager.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.error("Thread interrupted: " + ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    /**
     * this method extracts the hashtag from each tweet, and adds it to the map
     * @param tweet
     */
    private void extractHashtagsFromTweet(final String tweet) {
    	/**
    	 * Tweet delimiter reference: https://dev.twitter.com/streaming/overview/processing
    	 * String tokenizer reference: http://docs.oracle.com/javase/7/docs/api/java/util/StringTokenizer.html#StringTokenizer%28java.lang.String%29 
    	 */
        String deliminator = " \t\n\r\f,.:;?![]'";
        StringTokenizer tokenizer = new StringTokenizer(tweet, deliminator);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith("#")) {
            	tweetQueueManager.addHashTag(token);
            }
        }
    }
}
