package com.gcl.crm.controller;

import com.gcl.crm.entity.Potential;
import com.gcl.crm.service.PotentialService;
import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/junk")
public class JunkController {

    @Autowired
    PotentialService potentialService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model) {
        List<Potential> potentials = potentialService.getAllDeletedPotentials();
        model.addAttribute("potentials", potentials);
        return "/junk/data-junk-page-hungNT";
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.GET)
    public String resetPotential(Model model, @Nullable @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean done = potentialService.resetPotential(id);
        if (done) {
            System.out.println("Ket qua: success");
            redirectAttributes.addFlashAttribute("flag","showAlert");
        } else {
            System.out.println("False");
        }
        return "redirect:/junk/home";
    }

    @RequestMapping(value = "/resetAll", method = RequestMethod.POST)
    public String resetAllPotential(Model model, @Nullable @RequestParam("checkedbox") List<Long> checkedPotential, RedirectAttributes redirectAttributes) {
        System.out.println(checkedPotential);
        if (checkedPotential == null) {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
            return "redirect:/junk/home";
        }
        Potential potential = new Potential();
        boolean done = potentialService.resetAllPotential(potential, checkedPotential);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/junk/home";
    }
}
