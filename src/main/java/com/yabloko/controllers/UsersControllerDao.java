package com.yabloko.controllers;


import com.yabloko.dao.UsersDao;
import com.yabloko.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class UsersControllerDao {

    @Autowired
    private UsersDao usersDao;

    @RequestMapping(path = "/users-dao", method = RequestMethod.GET)
    public ModelAndView getAllUsers(@RequestParam(value = "first_name", required = false) String firstName) {
        List<User> users = null;

        if (firstName != null) {
            users = usersDao.findAllByFirstName(firstName);
        } else {
            users = usersDao.findAll();
        }
        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", users);
        return modelAndView;
    }

    // обращение к ресурсу в REST-стиле
    @RequestMapping(path = "/users-dao/{user-id}", method = RequestMethod.GET)
    public ModelAndView getUserById(@PathVariable("user-id") Long userId) {
        Optional<User> optional = usersDao.find(userId);
        ModelAndView modelAndView = new ModelAndView("users");


        if (optional.isPresent())
            modelAndView.addObject("usersFromServer",
                    Arrays.asList(optional.get()));

        // aka
        optional.ifPresent(
                user ->
                        modelAndView.addObject("usersFromServer",
                                Collections.singletonList(user)));

        return modelAndView;
    }
}