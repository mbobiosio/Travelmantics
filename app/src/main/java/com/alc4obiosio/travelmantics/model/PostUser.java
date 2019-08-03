package com.alc4obiosio.travelmantics.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
@IgnoreExtraProperties
public class PostUser {

    String displayName;
    String photoUrl;
    String lastAccess;
    String email;
    String providers;

    public PostUser() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public PostUser(String display_name, String photo_url, String last_access, String email,
                    String provider) {
        this.displayName = display_name;
        this.photoUrl = photo_url;
        this.lastAccess = last_access;
        this.email = email;
        this.providers = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("displayName", displayName);
        result.put("photoUrl", photoUrl);
        result.put("email", email);
        result.put("provider", providers);
        result.put("lastAccess", lastAccess);
        return result;
    }
}
