package com.in28minutes.servletjsp.web.servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.in28minutes.data.impl.UserSessionImpl;
import com.in28minutes.domain.User;
import com.in28minutes.web.common.util.TodoListUtils;

@WebServlet(name = "LogoutServlet", urlPatterns = "/user/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -5807993518701671491L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute(TodoListUtils.SESSION_USER);
		UserSessionImpl.updateSessionStatus(user.getName(), user.getEmail(), "INActive");
		request.getSession().invalidate();
		request.getRequestDispatcher("/").forward(request, response);
	}

}
