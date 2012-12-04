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
import org.opennms.netmgt.model.OnmsIpInterfaceList;
import org.opennms.netmgt.model.OnmsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>RestNodeProvider class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>*
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class RestNodeProvider {

    private static Logger logger = LoggerFactory.getLogger(RestNodeProvider.class);

    public static List<OnmsNode> getNodes(ApacheHttpClient httpClient, String baseUrl, String parameters) {
        WebResource webResource = httpClient.resource(baseUrl + "rest/nodes" + parameters);
        List<OnmsNode> nodes = null;
        try {
            nodes = webResource.header("Accept", "application/xml").get(new GenericType<List<OnmsNode>>() {
            });
        } catch (Exception ex) {
            logger.debug("Rest-Call for Nodes went wrong", ex);
        }
        return nodes;
    }

//    public static Set<OnmsIpInterface> getIpInterfacesByNode(ApacheHttpClient httpClient, String baseUrl, Integer nodeId, String parameters) {
//    WebResource webResource = httpClient.resource(baseUrl + "rest/nodes/" + nodeId + "/ipinterfaces" + parameters);
//        Set<OnmsIpInterface> ipInterfaces = new HashSet<OnmsIpInterface>();
//        try {
//            ipInterfaces = webResource.header("Accept", "application/xml").get(new GenericType<Set<OnmsIpInterface>>() {});
//        } catch (Exception ex) {
//            logger.debug("Rest-Call for IpInterfaces by Nodes went wrong");
//        }
//        return ipInterfaces;
//    }

    public static OnmsIpInterfaceList getIpInterfacesByNode(ApacheHttpClient httpClient, String baseUrl, Integer nodeId, String parameters) {
        WebResource webResource = httpClient.resource(baseUrl + "rest/nodes/" + nodeId + "/ipinterfaces" + parameters);
        OnmsIpInterfaceList ipInterfaces = new OnmsIpInterfaceList();
        try {
            ipInterfaces = webResource.header("Accept", "application/xml").get(OnmsIpInterfaceList.class);
        } catch (Exception ex) {
            logger.debug("Rest-Call for IpInterfaces by Nodes went wrong", ex);
        }
        return ipInterfaces;
    }
}
