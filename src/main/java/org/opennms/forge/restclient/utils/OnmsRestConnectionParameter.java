/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

import org.opennms.forge.restclient.utils.RestConnectionParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>OnmsRestConnectionParameter class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>*
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public class OnmsRestConnectionParameter implements RestConnectionParameter {

    private static Logger logger = LoggerFactory.getLogger(OnmsRestConnectionParameter.class);

    private URL m_baseUrl;

    private String m_username = "";

    private String m_password = "";

    public OnmsRestConnectionParameter(String baseUrl, String username, String password) throws MalformedURLException {
        m_username = username;
        m_password = password;
        m_baseUrl = new URL(baseUrl);
    }

    @Override
    public URL getBaseUrl() {
        return this.m_baseUrl;
    }

    @Override
    public void setBaseUrl(URL baseUrl) {
        this.m_baseUrl = baseUrl;
    }

    @Override
    public int getPort() {
        return this.m_baseUrl.getPort();
    }

    @Override
    public String getUsername() {
        return this.m_username;
    }

    @Override
    public String getPassword() {
        return this.m_password;
    }
}
