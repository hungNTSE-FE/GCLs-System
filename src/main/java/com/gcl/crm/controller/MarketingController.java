package com.gcl.crm.controller;

import com.gcl.crm.dto.SourceEvaluationDto;
import com.gcl.crm.entity.User;
import com.gcl.crm.form.*;
import com.gcl.crm.service.MarketingServices;
import com.gcl.crm.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/marketing")
public class MarketingController {

    public static final String MAIN_PAGE = "marketing/marketing.html";
    public static final String CUSTOMER_REPORT_PAGE = "marketing/customerStatusReport.html";
    public static final String MKT_KPI_EVALUATION_REPORT_PAGE = "/report/agency-page";
    public static final String CUSTOMERSTATUS_PAGE = "/report/customerStatus-page";
    public static final String APP_USER = "users";

    @Autowired
    MarketingServices maketingServices;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/")
    public String initMarketingScreen(Model model) {
        maketingServices.initScreen();
        return MAIN_PAGE;
    }

    @RequestMapping(value = "/customerStatusReport")
    public String initScreenCustomerStatusReport() {
        return CUSTOMER_REPORT_PAGE;
    }

    @PostMapping(value = "/getListCustomerStatusReport")
    public String getCustomerStatusReport(@ModelAttribute CustomerStatusReportForm customerStatusReportForm, Model model
                    , Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        CustomerStatusReportForm customerNewForm = maketingServices.getCustomerStatusReport(customerStatusReportForm);
        model.addAttribute("customerStatusReportForm", customerNewForm);
        model.addAttribute("userInfo", currentUser);
        return CUSTOMERSTATUS_PAGE;
    }

    @PostMapping(value = "/getKPIMktGroup")
    public String getKPIMktGroup(Model model, @ModelAttribute KPIMktGroupForm kpiMktGroupForm
                                                                            , Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        KPIMktGroupForm newForm = maketingServices.getKPIMktGroup(kpiMktGroupForm);
        model.addAttribute("KPI_MKT_GROUP_FORM" , newForm);
        model.addAttribute("userInfo", currentUser);
        return MKT_KPI_EVALUATION_REPORT_PAGE;
    }

    @PostMapping(value = "/distributionPotential")
    public String distributionPotential(@ModelAttribute CustomerDistributionForm customerDistributionForm
            , Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(principal.getName());
        maketingServices.distributeCustomerData(customerDistributionForm, user);
        redirectAttributes.addFlashAttribute("flag", "showAlertShareSuccess");
        return "redirect:/potential/home";
    }

    @PostMapping(value = "/getSourceEvaluation")
    public String getSourceEvaluation(@ModelAttribute SourceEvaluationForm sourceEvaluationForm, Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        List<SourceEvaluationDto> sourceEvaluationDtoList = maketingServices.getSourceEvaluation(sourceEvaluationForm);
        sourceEvaluationForm.setSourceEvaluationDtoList(sourceEvaluationDtoList);
        sourceEvaluationForm.setTotal(sourceEvaluationDtoList.get(0).getSumOfPotential());
        model.addAttribute("sourceEvaluationFormJSON", new Gson().toJson(sourceEvaluationForm));
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("sourceEvaluationForm", sourceEvaluationForm);
        String sourceLabel = sourceEvaluationForm.getSourceEvaluationDtoList()
                                                .stream()
                                                .map(source -> source.getSourceName())
                                                .collect(Collectors.toList())
                                                .toString().replace("[", "").replace("]", "");
        String sourceData = sourceEvaluationForm.getSourceEvaluationDtoList()
                            .stream()
                            .map(SourceEvaluationDto::getNumOfPotential)
                            .collect(Collectors.toList())
                            .toString().replace("[", "").replace("]", "");
        model.addAttribute("sourceLabel", sourceLabel);
        model.addAttribute("sourceData", sourceData);
        return "report/source-page.html";
    }

    @PostMapping(value = "getSummaryMKTReport")
    public String getSummaryMKTReport(@ModelAttribute MarketingSummaryReportForm marketingSummaryReportForm, Model model
    , Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        MarketingSummaryReportForm newForm = maketingServices.getSummaryMKTReport(marketingSummaryReportForm);

        model.addAttribute("userInfo", currentUser);
        model.addAttribute("marketingSummaryReportForm", newForm);
        return "report/summary-page";
    }

}
