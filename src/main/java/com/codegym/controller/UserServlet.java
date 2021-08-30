package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.service.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("Homepage");
        if(action == null){
            action = "";
        }
        switch (action) {
            case "create":
                showCreateForm(req,resp);
                break;
            case "edit":
                showEditForm(req,resp);
                break;
            case "delete":
                deleteUser(req, resp);
                break;
            default:
                homePage(req, resp);
                break;
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        try {
            userDAO.deleteUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<User> listUser = userDAO.selectAllUsers();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("listUser.jsp");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("edit.jsp");
        req.setAttribute("user", existingUser);
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher dispatcher = req.getRequestDispatcher("create.jsp");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void homePage(HttpServletRequest req, HttpServletResponse resp) {
        List<User> listUser = userDAO.selectAllUsers();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("listUser.jsp");
        try {
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("Homepage");
        if(action == null){
            action = "";
        }
        switch (action){
            case "create":
                createUser(req,resp);
                break;
            case "edit":
                editUser(req,resp);
                break;
        }
    }

    private void editUser(HttpServletRequest req, HttpServletResponse resp) {
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");

        User book = new User(id, name, email, country);
        RequestDispatcher dispatcher = req.getRequestDispatcher("edit.jsp");
        try {
            userDAO.updateUser(book);
            dispatcher.forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        User newUser = new User(name, email, country);
        userDAO.insertUser(newUser);
        try {
            resp.sendRedirect("/users?Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
