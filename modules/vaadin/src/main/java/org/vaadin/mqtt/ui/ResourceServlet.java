package org.vaadin.mqtt.ui;

import com.vaadin.server.VaadinServlet;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet bootstrap.
 *
 * @author Sami Ekblad
 */
@WebServlet(value = {"/VAADIN/*"}, asyncSupported = true)
public class ResourceServlet extends VaadinServlet {

}
