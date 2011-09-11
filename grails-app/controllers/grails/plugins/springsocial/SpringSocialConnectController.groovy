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
package grails.plugins.springsocial

import grails.plugins.springsocial.connect.web.GrailsConnectSupport
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.social.connect.DuplicateConnectionException
import org.springframework.web.context.request.RequestAttributes

class SpringSocialConnectController {

    private static final String DUPLICATE_CONNECTION_EXCEPTION_ATTRIBUTE = "_duplicateConnectionException"
    private static final String DUPLICATE_CONNECTION_ATTRIBUTE = "social.addConnection.duplicate"

    def connectionFactoryLocator
    def connectionRepository
    def grailsApplication
    def cfg = grailsApplication.mergedConfig.asMap(true).grails.plugin.springsocial

    def webSupport = new GrailsConnectSupport(home: g.createLink(uri: "/", absolute: true))

    static allowedMethods = [connect: 'POST', oauthCallback: 'GET', disconnect: 'DELETE']

    def connect = {
        def providerId = params.providerId
        def connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId)
        def nativeWebRequest = new GrailsWebRequest(request, response, servletContext)
        def url = webSupport.buildOAuthUrl(connectionFactory, nativeWebRequest)
        redirect url: url
    }

    def oauthCallback = {
        def providerId = params.providerId
        def uriRedirect = session.ss_oauth_redirect_callback
        def config = cfg.get(providerId)
        def uri = uriRedirect ?: config.page.connectedHome

        def connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId)
        def connection = webSupport.completeConnection(connectionFactory, new GrailsWebRequest(request, response, servletContext))

        addConnection(connection, connectionFactory, request)
        redirect(uri: uri)
    }

    def disconnect = {
        def providerId = params.providerId
        assert providerId, "The providerId is required"
        if(log.isInfoEnabled()) {
            log.info("Disconecting from ${providerId}")
        }
        connectionRepository.removeConnections(providerId)
        def postDisconnectUri = params.ss_post_disconnect_uri ?: cfg.postDisconnectUri
        if(log.isInfoEnabled()) {
            log.info("redirecting to ${postDisconnectUri}")
        }
        redirect(uri: postDisconnectUri)
    }

    private void addConnection(connection, connectionFactory, request) {
        try {
            connectionRepository.addConnection(connection)
            //postConnect(connectionFactory, connection, request)
        } catch (DuplicateConnectionException e) {
            request.setAttribute(DUPLICATE_CONNECTION_ATTRIBUTE, e, RequestAttributes.SCOPE_SESSION);
        }
    }
}
