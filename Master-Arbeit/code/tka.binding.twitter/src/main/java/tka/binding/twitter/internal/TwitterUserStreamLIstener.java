/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package tka.binding.twitter.internal;

import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventPublisher;

import tka.binding.twitter.TwitterBindingConstants;
import twitter4j.DirectMessage;
import twitter4j.ExtendedMediaEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

/**
 * Uses Twitter Streaming API to get notified of changes.
 *
 * @author Konstantin Tkachuk
 *
 *         27.02.2017
 */
public class TwitterUserStreamLIstener implements UserStreamListener {

    /**
     * The event publisher.
     */
    private EventPublisher eventPublisher;

    /**
     * The constructor.
     *
     * @param eventPublisher
     */
    public TwitterUserStreamLIstener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * @see twitter4j.StatusListener#onStatus(twitter4j.Status)
     */
    @Override
    public void onStatus(Status status) {
        publishEvent(TwitterBindingConstants.TOPIC_STATUS_CHANGED, status.getText());
        System.out.println("onStatus @" + status.getUser().getScreenName() + " - " + status.getText());
        ExtendedMediaEntity[] entities = status.getExtendedMediaEntities();
        System.out.println("Number of medias: " + entities.length);
        for (ExtendedMediaEntity entity : entities) {
            publishEvent(TwitterBindingConstants.TOPIC_MEDIA, entity.getMediaURL());
        }
    }

    /**
     * Publishes an event with the specified parameters.
     *
     * @param topic
     * @param payload
     */
    private void publishEvent(final String topic, final String payload) {
        Event event = new Event() {
            @Override
            public String getType() {
                return "FlashEvent";
            }

            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public String getSource() {
                return TwitterBindingConstants.SOURCE;
            }

            @Override
            public String getPayload() {
                return payload;
            }
        };
        eventPublisher.post(event);
    }

    /**
     * @see twitter4j.StatusListener#onDeletionNotice(twitter4j.StatusDeletionNotice)
     */
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    /**
     * @see twitter4j.UserStreamListener#onDeletionNotice(long, long)
     */
    @Override
    public void onDeletionNotice(long directMessageId, long userId) {
        System.out.println("Got a direct message deletion notice id:" + directMessageId);
    }

    /**
     * @see twitter4j.StatusListener#onTrackLimitationNotice(int)
     */
    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
    }

    /**
     * @see twitter4j.StatusListener#onScrubGeo(long, long)
     */
    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

    /**
     * @see twitter4j.StatusListener#onStallWarning(twitter4j.StallWarning)
     */
    @Override
    public void onStallWarning(StallWarning warning) {
        System.out.println("Got stall warning:" + warning);
    }

    /**
     * @see twitter4j.UserStreamListener#onFriendList(long[])
     */
    @Override
    public void onFriendList(long[] friendIds) {
        System.out.print("onFriendList");
        for (long friendId : friendIds) {
            System.out.print(" " + friendId);
        }
        System.out.println();
    }

    /**
     * @see twitter4j.UserStreamListener#onFavorite(twitter4j.User, twitter4j.User, twitter4j.Status)
     */
    @Override
    public void onFavorite(User source, User target, Status favoritedStatus) {
        System.out.println("onFavorite source:@" + source.getScreenName() + " target:@" + target.getScreenName() + " @"
                + favoritedStatus.getUser().getScreenName() + " - " + favoritedStatus.getText());
    }

    /**
     * @see twitter4j.UserStreamListener#onUnfavorite(twitter4j.User, twitter4j.User, twitter4j.Status)
     */
    @Override
    public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
        System.out.println("onUnFavorite source:@" + source.getScreenName() + " target:@" + target.getScreenName()
                + " @" + unfavoritedStatus.getUser().getScreenName() + " - " + unfavoritedStatus.getText());
    }

    /**
     * @see twitter4j.UserStreamListener#onFollow(twitter4j.User, twitter4j.User)
     */
    @Override
    public void onFollow(User source, User followedUser) {
        System.out.println("onFollow source:@" + source.getScreenName() + " target:@" + followedUser.getScreenName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUnfollow(twitter4j.User, twitter4j.User)
     */
    @Override
    public void onUnfollow(User source, User followedUser) {
        System.out.println("onFollow source:@" + source.getScreenName() + " target:@" + followedUser.getScreenName());
    }

    /**
     * @see twitter4j.UserStreamListener#onDirectMessage(twitter4j.DirectMessage)
     */
    @Override
    public void onDirectMessage(DirectMessage directMessage) {
        publishEvent(TwitterBindingConstants.TOPIC_MESSAGE, directMessage.getText());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListMemberAddition(twitter4j.User, twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberAddition added member:@" + addedMember.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListMemberDeletion(twitter4j.User, twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
        System.out.println("onUserListMemberDeleted deleted member:@" + deletedMember.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListSubscription(twitter4j.User, twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListSubscribed subscriber:@" + subscriber.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListUnsubscription(twitter4j.User, twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
        System.out.println("onUserListUnsubscribed subscriber:@" + subscriber.getScreenName() + " listOwner:@"
                + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListCreation(twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListCreation(User listOwner, UserList list) {
        System.out.println("onUserListCreated  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListUpdate(twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListUpdate(User listOwner, UserList list) {
        System.out.println("onUserListUpdated  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserListDeletion(twitter4j.User, twitter4j.UserList)
     */
    @Override
    public void onUserListDeletion(User listOwner, UserList list) {
        System.out.println("onUserListDestroyed  listOwner:@" + listOwner.getScreenName() + " list:" + list.getName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserProfileUpdate(twitter4j.User)
     */
    @Override
    public void onUserProfileUpdate(User updatedUser) {
        System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUserDeletion(long)
     */
    @Override
    public void onUserDeletion(long deletedUser) {
        System.out.println("onUserDeletion user:@" + deletedUser);
    }

    /**
     * @see twitter4j.UserStreamListener#onUserSuspension(long)
     */
    @Override
    public void onUserSuspension(long suspendedUser) {
        System.out.println("onUserSuspension user:@" + suspendedUser);
    }

    /**
     * @see twitter4j.UserStreamListener#onBlock(twitter4j.User, twitter4j.User)
     */
    @Override
    public void onBlock(User source, User blockedUser) {
        System.out.println("onBlock source:@" + source.getScreenName() + " target:@" + blockedUser.getScreenName());
    }

    /**
     * @see twitter4j.UserStreamListener#onUnblock(twitter4j.User, twitter4j.User)
     */
    @Override
    public void onUnblock(User source, User unblockedUser) {
        System.out.println("onUnblock source:@" + source.getScreenName() + " target:@" + unblockedUser.getScreenName());
    }

    /**
     * @see twitter4j.UserStreamListener#onRetweetedRetweet(twitter4j.User, twitter4j.User, twitter4j.Status)
     */
    @Override
    public void onRetweetedRetweet(User source, User target, Status retweetedStatus) {
        System.out.println("onRetweetedRetweet source:@" + source.getScreenName() + " target:@" + target.getScreenName()
                + retweetedStatus.getUser().getScreenName() + " - " + retweetedStatus.getText());
    }

    /**
     * @see twitter4j.UserStreamListener#onFavoritedRetweet(twitter4j.User, twitter4j.User, twitter4j.Status)
     */
    @Override
    public void onFavoritedRetweet(User source, User target, Status favoritedRetweet) {
        System.out.println("onFavroitedRetweet source:@" + source.getScreenName() + " target:@" + target.getScreenName()
                + favoritedRetweet.getUser().getScreenName() + " - " + favoritedRetweet.getText());
    }

    /**
     * @see twitter4j.UserStreamListener#onQuotedTweet(twitter4j.User, twitter4j.User, twitter4j.Status)
     */
    @Override
    public void onQuotedTweet(User source, User target, Status quotingTweet) {
        System.out.println("onQuotedTweet" + source.getScreenName() + " target:@" + target.getScreenName()
                + quotingTweet.getUser().getScreenName() + " - " + quotingTweet.getText());
    }

    /**
     * @see twitter4j.StreamListener#onException(java.lang.Exception)
     */
    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
        System.out.println("onException:" + ex.getMessage());
    }
}
