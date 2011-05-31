/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugins.springsocial.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.facebook.api.FacebookApi
import org.springframework.social.twitter.api.TwitterApi

@Configuration
class ApisConfig {

    /*@Bean
    @Scope(value = "request")
    public TripItApi tripitApi(ConnectionRepository connectionRepository) {
        Connection<TripItApi> connection = connectionRepository.findPrimaryConnectionToApi(TripItApi.class)
        return connection != null ? connection.getApi() : null
    }*/

    @Bean
    @Scope(value = "request")
    public TwitterApi twitterApi(ConnectionRepository connectionRepository) {
        Connection<TwitterApi> connection = connectionRepository.findPrimaryConnectionToApi(TwitterApi.class)
        return connection != null ? connection.getApi() : null
    }

    @Bean
    @Scope(value = "request")
    FacebookApi facebookApi(ConnectionRepository connectionRepository) {
        Connection<FacebookApi> connection = connectionRepository.findPrimaryConnectionToApi(FacebookApi.class)
        return connection != null ? connection.getApi() : null
    }

}

