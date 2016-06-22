package com.twtr.processor;

import org.apache.log4j.Logger;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.twtr.filter.StreamFilter;
import com.twtr.listener.StreamListener;

/**
 * 
 * @author pchaturvedi
 * with references from https://dev.twitter.com/oauth/overview/authorizing-requests
 */
public class StreamProcessor implements Runnable {

	private static final String CONSUMER_KEY = "Yu7E11cRV4xUKD3q9GpJgxdRu";
	private static final String CONSUMER_SECRET = "pDD22SEBB6ExZctFzByj6fXr0Zz1JBnsZrW7xEQpUi4yEysexS";
	private static final String ACCESS_TOKEN = "3085783995-gBtZmpmxsw46NwoFdpsyhRJ7Hii30sUC3dQRuU6";
	private static final String ACCESS_TOKEN_SECRET = "GFtpLrsQ8XpHkhmQIbdqmli0MFkNuU3vwKBEoreKriMIy";
	
	private String trackedTerm;
    private TweetQueueManager tweetQueueManager;
    private final static Logger LOGGER = Logger.getLogger(StreamProcessor.class);
    
    private Configuration configuration;

    public StreamProcessor(final TweetQueueManager tweetQueueManager, final String trackedTerm){
    	this.tweetQueueManager = tweetQueueManager;
    	this.trackedTerm = trackedTerm;
    	ConfigurationBuilder config = new ConfigurationBuilder();
    	config.setOAuthConsumerKey(CONSUMER_KEY);
    	config.setOAuthConsumerSecret(CONSUMER_SECRET);
    	config.setOAuthAccessToken(ACCESS_TOKEN);
    	config.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
    	this.configuration = config.build();
    	LOGGER.debug(String.format("Initializing Twitter Streaming %s", configuration.toString()));
    }
    
    public TweetQueueManager getTweetQueueManager(){
    	return this.tweetQueueManager;
    }
    
    public String getTrackedTerm(){
    	return this.trackedTerm;
    }
    
    public void run() {
		// TODO Auto-generated method stub
		 TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();
	     twitterStream.addListener(new StreamListener(this));
	     String[] trackingKeyWords = new String[]{this.trackedTerm};
	     twitterStream.filter(new StreamFilter(trackingKeyWords).getFilterQuery());
		
	}

}
