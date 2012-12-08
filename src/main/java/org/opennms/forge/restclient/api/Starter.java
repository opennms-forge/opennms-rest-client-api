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

import org.opennms.forge.restclient.utils.OnmsRestConnectionParameter;
import org.opennms.forge.restclient.utils.RestConnectionParameter;
import org.opennms.forge.restclient.utils.RestHelper;
import org.opennms.netmgt.model.OnmsOutage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.List;

/**
 * <p>Starter class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>*
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
class Starter {

    private static Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        logger.info("OpenNMS Rest Client API");

        String baseUrl = "http://demo.opennms.com/opennms/";

        String username = "demo";
        String password = "demo";

        logger.info("Getting Outages from " + baseUrl);
        try {
            RestConnectionParameter restConnectionParameter = new OnmsRestConnectionParameter(baseUrl, username, password);
            List<OnmsOutage> outages = RestOutageProvider.getOutages(RestHelper.createApacheHttpClient(restConnectionParameter), "", "");
            logger.info("Got '{}' from '{}'", outages.size(), baseUrl);
            logger.info("Thanks for computing with OpenNMS!");
        } catch (MalformedURLException e) {
            logger.error("Invalid base URL '{}'", baseUrl);
            System.exit(1);
        }
    }
}
