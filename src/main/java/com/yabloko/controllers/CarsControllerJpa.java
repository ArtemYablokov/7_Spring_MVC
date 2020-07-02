package com.yabloko.controllers;

import com.yabloko.models.Car;
import com.yabloko.repositories.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CarsController {

    @Autowired
    private CarsRepository carsRepository;


    @RequestMapping(path = "/cars", method = RequestMethod.GET)
    @ResponseBody
    public String getCarsByOwnerFirstName(@RequestParam(name = "first_name", defaultValue = "") String firstName) {
        List<Car> cars = carsRepository.findAllByOwner_FirstName(firstName);
        return cars.toString();
    }
}