package me.huzhiwei.zuul.domain;

import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-21 16:31
 */
@Data
public class Client implements ClientDetails {
    private Long id;
    private String clientId;
    private String clientSecret;
    private String authoritiesString;
    private String resourceIdsString;
    private String scopeString;
    private Boolean available;
    private String authorizedGrantTypesString;

    @Override
    public Set<String> getResourceIds() {
        if (StringUtils.isBlank(this.resourceIdsString)) {
            return null;
        } else {
            return Sets.newHashSet(this.resourceIdsString.split(","));
        }
    }

    @Override
    public Set<String> getScope() {
        if (StringUtils.isBlank(this.scopeString)) {
            return null;
        } else {
            return Sets.newHashSet(this.scopeString.split(","));
        }
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        if (StringUtils.isBlank(this.authorizedGrantTypesString)) {
            return null;
        } else {
            return Sets.newHashSet(this.authorizedGrantTypesString.split(","));
        }
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return null;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
