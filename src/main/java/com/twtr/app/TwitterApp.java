package com.twtr.app;

import java.util.Map;

import org.apache.log4j.Logger;

import com.twtr.processor.StreamProcessor;
import com.twtr.processor.TweetProcessor;
import com.twtr.processor.TweetQueueManager;
/**
 * 
 * @author pchaturvedi
 *https://dev.twitter.com/streaming/overview/request-parameters
 *https://dev.twitter.com/oauth/overview/authorizing-requests
 *http://code.tutsplus.com/tutorials/building-with-the-twitter-api-using-real-time-streams--cms-22194
 *http://stackoverflow.com/questions/13907719/simple-twitter-streaming-api-java-oauth-example
 *https://github.com/twitter/hbc
 *https://www.javacodegeeks.com/2013/02/a-walk-through-for-the-twitter-streaming-api.html
 *http://twitter4j.org/en/code-examples.html
 *http://mike.teczno.com/notes/streaming-data-from-twitter.html
 *
 *http://codereview.stackexchange.com/questions/52560/twitter-streaming-client-round2
 *http://codereview.stackexchange.com/questions/52014/twitter-streaming-api-client-identifying-the-top-trending-hashtags-for-a-specif
 */
public class TwitterApp {
	
	private static final Logger LOGGER = Logger.getLogger(TwitterApp.class);
    private final TweetQueueManager tweetQueueManager;
    private StreamProcessor streamProcessor;

    public TwitterApp(final String trackedTerm) {
    	tweetQueueManager = new TweetQueueManager();
        streamProcessor = new StreamProcessor(tweetQueueManager, trackedTerm);
    }

    public static void main(String[] args) {
        if (args.length == 0) {        	
        	LOGGER.info("Tracking twitter stream:");
            TwitterApp application = new TwitterApp("2016");
            application.startStreaming();
            application.startProcessingTweets();
            application.outputTop10Every30Seconds();
        } else {
            System.out.println("Invalid number of arguments. Usage: Lotus [keyword, hashtag, or string]");
            System.exit(-1);
        }
    }

    public Map<String, Integer> getTopTenHashtags() {
        return tweetQueueManager.getTopNHashtags(10);
    }

    private void startStreaming() {
        Thread twitterStream = new Thread(streamProcessor);
        LOGGER.info("Starting Twitter Streaming: " + streamProcessor.toString() + ".");
        twitterStream.start();
    }

    private void startProcessingTweets() {
        TweetProcessor tweetProcessor = new TweetProcessor(tweetQueueManager);
        Thread processTweets = new Thread(tweetProcessor);
        LOGGER.info("Starting tweet processor.");
        processTweets.start();
    }

    private void outputTop10Every30Seconds() {
        while (true) {
            LOGGER.info("Top 10 Related Hashtags for the term: " +
            		streamProcessor.getTrackedTerm() + ", " +
                    getTopTenHashtags());
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }
    }
}