package com.opuscapita.peppol.commons.auth;

import org.apache.commons.lang3.StringUtils;

public class AuthorizationResponse {

    private String access_token;
    private Integer expires_in;
    private String token_type;
    private String id_token;
    private String refresh_token;

    public String getAccess_token() {
        return access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getId_token() {
        return id_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getAuthorizationHeader() {
        if (StringUtils.isBlank(token_type) || StringUtils.isBlank(access_token)) {
            return null;
        }
        return token_type + " " + access_token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((token_type == null) ? 0 : token_type.hashCode());
        result = prime * result + ((access_token == null) ? 0 : access_token.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthorizationResponse other = (AuthorizationResponse) obj;
        if (token_type == null) {
            if (other.token_type != null) {
                return false;
            }
        } else if (!token_type.equals(other.token_type)) {
            return false;
        }
        if (access_token == null) {
            if (other.access_token != null) {
                return false;
            }
        } else if (!access_token.equals(other.access_token)) {
            return false;
        }
        return true;
    }

}
