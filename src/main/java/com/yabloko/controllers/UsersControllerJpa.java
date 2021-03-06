package com.yabloko.controllers;

import com.yabloko.forms.UserForm;
import com.yabloko.models.Car;
import com.yabloko.repositories.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.yabloko.models.User;

import com.yabloko.repositories.UsersRepository;

import java.util.List;

@Controller
public class UsersControllerJpa {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CarsRepository carsRepository;

    @RequestMapping(path = "/users-jpa", method = RequestMethod.GET)
    public ModelAndView getUsers(@RequestParam(required = false, name = "first_name") String firstName) {
        List<User> users = null;

        if (firstName != null) {
            users = usersRepository.findAllByFirstName(firstName);
        } else {
            users = usersRepository.findAll();
        }

        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("usersFromServer", users);
        return modelAndView;
    }

    @RequestMapping(path = "/users-jpa", method = RequestMethod.POST)
    public String addUser(UserForm userForm, @RequestParam String model) {

        if (userForm.getFirstName() != "" ) {
            User newUser = User.from(userForm);
            usersRepository.save(newUser);
            if (model != null) {
                carsRepository.save(
                        Car.builder()
                                .model(model)
                                .owner(newUser)
                                .build());
            }
        }
        return "redirect:/users-jpa";
    }
}



















