package com.cetys.instructions.controller;
/*****
 *   Created by Jose Armando Cardenas
 *   on 01/06/2020
 */

import com.cetys.instructions.dao.EmployeeRepository;
import com.cetys.instructions.dao.RequestRepository;
import com.cetys.instructions.dao.VendorRepository;
import com.cetys.instructions.model.Request;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/requests")
public class RequestController
{
    final RequestRepository requestRepository;

    final EmployeeRepository employeeRepository;

    final VendorRepository vendorRepository;

    public RequestController(
            RequestRepository requestRepository
            , EmployeeRepository employeeRepository
            , VendorRepository vendorRepository
    )
    {
        this.employeeRepository = employeeRepository;
        this.vendorRepository = vendorRepository;

        System.out.println("employeeRepository size: " + employeeRepository.count());
        System.out.println("vendorRepository size: " + vendorRepository.count());

        requestRepository.save(new Request("Required for workstation", employeeRepository.findTop1ByEmailEquals("luis.rey@prof.com"), vendorRepository.findTopBySoftwareEquals("Clearquest"), "2018-05-01"));
        requestRepository.save(new Request("Required for workstation", employeeRepository.findTop1ByEmailEquals("luis.rey@prof.com"), vendorRepository.findTopBySoftwareEquals("Clearcase"), "2018-05-01"));
        requestRepository.save(new Request("VM target automated system", employeeRepository.findTop1ByEmailEquals("sunil.vam@corp.com"), vendorRepository.findTopBySoftwareEquals("Matlab"), "2019-03-15"));

        this.requestRepository = requestRepository;
    }

    /***** READ *****/
    @GetMapping(value = { "", "/" })
    String listRequests(
            Model model
    ){
        model.addAttribute("requests", requestRepository.findAll());

        return "view/request/requests-list";
    }

    /***** CREATE *****/
    @GetMapping("/create")
    String createRequest(
            Request request
            , Model model
    ) {
        model.addAttribute("request", request);
        model.addAttribute("employees", employeeRepository.findByStatusEqualsOrderByLastnameAscFirstnameAsc("Active"));
        model.addAttribute("vendors", vendorRepository.findAllByStatusOrderByNameAsc(Boolean.TRUE));

        return "view/request/request-add";
    }

    @PostMapping("/add")
    ModelAndView addRequest(
            Request request
            , ModelMap model
    ){
        var newRequest = new Request( request.getJustification(), request.getEmployee(), request.getVendor() );
        requestRepository.save(newRequest);
        model.addAttribute("requests", requestRepository.findAll());

        return new ModelAndView("redirect:/requests");
    }

    /***** UPDATE *****/
    @GetMapping("/edit/{id}")
    String editRequest(
            @PathVariable("id") Long id
            , Model model
    ){
        var request = requestRepository.findById(id).get();
        model.addAttribute("request", request);
        model.addAttribute("employees", employeeRepository.findByStatusEqualsOrderByLastnameAscFirstnameAsc("Active"));
        model.addAttribute("vendors", vendorRepository.findAllByStatusOrderByNameAsc(Boolean.TRUE));

        return "view/request/request-edit";
    }

    @PostMapping("/update/{id}")
    ModelAndView updateRequest(
            @PathVariable("id") Long id
            , Request editedRequest
            , ModelMap model
    ){
        editedRequest.setReqdate(editedRequest.getReqDateUpdated());
        requestRepository.save(editedRequest);
        model.addAttribute("requests", requestRepository.findAll());

        return new ModelAndView("redirect:/requests");
    }

    /***** DELETE *****/
    @GetMapping("/delete/{id}")
    ModelAndView deleteRequest(
            @PathVariable("id") Long id
            , ModelMap model
    ){
        requestRepository.deleteById(id);
        model.addAttribute("requests", requestRepository.findAll());

        return new ModelAndView("redirect:/requests");
    }

}
