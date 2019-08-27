package me.huzhiwei.zuul.config;

import me.huzhiwei.zuul.constant.Constant;
import me.huzhiwei.zuul.domain.Client;
import me.huzhiwei.zuul.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * description:
 * author: koshitakashi
 * date: 2019-08-16 10:02
 */
@Configuration
public class OAuth2Config {

    @Configuration
    @EnableResourceServer
    static class ResourceServer extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(Constant.RESOURECE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/favicon.ico", "/oauth/**", "/gateway/**", "/actuator/**", "/hystrix/**").anonymous()
                    .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        }
    }


    @Configuration
    @EnableAuthorizationServer
    static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private ClientMapper clientMapper;

        @Autowired
        private DataSource dataSource;

        @Autowired
        @Qualifier("userDetailsServiceImpl")
        private UserDetailsService userDetailsService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            //提供password认证方式
            security.allowFormAuthenticationForClients();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(new ClientDetailsService() {

                @Override
                public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
                    Client client = clientMapper.selectByClientId(clientId);
                    if (client == null) {
                        throw new NoSuchClientException("No client with requested id: " + clientId);
                    }
                    return client;
                }
            });
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(new JdbcTokenStore(dataSource))
                    .authenticationManager(authenticationManager)
                    //不配置无法refresh token
                    .userDetailsService(userDetailsService);
        }

    }

}
