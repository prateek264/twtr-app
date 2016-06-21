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

    
    public void run() {
        synchronized (tweetQueueManager) {
            while (true) {
                if (!tweetQueueManager.isTweetQueueEmpty()) {
                    LOGGER.debug("Extracting hashtags from message.");
                    extractHashtagsFromTweet(tweetQueueManager.deQueueTweet());
                } else {
                    LOGGER.debug("The queue is empty. Waiting...");
                    try {
                    	tweetQueueManager.wait();
                    } catch (InterruptedException ex) {
                        LOGGER.error("InterruptedException thrown: " + ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private void extractHashtagsFromTweet(final String tweet) {
        String deliminator = " \t\n\r\f,.:;?![]'"; //adds punctuation marks to default set
        StringTokenizer tokenizer = new StringTokenizer(tweet, deliminator);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith("#")) {
            	tweetQueueManager.addHashTag(token);
            }
        }
    }
}
