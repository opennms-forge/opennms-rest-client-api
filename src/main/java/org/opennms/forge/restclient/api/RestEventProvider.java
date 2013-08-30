/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.forge.restclient.api;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.opennms.forge.restclient.utils.RestConnectionParameter;
import org.opennms.forge.restclient.utils.RestHelper;
import org.opennms.netmgt.model.OnmsEvent;
import org.opennms.netmgt.model.OnmsEventCollection;

/**
 * <p>RestEventProvider class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class RestEventProvider {

    private static Logger logger = LoggerFactory.getLogger(RestEventProvider.class);

    public static List<OnmsEvent> getEvents(RestConnectionParameter restConnectionParameter, String parameters) {
        ApacheHttpClient apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        String baseUrl = restConnectionParameter.getBaseUrl().toString();
        
        WebResource webResource = apacheHttpClient.resource(baseUrl + "rest/events" + parameters);
        List<OnmsEvent> events = null;
        try {
            events = webResource.header("Accept", "application/xml").get(new GenericType<List<OnmsEvent>>() {});
        } catch (Exception ex) {
            logger.debug("Rest-Call for Events went wrong", ex);
        }
        return events;
    }

    public static List<OnmsEvent> getEventsByUEI(RestConnectionParameter restConnectionParameter, String uei, String parameters) {
        ApacheHttpClient apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        String baseUrl = restConnectionParameter.getBaseUrl().toString();

        WebResource webResource = apacheHttpClient.resource(baseUrl + "rest/events?eventUei=" + uei + parameters);
        List<OnmsEvent> events = null;
        try {
            events = webResource.header("Accept", "application/xml").get(new GenericType<List<OnmsEvent>>() {});
        } catch (Exception ex) {
            logger.debug("Rest-Call for Events went wrong", ex);
        }
        return events;
    }

    public static List<OnmsEvent> getEventsByNodeLabelAndUEI(RestConnectionParameter restConnectionParameter, String nodeLable, String uei, String parameters) {
        ApacheHttpClient apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        String baseUrl = restConnectionParameter.getBaseUrl().toString();

        WebResource webResource = apacheHttpClient.resource(baseUrl + "rest/events?eventUei=" + uei + parameters);
        List<OnmsEvent> events = null;
        try {
            events = webResource.header("Accept", "application/xml").get(new GenericType<List<OnmsEvent>>() {});
        } catch (Exception ex) {
            logger.debug("Rest-Call for Events went wrong", ex);
        }
        return events;
    }

    public static List<OnmsEvent> getEventsByServerity(RestConnectionParameter restConnectionParameter, String serverity, String parameters) {
        ApacheHttpClient apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        String baseUrl = restConnectionParameter.getBaseUrl().toString();

        WebResource webResource = apacheHttpClient.resource(baseUrl + "rest/events/?eventSeverity=" + getServeretyIdByLable(serverity) + parameters);
        List<OnmsEvent> events = null;
        try {
            events = webResource.header("Accept", "application/xml").get(new GenericType<List<OnmsEvent>>() {});
        } catch (Exception ex) {
            logger.debug("Rest-Call Events by Serverity " + serverity + " went wrong", ex);
        }
        return events;
    }

    public static Integer countEventsByServerity(RestConnectionParameter restConnectionParameter, String serverity, String parameters) {
        ApacheHttpClient apacheHttpClient = RestHelper.createApacheHttpClient(restConnectionParameter);
        String baseUrl = restConnectionParameter.getBaseUrl().toString();

        WebResource webResource = apacheHttpClient.resource(baseUrl + "rest/events/?eventSeverity=" + getServeretyIdByLable(serverity) + parameters);
        OnmsEventCollection events = null;
        try {
            events = webResource.header("Accept", "application/xml").get(OnmsEventCollection.class);
        } catch (Exception ex) {
            logger.debug("Rest-Call Events by Serverity " + serverity + " went wrong", ex);
        }
        return events.getTotalCount();
    }

    private static Integer getServeretyIdByLable(String serverity) {

        if(serverity.equalsIgnoreCase("INDETERMINATE")) {
            return 1;
        }
        if(serverity.equalsIgnoreCase("CLEARED")) {
            return 2;
        }
        if(serverity.equalsIgnoreCase("NORMAL")) {
            return 3;
        }
        if(serverity.equalsIgnoreCase("WARNING")) {
            return 4;
        }
        if(serverity.equalsIgnoreCase("MINOR")) {
            return 5;
        }
        if(serverity.equalsIgnoreCase("MAJOR")) {
            return 6;
        }
        if(serverity.equalsIgnoreCase("CRITICAL")) {
            return 7;
        }
        return null;
    }

}