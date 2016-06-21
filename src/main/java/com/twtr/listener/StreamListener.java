package com.twtr.listener;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import com.twtr.processor.StreamProcessor;
/**
 * 
 * @author pchaturvedi
 *
 */
public class StreamListener implements StatusListener {
	
	private StreamProcessor processor;
	
	public StreamListener(StreamProcessor processor){
		this.processor = processor;
		
	}
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	public void onStatus(Status status) {
		// TODO Auto-generated method stub
		synchronized (this.processor.getTweetQueueManager()) {
            this.processor.getTweetQueueManager().enqueTweet(status.getText());
            this.processor.getTweetQueueManager().notifyAll();
        }
		
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// TODO Auto-generated method stub
		
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// TODO Auto-generated method stub
		
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		// TODO Auto-generated method stub
		
	}

	public void onStallWarning(StallWarning warning) {
		// TODO Auto-generated method stub
		
	}

}
