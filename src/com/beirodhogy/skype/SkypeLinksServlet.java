package com.beirodhogy.skype;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.beirodhogy.data.LinkDataBroker;

@SuppressWarnings("serial")
public class SkypeLinksServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(SkypeLinksServlet.class.getName());

	/** save data on POST */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("new POST request : " + req.getRequestURL());
		storeLink(req);
		createResponse(resp);
	}

	/** save data on GET */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("new GET request : " + req.getRequestURL());
		storeLink(req);
		createResponse(resp);
	}

	private void storeLink(HttpServletRequest req) {

		String magick = req.getParameter("magick");
		String text = req.getParameter("text");
		String channel = req.getParameter("channel");

		int ch = Integer.valueOf(channel);

		LinkDataBroker.saveNewLink(ch, text, magick);
	}

	private void createResponse(HttpServletResponse resp) throws IOException {
		// resp.setContentType("text/plain");
		// resp.getWriter().println("ok");
		resp.sendRedirect("/");
	}
}
