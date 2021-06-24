package com.gcl.crm.controller;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.Source;
import com.gcl.crm.repository.SourceRepository;
import com.gcl.crm.service.PotentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/potential")
public class PotentialController {
    @Autowired
    PotentialService potentialService;

    @Autowired
    SourceRepository sourceRepository;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model) {
        return "/potential/home-potential-hungNT-V2";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String goCreatePage(Model model) {
        List<Source> sources = sourceRepository.getAll();

        Potential potential = new Potential();
        model.addAttribute("potentialForm", potential);
        model.addAttribute("sources", sources);
        return "/potential/create-potential-hungNT-V2";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("potentialForm") Potential potential, RedirectAttributes redirectAttributes) {
        potential.setDate(getCurrentDate());
        boolean done = potentialService.createPotential(potential);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/potential/create";
    }

    private String getCurrentDate() {
        String newYorkDateTimePattern = "dd/MM/yyyy";
        DateTimeFormatter newYorkDateFormatter = DateTimeFormatter.ofPattern(newYorkDateTimePattern);
        String formattedDate = newYorkDateFormatter.format(LocalDate.now());
        return formattedDate;
    }
}
