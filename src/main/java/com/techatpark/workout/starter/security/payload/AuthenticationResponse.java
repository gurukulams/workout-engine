package com.techatpark.workout.starter.security.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The type Authentication response.
 */
public final class AuthenticationResponse {
    /**
     * declares variable userName.
     */
    private final String userName;

    /**
     * declares variable displayName.
     */
    private final String displayName;

    /**
     * declares variable authToken.
     */
    private final String authToken;

    /**
     * declares variable expiresIn.
     */
    private final Long expiresIn;

    /**
     * declares variable refreshToken.
     */
    private final String refreshToken;

    /**
     * declares variable registrationToken.
     */
    private final String registrationToken;

    /**
     * declares variable profilePicture.
     */
    private final String profilePicture;

    /**
     * declares variable profilePicture.
     */
    private final List<String> features;

    /**
     * initializes the value for registered user.
     *
     * @param anUserName      the an user name
     * @param theDisplayName the Display Name
     * @param anAuthToken     the an auth token
     * @param anExpiresIn       the anExpiresIn
     * @param aRefreshToken   the a refresh token
     * @param aProfilePicture the a profile picture
     * @param theFeatures the features of the user
     */
    @JsonCreator
    public AuthenticationResponse(
            @JsonProperty("userName") final String anUserName,
            @JsonProperty("displayName") final String theDisplayName,
            @JsonProperty("authToken") final String anAuthToken,
            @JsonProperty("expires_in") final Long anExpiresIn,
            @JsonProperty("refresh_token") final String aRefreshToken,
            @JsonProperty("profile_pic") final String aProfilePicture,
            @JsonProperty("features")  final List<String> theFeatures) {
        this.userName = anUserName;
        this.displayName = theDisplayName;
        this.authToken = anAuthToken;
        this.expiresIn = anExpiresIn;
        this.refreshToken = aRefreshToken;
        this.profilePicture = aProfilePicture;
        this.features = theFeatures;

        this.registrationToken = null;
    }

    /**
     * initializes the value for non registered user.
     *
     * @param anUserName      the an user name
     * @param aRegistrationToken aRegistrationToken
     * @param aProfilePicture the a profile picture
     */
    @JsonCreator
    public AuthenticationResponse(
            @JsonProperty("userName") final String anUserName,
            @JsonProperty("registration_token") final String aRegistrationToken,
            @JsonProperty("profile_pic") final String aProfilePicture) {
        this.userName = anUserName;
        this.registrationToken = aRegistrationToken;
        this.profilePicture = aProfilePicture;

        this.displayName = null;
        this.authToken = null;
        this.expiresIn = null;
        this.refreshToken = null;
        this.features = null;


    }

    /**
     * gets the value for expiresIn.
     *
     * @return expiresIn
     */
    public Long getExpiresIn() {
        return expiresIn;
    }

    /**
     * gets the value for auth token.
     *
     * @return auth token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * gets the value for registration token.
     *
     * @return registration token
     */
    public String getRegistrationToken() {
        return registrationToken;
    }

    /**
     * gets the value for refresh token.
     *
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * gets the value for profile picture.
     *
     * @return profile picture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * gets the value for userName.
     *
     * @return userName user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * gets the value for display Name.
     *
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets Features of User.
     * @return features
     */
    public List<String> getFeatures() {
        return features;
    }
}
