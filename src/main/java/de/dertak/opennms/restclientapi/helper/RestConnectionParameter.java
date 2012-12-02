package de.dertak.opennms.restclientapi.helper;

import java.net.URL;

/**
 * <p>RestConnectionParameter class.</p>
 *
 * @author Ronny Trommer <ronny@opennms.org>
 * @version $Id: $
 * @since 1.8.1
 */
public interface RestConnectionParameter {

    public URL getBaseUrl();

    public void setBaseUrl(URL baseUrl);

    public int getPort();

    public String getUsername();

    public String getPassword();
}

