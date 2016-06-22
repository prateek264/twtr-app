package com.twtr.app;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.twtr.processor.StreamProcessor;
import com.twtr.processor.TweetProcessor;
import com.twtr.processor.TweetQueueManager;
/**
 * 
 * @author pchaturvedi
 * With references from following pages
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
	private static final String OUTPUT_FORMAT = "Top 10 Hastags associated with string [%s] at %s are %s";
    private final TweetQueueManager tweetQueueManager;
    private StreamProcessor streamProcessor;

    public TwitterApp(final String trackedTerm) {
    	tweetQueueManager = new TweetQueueManager();
        streamProcessor = new StreamProcessor(tweetQueueManager, trackedTerm);
    }

    public static void main(String[] args) {
        if (args.length == 1) {        	
        	LOGGER.info("Tracking twitter stream:");
        	String stringToTrack = args[0];
            TwitterApp application = new TwitterApp(stringToTrack);
            application.startStreaming();
            application.startProcessingTweets();
            application.outputTop10HashesEvery30Seconds();
            
        } else {
        	System.out.println("*******************************************************************");
            System.out.println("No String specified to track, exiting!" );
            System.out.println("*******************************************************************");
        }
    }

    /**
     * reads top 10 hashtags from the map
     * @return
     */
    public Map<String, Integer> getTopTenHashtags() {
        return tweetQueueManager.getTopNHashtags(10);
    }
    
    /**
     * starts twitter stream
     */
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

    private void outputTop10HashesEvery30Seconds() {
        while (true) {
            LOGGER.debug(String.format(OUTPUT_FORMAT, streamProcessor.getTrackedTerm(),new Date() ,getTopTenHashtags()));
            System.out.println("*******************************************************************");
            System.out.println(String.format(OUTPUT_FORMAT, streamProcessor.getTrackedTerm(),new Date() ,getTopTenHashtags()));
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }
    }
}