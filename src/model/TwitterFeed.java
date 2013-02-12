package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFeed implements StatusListener {

	Vector<String> feedBuffer = new Vector<String>();
	int bufferSize = 50;

	Vector<String> usedCache = new Vector<String>();
	int cachedSize = 50;

	TwitterStream twitterStream;

	String[] tags = new String[] { "#love", "#heart", "#amor", "#teamo", "#jet'aime", "#jetaime", "#valentine", "#girlfriend", "#boyfriend", "#wife",
			"#husband", "#propose", "#married" };

	public TwitterFeed() {

		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true).setOAuthConsumerKey("8NGsgoyJZtP2iwm6sE2Dxg").setOAuthConsumerSecret("hvfM705RQFxYSUfz8oDXwbMnEpAa4Zm6ZKT80ezY")
				.setOAuthAccessToken("53223146-NOVMbadRu7kVdFj5UcoyYijfSZNldKiDhglICxxKs")
				.setOAuthAccessTokenSecret("7vQtsegtERMWU01SLRn9JKPrPZaqo1dGBPwLVEys");

		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		// twitterStream.setOAuthConsumer("8NGsgoyJZtP2iwm6sE2Dxg",
		// "hvfM705RQFxYSUfz8oDXwbMnEpAa4Zm6ZKT80ezY");

		// try {
		// RequestToken requestToken = twitterStream.getOAuthRequestToken();
		//
		// AccessToken accessToken = null;
		//
		// System.out.println(requestToken.getAuthorizationURL());
		//
		//
		//
		// } catch (TwitterException e) {
		// e.printStackTrace();
		// }

		twitterStream.addListener(this);

		FilterQuery query = new FilterQuery();

		query.track(tags);

		twitterStream.filter(query);
		// twitterStream.sample();

		// String file = readFile("input.txt");
		//
		// String[] tokens = file.split("\\.");
		//
		// for (String token : tokens) {
		// feedsim.add(token.trim() + "|||");
		// }
		//

	}

	public static void main(String[] args) {
		new TwitterFeed();
	}

	public synchronized String getNewTweet() {

		String newTweet;

		if (!feedBuffer.isEmpty()) {

			newTweet = feedBuffer.elementAt(0);
			feedBuffer.removeElementAt(0);

			if (usedCache.isEmpty()) {
				usedCache.add(newTweet);
			} else {
				usedCache.add(newTweet);

				if (usedCache.size() >= cachedSize) {
					usedCache.remove(0);
				}
			}

			// System.out.println("Poping tweet :" + newTweet);
			return newTweet;
		} else {
			if (!usedCache.isEmpty()) {
				newTweet = usedCache.elementAt(0);

				if (usedCache.size() > 1) {
					usedCache.remove(0);
					usedCache.add(newTweet);
				}

				return newTweet;
			} else {

				return null;
			}
		}

	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * @param aFile
	 *            is a file which already exists and can be read.
	 */
	public static String readFile(String fileName) {

		URL fileURL = TwitterFeed.class.getResource(fileName);

		if (fileURL == null) {
			fileURL = TwitterFeed.class.getResource(fileName);
		}

		File aFile = null;

		if (fileURL == null) {
			try {
				aFile = new File(fileName);
				fileURL = aFile.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

		if (fileURL == null) {
			return null;
		} else {
			if (aFile != null) {
				if (!aFile.canRead()) {
					return null;
				}
			}
		}

		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new InputStreamReader(fileURL.openStream()));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString();
	}

	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();

	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());

	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);

	}

	@Override
	public void onStallWarning(StallWarning warning) {
		System.out.println("Got stall warning:" + warning);

	}

	@Override
	public void onStatus(Status status) {

		// System.out.println(status.getText());
		// System.out.println("@" + status.getUser().getScreenName() + " - " +
		// status.getText());

		if (feedBuffer.size() < bufferSize) {
			synchronized (this) {
				String cleanedTweet = cleanTweet(status.getText());

				if (!cleanedTweet.isEmpty()) {
					feedBuffer.addElement(cleanedTweet + " | ");
					// System.out.println("Buffered " + feedBuffer.size() +
					// " tweets");
				}
				 //System.out.println("Buffering: " + status.getText());
			}
		}

	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);

	}

	public boolean validTag(String tag) {
		for (String t : tags) {
			if (t.equalsIgnoreCase(tag)) {
				return true;
			}
		}

		return false;
	}

	public String cleanTweet(String dirtyTweet) {

		// eliminate retweets
		if (dirtyTweet.trim().toLowerCase().startsWith("rt") || dirtyTweet.toLowerCase().contains("follow")) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		String[] tokens = dirtyTweet.split(" ");

		boolean invalid = false;

		for (String token : tokens) {

			invalid |= token.startsWith("#") && !validTag(token);
			invalid |= token.startsWith("http");
			invalid |= token.startsWith("@");

			if (!invalid) {
				sb.append(token.trim() + " ");
			}
		}

		return sb.toString();
	}
}
