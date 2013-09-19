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
package org.opennms.forge.restclient.utils;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

/**
 * <p>RestHelper class.</p>
 * <p/>
 * Providing static method to generate the HTTP client with given connection parameter.
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class RestHelper {

    /**
     * <p>createApacheHttpClient</p>
     * <p/>
     * Initialize HTTP client for ReST call
     *
     * @param restConnectionParameter Connection parameter for HTTP client as {@link RestConnectionParameter}
     * @return configured HTTP client as {@link com.sun.jersey.client.apache.ApacheHttpClient}
     */
    public static ApacheHttpClient createApacheHttpClient(RestConnectionParameter restConnectionParameter) {
        DefaultApacheHttpClientConfig httpClientConfig = new DefaultApacheHttpClientConfig();

        httpClientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        
        httpClientConfig.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION, Boolean.TRUE); 
        httpClientConfig.getState().setCredentials(null, restConnectionParameter.getBaseUrl().getHost(), restConnectionParameter.getPort(), restConnectionParameter.getUsername(), restConnectionParameter.getPassword());

        ApacheHttpClient httpClient = ApacheHttpClient.create(httpClientConfig);

        return httpClient;
    }
}
