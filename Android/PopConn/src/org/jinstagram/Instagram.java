package org.jinstagram;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jinstagram.auth.model.OAuthConstants;
import org.jinstagram.auth.model.OAuthRequest;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.comments.MediaCommentResponse;
import org.jinstagram.entity.comments.MediaCommentsFeed;
import org.jinstagram.entity.common.InstagramErrorResponse;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.likes.LikesFeed;
import org.jinstagram.entity.locations.LocationInfo;
import org.jinstagram.entity.locations.LocationSearchFeed;
import org.jinstagram.entity.media.MediaInfoFeed;
import org.jinstagram.entity.relationships.RelationshipFeed;
import org.jinstagram.entity.tags.TagInfoFeed;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.tags.TagSearchFeed;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.UserFeed;
import org.jinstagram.exceptions.InstagramException;
import org.jinstagram.http.Response;
import org.jinstagram.http.Verbs;
import org.jinstagram.model.Constants;
import org.jinstagram.model.Methods;
import org.jinstagram.model.QueryParam;
import org.jinstagram.model.Relationship;
import org.jinstagram.utils.Preconditions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Instagram
 *
 * @author Sachin Handiekar
 * @version 1.0
 */
public class Instagram {
	private Token accessToken;
    private final String clientId;

	public Instagram(Token accessToken) {
		this.accessToken = accessToken;
		clientId = null;
	}

	/**
	 * Create a new Instagram instance only appropriate for unauthenticated requests (i.e. on behalf of the
	 * application but not any particular user)
	 */
	public Instagram(String clientId) {
	    this.accessToken = null;
	    this.clientId = clientId;
	}


	/**
	 * @return the accessToken
	 */
	public Token getAccessToken() {
		return accessToken;
	}


	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}


	/**
	 * Get basic information about a user.
	 *
	 * @param userId user-id
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserInfo getUserInfo(long userId) throws InstagramException {
		Preconditions.checkNotNull(userId, "UserId cannot be null.");

		String apiMethod = String.format(Methods.USERS_WITH_ID, userId);
		UserInfo userInfo = createInstagramObject(Verbs.GET, UserInfo.class, apiMethod, null);

		return userInfo;
	}


	/**
	 * Get basic information about a user.
	 *
	 * @return a UserInfo object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserInfo getCurrentUserInfo() throws InstagramException {
 		UserInfo userInfo = createInstagramObject(Verbs.GET, UserInfo.class, Methods.USERS_SELF, null);
 		return userInfo;
	}

	/**
	 * See the authenticated user's feed.
	 *
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaFeed getUserFeeds() throws InstagramException {
		MediaFeed userFeed = createInstagramObject(Verbs.GET, MediaFeed.class, Methods.USERS_SELF_FEED, null);
        return userFeed;
	}

     /**
     * See the authenticated user's feed
     *
     * @param maxId
     * @param minId
     * @param count Count of media to return.
     * @return a MediaFeed object.
     * @throws InstagramException if any error occurs.
     */
    public MediaFeed getUserFeeds(String maxId, String minId, long count) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        if(maxId != null) {
            params.put(QueryParam.MAX_ID,String.valueOf(maxId));
        }

        if(minId != null) {
            params.put(QueryParam.MIN_ID,String.valueOf(minId));
        }

        if(count != 0) {
            params.put(QueryParam.COUNT,String.valueOf(count));
        }

        MediaFeed userFeed = createInstagramObject(Verbs.GET, MediaFeed.class, Methods.USERS_SELF_FEED, params);
        return userFeed;
    }

	/**
	 * Get the most recent media published by a user.
	 *
	 * @param userId userId of the User.
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs
	 */
	public MediaFeed getRecentMediaFeed(long userId) throws InstagramException {
		Preconditions.checkNotNull(userId, "UserId cannot be null.");

		String methodName = String.format(Methods.USERS_RECENT_MEDIA, userId);
		MediaFeed recentMediaFeed = createInstagramObject(Verbs.GET, MediaFeed.class, methodName, null);

		return recentMediaFeed;
	}

    /**
     * Get the most recent media published by a user.
     *
     * @param userId userId of the User.
     * @return a MediaFeed object.
     * @throws InstagramException if any error occurs
     */
    public MediaFeed getRecentMediaFeed(long userId, int count, String minId, String maxId, Date maxTimeStamp, Date minTimeStamp) throws InstagramException {
        Preconditions.checkNotNull(userId, "UserId cannot be null.");
        Map<String, String> params = new HashMap<String, String>();

        if(maxId != null) {
            params.put(QueryParam.MAX_ID,String.valueOf(maxId));
        }

        if(minId != null) {
            params.put(QueryParam.MIN_ID,String.valueOf(minId));
        }

        if(count != 0) {
            params.put(QueryParam.COUNT,String.valueOf(count));
        }

        if(maxTimeStamp != null) {
            params.put(QueryParam.MAX_TIMESTAMP, String.valueOf(maxTimeStamp.getTime()));
        }

        if(minTimeStamp != null) {
            params.put(QueryParam.MIN_TIMESTAMP, String.valueOf(minTimeStamp.getTime()));
        }

        String methodName = String.format(Methods.USERS_RECENT_MEDIA, userId);
        MediaFeed recentMediaFeed = createInstagramObject(Verbs.GET, MediaFeed.class, methodName, params);

        return recentMediaFeed;
    }

    /**
     * Get the next page of recent media objects from a previously executed request
     * @param pagination
     * @throws InstagramException
     */
    public MediaFeed getRecentMediaNextPage(Pagination pagination) throws InstagramException {
        return createInstagramObject(Verbs.GET, MediaFeed.class, StringUtils.removeStart(pagination.getNextUrl(), Constants.API_URL), null);
    }

	/**
	 * Get the authenticated user's list of media they've liked.
	 *
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaFeed getUserLikedMediaFeed() throws InstagramException {
		MediaFeed userLikedMedia = createInstagramObject(Verbs.GET, MediaFeed.class,
				Methods.USERS_SELF_LIKED_MEDIA, null);

		return userLikedMedia;
	}

    /**
     * Get the authenticated user's list of media they've liked.
     *
     * @return a MediaFeed object.
     * @throws InstagramException if any error occurs.
     */
    public MediaFeed getUserLikedMediaFeed(long maxLikeId, int count) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        if(maxLikeId > 0) {
            params.put(QueryParam.MAX_LIKE_ID, String.valueOf(maxLikeId));
        }

        if(count > 0) {
            params.put(QueryParam.COUNT, String.valueOf(count));
        }

        MediaFeed userLikedMedia = createInstagramObject(Verbs.GET, MediaFeed.class,
                Methods.USERS_SELF_LIKED_MEDIA, params);

        return userLikedMedia;
    }

	/**
	 * Search for a user by name.
	 *
	 * @param query A query string.
	 * @return a UserFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserFeed searchUser(String query) throws InstagramException {
		Preconditions.checkNotNull(query, "search query cannot be null.");

		Map<String, String> params = new HashMap<String, String>();
		params.put(QueryParam.SEARCH_QUERY, query);

		UserFeed userFeed = createInstagramObject(Verbs.GET, UserFeed.class, Methods.USERS_SEARCH, params);

		return userFeed;
	}

    /**
     * Search for a user by name.
     *
     * @param query A query string.
     * @return a UserFeed object.
     * @throws InstagramException if any error occurs.
     */
    public UserFeed searchUser(String query, int count) throws InstagramException {
        Preconditions.checkNotNull(query, "search query cannot be null.");

        Map<String, String> params = new HashMap<String, String>();
        params.put(QueryParam.SEARCH_QUERY, query);

        if(count > 0) {
            params.put(QueryParam.COUNT, String.valueOf(count));
        }

        UserFeed userFeed = createInstagramObject(Verbs.GET, UserFeed.class, Methods.USERS_SEARCH, params);

        return userFeed;
    }


    /**
	 * Get the list of 'users' the authenticated user follows.
	 *
	 * @param userId userId of the User.
	 * @return a UserFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserFeed getUserFollowList(long userId) throws InstagramException {
		Preconditions.checkNotNull(userId, "userId cannot be null.");

		String apiMethod = String.format(Methods.USERS_ID_FOLLOWS, userId);
		UserFeed userFeed = createInstagramObject(Verbs.GET, UserFeed.class, apiMethod, null);

		return userFeed;
	}

	/**
	 * Get the list of 'users' the current authenticated user is followed by.
	 *
	 * @param userId userId of the User.
	 * @return a UserFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserFeed getUserFollowedByList(long userId) throws InstagramException {
		Preconditions.checkNotNull(userId, "userId cannot be null.");

		String apiMethod = String.format(Methods.USERS_ID_FOLLOWED_BY, userId);
		UserFeed userFeed = createInstagramObject(Verbs.GET, UserFeed.class, apiMethod, null);

		return userFeed;
	}

	/**
	 * Get a list of users who have requested this user's permission to follow
	 *
	 * @return a UserFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public UserFeed getUserRequestedBy() throws InstagramException {
		UserFeed userFeed = createInstagramObject(Verbs.GET, UserFeed.class, Methods.USERS_SELF_REQUESTED_BY, null);

		return userFeed;
	}

	/**
	 * Get information about the current user's relationship
	 * (follow/following/etc) to another user.
	 *
	 * @param userId userId of the User.
	 * @return a Relationship feed object.
	 * @throws InstagramException if any error occurs.
	 */
	public RelationshipFeed getUserRelationship(long userId) throws InstagramException {
		Preconditions.checkNotNull(userId, "userId cannot be null.");

		String apiMethod = String.format(Methods.USERS_ID_RELATIONSHIP, userId);
		RelationshipFeed feed = createInstagramObject(Verbs.GET, RelationshipFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Set the relationship between the current user and the target user
	 *
	 * @param userId userId of the user.
	 * @param relationship Relationship status
	 * @return a Relationship feed object
	 * @throws InstagramException if any error occurs.
	 */
	public RelationshipFeed setUserRelationship(long userId, Relationship relationship) throws InstagramException {
		Preconditions.checkNotNull(userId, "userId cannot be null.");
		Preconditions.checkNotNull(relationship, "relationship cannot be null.");

		String apiMethod = String.format(Methods.USERS_ID_RELATIONSHIP, userId);
		Map<String, String> params = new HashMap<String, String>();

		params.put(QueryParam.ACTION, relationship.toString());

		RelationshipFeed feed = createInstagramObject(Verbs.POST, RelationshipFeed.class, apiMethod, params);

		return feed;
	}

	/**
	 * Get information about a media object.
	 *
	 * @param mediaId mediaId of the Media object.
	 * @return a mediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaInfoFeed getMediaInfo(String mediaId) throws InstagramException {
		Preconditions.checkNotNull(mediaId, "mediaId cannot be null.");

		String apiMethod = String.format(Methods.MEDIA_BY_ID, mediaId);
		MediaInfoFeed feed = createInstagramObject(Verbs.GET, MediaInfoFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Search for media in a given area.
	 *
	 * @param latitude Latitude of the center search coordinate.
	 * @param longitude Longitude of the center search coordinate.
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs
	 */
	public MediaFeed searchMedia(double latitude, double longitude) throws InstagramException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(QueryParam.LATITUDE, Double.toString(latitude));
		params.put(QueryParam.LONGITUDE, Double.toString(longitude));

		MediaFeed mediaFeed = createInstagramObject(Verbs.GET, MediaFeed.class, Methods.MEDIA_SEARCH, params);

		return mediaFeed;
	}

    /**
     * Search for media in a given area.
     *
     * @param latitude Latitude of the center search coordinate.
     * @param longitude Longitude of the center search coordinate.
     * @return a MediaFeed object.
     * @throws InstagramException if any error occurs
     */
    public MediaFeed searchMedia(double latitude, double longitude, Date maxTimeStamp, Date minTimeStamp, int distance) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        params.put(QueryParam.LATITUDE, Double.toString(latitude));
        params.put(QueryParam.LONGITUDE, Double.toString(longitude));

        if(maxTimeStamp != null) {
            params.put(QueryParam.MAX_TIMESTAMP, String.valueOf(maxTimeStamp.getTime()));
        }

        if(minTimeStamp != null) {
            params.put(QueryParam.MIN_TIMESTAMP, String.valueOf(minTimeStamp.getTime()));
        }

        params.put(QueryParam.DISTANCE, String.valueOf(distance));


        MediaFeed mediaFeed = createInstagramObject(Verbs.GET, MediaFeed.class, Methods.MEDIA_SEARCH, params);

        return mediaFeed;
    }

	/**
	 * Get a list of what media is most popular at the moment.
	 *
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaFeed getPopularMedia() throws InstagramException {
		MediaFeed mediaFeed = createInstagramObject(Verbs.GET, MediaFeed.class, Methods.MEDIA_POPULAR, null);

		return mediaFeed;
	}

	/**
	 * Get a full list of comments on a media.
	 *
	 * @param mediaId a mediaId
	 * @return a MediaCommentsFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaCommentsFeed getMediaComments(String mediaId) throws InstagramException {
		String apiMethod = String.format(Methods.MEDIA_COMMENTS, mediaId);
		MediaCommentsFeed feed = createInstagramObject(Verbs.GET, MediaCommentsFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Create a comment on a media.
	 *
	 * @param mediaId a mediaId
	 * @param text Text to post as a comment on the media as specified in
	 * media-id.
	 * @return a MediaCommentResponse feed.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaCommentResponse setMediaComments(String mediaId, String text) throws InstagramException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(QueryParam.TEXT, text);

		String apiMethod = String.format(Methods.MEDIA_COMMENTS, mediaId);
		MediaCommentResponse feed = createInstagramObject(Verbs.POST, MediaCommentResponse.class, apiMethod, params);

		return feed;
	}

	/**
	 * Remove a comment either on the authenticated user's media or authored by
	 * the authenticated user.
	 *
	 * @param mediaId a mediaId of the Media
	 * @param commentId a commentId of the Comment
	 * @return a MediaCommentResponse feed.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaCommentResponse deleteMediaCommentById(String mediaId, long commentId) throws InstagramException {
		String apiMethod = String.format(Methods.DELETE_MEDIA_COMMENTS, mediaId, commentId);
		MediaCommentResponse feed = createInstagramObject(Verbs.DELETE, MediaCommentResponse.class, apiMethod, null);

		return feed;
	}

	/**
	 * Get a list of users who have liked this media.
	 *
	 * @param mediaId a mediaId of the Media
	 * @return a LikesFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public LikesFeed getUserLikes(String mediaId) throws InstagramException {
		String apiMethod = String.format(Methods.LIKES_BY_MEDIA_ID, mediaId);
		LikesFeed feed = createInstagramObject(Verbs.GET, LikesFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Set a like on this media by the currently authenticated user.
	 *
	 * @param mediaId a mediaId of the Media
	 * @return a LikesFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public LikesFeed setUserLike(String mediaId) throws InstagramException {
		String apiMethod = String.format(Methods.LIKES_BY_MEDIA_ID, mediaId);
		LikesFeed feed = createInstagramObject(Verbs.POST, LikesFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Remove a like on this media by the currently authenticated user.
	 *
	 * @param mediaId a mediaId of the Media
	 * @return a LikesFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public LikesFeed deleteUserLike(String mediaId) throws InstagramException {
		String apiMethod = String.format(Methods.LIKES_BY_MEDIA_ID, mediaId);
		LikesFeed feed = createInstagramObject(Verbs.DELETE, LikesFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Get information about a tag object.
	 *
	 * @param tagName name of the tag.
	 * @return a TagInfoFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public TagInfoFeed getTagInfo(String tagName) throws InstagramException {
		String apiMethod = String.format(Methods.TAGS_BY_NAME, tagName);
		TagInfoFeed feed = createInstagramObject(Verbs.GET, TagInfoFeed.class, apiMethod, null);

		return feed;
	}

	/**
	 * Get a list of recently tagged media.
	 *
	 * @param tagName name of the tag.
	 * @return a TagMediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public TagMediaFeed getRecentMediaTags(String tagName) throws InstagramException {
		String apiMethod = String.format(Methods.TAGS_RECENT_MEDIA, tagName);
		TagMediaFeed feed = createInstagramObject(Verbs.GET, TagMediaFeed.class, apiMethod, null);

		return feed;
	}

    /**
     * Get a list of recently tagged media.
     *
     * @param tagName name of the tag.
     * @return a TagMediaFeed object.
     * @throws InstagramException if any error occurs.
     */
    public TagMediaFeed getRecentMediaTags(String tagName, int minId, int maxId) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        if(minId > 0)
        params.put(QueryParam.MIN_ID, String.valueOf(minId));

        if(maxId > 0)
        params.put(QueryParam.MAX_ID, String.valueOf(maxId));

        String apiMethod = String.format(Methods.TAGS_RECENT_MEDIA, tagName);
        TagMediaFeed feed = createInstagramObject(Verbs.GET, TagMediaFeed.class, apiMethod, null);

        return feed;
    }

	/**
	 * Search for tags by name - results are ordered first as an exact match,
	 * then by popularity.
	 *
	 * @param tagName name of the tag
	 * @return a TagSearchFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public TagSearchFeed searchTags(String tagName) throws InstagramException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(QueryParam.SEARCH_QUERY, tagName);

		TagSearchFeed feed = createInstagramObject(Verbs.GET, TagSearchFeed.class, Methods.TAGS_SEARCH, params);

		return feed;
	}

	/**
	 * Get information about a location.
	 *
	 * @param locationId an id of the Location
	 * @return a LocationInfo object.
	 * @throws InstagramException if any error occurs.
	 */
	public LocationInfo getLocationInfo(long locationId) throws InstagramException {
		String apiMethod = String.format(Methods.LOCATIONS_BY_ID, locationId);
		LocationInfo feed = createInstagramObject(Verbs.GET, LocationInfo.class, apiMethod, null);

		return feed;
	}

	/**
	 * Get a list of recent media objects from a given location.
	 *
	 * @param locationId a id of the Media.
	 * @return a MediaFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public MediaFeed getRecentMediaByLocation(long locationId) throws InstagramException {
		String apiMethod = String.format(Methods.LOCATIONS_RECENT_MEDIA_BY_ID, locationId);
		MediaFeed feed = createInstagramObject(Verbs.GET, MediaFeed.class, apiMethod, null);

		return feed;
	}

    /**
     * Get a list of recent media objects from a given location.
     *
     * @param locationId a id of the Media.
     * @param minId Return media before this min_id.
     * @param maxId Return media before this max_id.
     * @param maxTimeStamp Return media before this max date.
     * @param minTimeStamp Return media after this min date.
     * @return a MediaFeed object.
     * @throws InstagramException if any error occurs.
     */
    public MediaFeed getRecentMediaByLocation(long locationId, int minId, int maxId, Date maxTimeStamp, Date minTimeStamp) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        if(maxTimeStamp != null) {
            params.put(QueryParam.MAX_TIMESTAMP, String.valueOf(maxTimeStamp.getTime()));
        }

        if(minTimeStamp != null) {
            params.put(QueryParam.MIN_TIMESTAMP, String.valueOf(minTimeStamp.getTime()));
        }

        if(minId > 0)
            params.put(QueryParam.MIN_ID, String.valueOf(minId));

        if(maxId > 0)
            params.put(QueryParam.MAX_ID, String.valueOf(maxId));


        String apiMethod = String.format(Methods.LOCATIONS_RECENT_MEDIA_BY_ID, locationId);
        MediaFeed feed = createInstagramObject(Verbs.GET, MediaFeed.class, apiMethod, params);

        return feed;
    }

	/**
	 * Search for a location by geographic coordinate.
	 *
	 * @param latitude Latitude of the center search coordinate.
	 * @param longitude Longitude of the center search coordinate.
	 * @return a LocationSearchFeed object.
	 * @throws InstagramException if any error occurs.
	 */
	public LocationSearchFeed searchLocation(double latitude, double longitude) throws InstagramException {
		Map<String, String> params = new HashMap<String, String>();

		params.put(QueryParam.LATITUDE, Double.toString(latitude));
		params.put(QueryParam.LONGITUDE, Double.toString(longitude));

		LocationSearchFeed feed = createInstagramObject(Verbs.GET, LocationSearchFeed.class, Methods.LOCATIONS_SEARCH,
				params);

		return feed;
	}


    /**
     * Search for a location by geographic coordinate.
     *
     * @param latitude Latitude of the center search coordinate.
     * @param longitude Longitude of the center search coordinate.
     * @param distance Default is 1000m (distance=1000), max distance is 5000.
     * @return a LocationSearchFeed object.
     * @throws InstagramException if any error occurs.
     */
    public LocationSearchFeed searchLocation(double latitude, double longitude, int distance) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        params.put(QueryParam.LATITUDE, Double.toString(latitude));
        params.put(QueryParam.LONGITUDE, Double.toString(longitude));
        params.put(QueryParam.DISTANCE, Integer.toString(distance));

        LocationSearchFeed feed = createInstagramObject(Verbs.GET, LocationSearchFeed.class, Methods.LOCATIONS_SEARCH,
                params);

        return feed;
    }

    /**
     * Search for a location by v2 Foursquare id.
     *
     * @param foursquareId Foursquare Venue ID of the location
     * @return a LocationSearchFeed object.
     * @throws InstagramException if any error occurs.
     */
    public LocationSearchFeed searchFoursquareVenue(String foursquareId) throws InstagramException {
        Map<String, String> params = new HashMap<String, String>();

        params.put(QueryParam.FOURSQUARE_V2_ID, foursquareId);

        LocationSearchFeed feed = createInstagramObject(Verbs.GET, LocationSearchFeed.class, Methods.LOCATIONS_SEARCH,
                params);

        return feed;
    }

	/**
	 * Create a instagram object based on class-name and response.
	 *
	 * @param verbs HTTP State
	 * @param clazz
	 * @param methodName
	 * @param params
	 * @return
	 * @throws InstagramException
	 */
	private <T> T createInstagramObject(Verbs verbs, Class<T> clazz, String methodName, Map<String, String> params)
        throws InstagramException {
            Response response;
            try {
                response = getApiResponse(verbs, methodName, params);
            } catch (IOException e) {
                throw new InstagramException("IOException while retrieving data", e);
            }

            if (response.getCode() >= 200 && response.getCode() < 300) {
                T object = createObjectFromResponse(clazz, response.getBody());

                return object;
            }

            throw handleInstagramError(response);
        }

    private InstagramException handleInstagramError(Response response) throws InstagramException {
        if (response.getCode() == 400) {
            Gson gson = new Gson();
            final InstagramErrorResponse error;
            try {
                System.out.println(response.getBody());
                error = gson.fromJson(response.getBody(), InstagramErrorResponse.class);
            } catch (JsonSyntaxException e) {
                throw new InstagramException("Failed to decode error response " + response.getBody(), e);
            }
            error.throwException();
        }
        throw new InstagramException("Unknown error response code: " + response.getCode() + " " + response.getBody());
    }

	/**
	 * Get response from Instagram.
	 *
	 * @param verb HTTP Verb
	 * @param methodName Instagram API Method
	 * @param params parameters which would be sent with the request.
	 * @return Response object.
	 */
	private Response getApiResponse(Verbs verb, String methodName, Map<String, String> params) throws IOException {
		Response response = null;
		String apiResourceUrl = Constants.API_URL + methodName;
		OAuthRequest request = new OAuthRequest(verb, apiResourceUrl);

		// Additional parameters in url
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (verb == Verbs.GET) {
					request.addQuerystringParameter(entry.getKey(), entry.getValue());
				}
				else {
					request.addBodyParameter(entry.getKey(), entry.getValue());
				}
			}
		}

		// Add the AccessToken to the Request Url
		if ((verb == Verbs.GET) || (verb == Verbs.DELETE)) {
			if (accessToken == null) {
			    request.addQuerystringParameter(OAuthConstants.CLIENT_ID, clientId);
			} else {
			    request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
			}
		}
		else {
		    if (accessToken == null) {
		        request.addBodyParameter(OAuthConstants.CLIENT_ID, clientId);
		    } else {
		        request.addBodyParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
		    }
		}

		response = request.send();

		return response;
	}

	/**
	 * Creates an object from the JSON response and the class which the object would be mapped to.
	 *
	 * @param clazz a class instance
	 * @param response a JSON feed
	 * @return a object of type <T>
	 * @throws InstagramException if any error occurs.
	 */
        private <T> T createObjectFromResponse(Class<T> clazz, final String response) throws InstagramException {
            Gson gson = new Gson();
            T object = null;

            try {
                object = clazz.newInstance();
			object = gson.fromJson(response, clazz);
		}
		catch (InstantiationException e) {
			throw new InstagramException("Problem in Instantiation of type " + clazz.getName(), e);
		}
		catch (IllegalAccessException e) {
			throw new InstagramException("Couldn't create object of type " + clazz.getName(), e);
		}
		catch (Exception e) {
			throw new InstagramException("Error parsing json to object type " + clazz.getName(), e);
		}

		return object;
	}
}
