package org.opennms.forge.restclient.utils;

import java.net.URL;

/**
 * <p>RestConnectionParameter class.</p>
 *
 * @author <a href="mailto:markus@opennms.org">Markus Neumann</a>*
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */
public interface RestConnectionParameter {

    public URL getBaseUrl();

    public void setBaseUrl(URL baseUrl);

    public int getPort();

    public String getUsername();

    public String getPassword();
}

