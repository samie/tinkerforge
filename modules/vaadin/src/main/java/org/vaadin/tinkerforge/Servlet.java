package org.vaadin.tinkerforge;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import javax.servlet.annotation.WebServlet;

/**
 * Servlet bootstrap.
 *
 * @author Sami Ekblad
 */
@WebServlet(value = {"/*"}, asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = TinkerForgeDashBoardUI.class)
public class Servlet extends VaadinServlet {

}
