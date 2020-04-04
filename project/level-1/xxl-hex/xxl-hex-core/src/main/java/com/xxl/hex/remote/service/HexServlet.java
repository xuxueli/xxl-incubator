package com.xxl.hex.remote.service;

import com.xxl.hex.handler.HexHandlerFactory;
import com.xxl.hex.remote.client.HexClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * hex servlet
 * @author xuxueli 
 * @version 2015-11-28 13:56:18
 */
public class HexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(HexServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// param
		String mapping = request.getParameter(HexClient.MAPPING);
		String request_hex = request.getParameter(HexClient.HEX);
		String plain = request.getParameter(HexClient.PLAIN);

		// handleObj
		String response_data = null;
		if (plain!=null && "true".equals(plain)) {
			response_data = HexHandlerFactory.dispatchHandlerPlain(request, response, mapping);
		} else {
			response_data = HexHandlerFactory.dispatchHandler(request, response, mapping, request_hex);
		}


		// response
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(response_data);
	}
	
}
