package com.payneteasy.dengisend.domain.model;

import android.support.annotation.Nullable;

/**
 * Dengisend
 *
 * Created by Alex Oleynyak on 01/08/2017.
 * Copyright © 2017 Payneteasy. All rights reserved.
 */

public class Session {

    @Nullable
    private String accessToken;

    @Nullable
    private String token;

    @Nullable
    private String nonce;

    @Nullable
    private String signature;

    public Session(@Nullable String accessToken) {
        this.accessToken = accessToken;
    }

    @Nullable
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(@Nullable String accessToken) {
        this.accessToken = accessToken;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public String getNonce() {
        return nonce;
    }

    public void setNonce(@Nullable String nonce) {
        this.nonce = nonce;
    }

    @Nullable
    public String getSignature() {
        return signature;
    }

    public void setSignature(@Nullable String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;

        Session session = (Session) o;

        if (accessToken != null ? !accessToken.equals(session.accessToken) : session.accessToken != null)
            return false;
        if (token != null ? !token.equals(session.token) : session.token != null) return false;
        if (nonce != null ? !nonce.equals(session.nonce) : session.nonce != null) return false;
        return signature != null ? signature.equals(session.signature) : session.signature == null;

    }

    @Override
    public int hashCode() {
        int result = accessToken != null ? accessToken.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (nonce != null ? nonce.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "accessToken='" + accessToken + '\'' +
                ", token='" + token + '\'' +
                ", nonce='" + nonce + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}