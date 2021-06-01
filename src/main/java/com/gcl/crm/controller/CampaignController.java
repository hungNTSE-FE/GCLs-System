package com.gcl.crm.controller;

import com.gcl.crm.form.CampaignDetailForm;
import com.gcl.crm.form.CampaignMaketingForm;
import com.gcl.crm.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class CampaignController {

    public static final String MAIN_PAGE = "campaign.html";
    public static final String CAMPAIGN_MAKETING_FORM = "CAMPAIGN_MAKETING_FORM";

    @Autowired
    CampaignService campaignService;

    @GetMapping(value = "/campaign")
    public String initScreen(@ModelAttribute CampaignMaketingForm form, BindingResult errors, Model model) {
        form = campaignService.init();
        form.setCampaignFromDate(new Date());
        form.setCampaignToDate(new Date());
        model.addAttribute(CAMPAIGN_MAKETING_FORM, form);
        return MAIN_PAGE;
    }

    @PostMapping(value = "/campaign/getCampaignMaketingForm")
    public ResponseEntity<CampaignMaketingForm> initScreen() {
        CampaignMaketingForm form = campaignService.init();
        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    @PostMapping(value = "/campaign/getListCampaignModal")
    public ResponseEntity<CampaignDetailForm> getListCampaignModal(@RequestBody String id) {
        CampaignDetailForm form = campaignService.getCampaignFormByPK(id);
        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    @PostMapping(value = "/campaign/saveCampaignDetail")
    public ResponseEntity saveCampaignDetail(CampaignDetailForm form){
        if (campaignService.saveCampaignDetailForm(form)) return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/campaign/deteleCampaignDetail")
    public ResponseEntity deteleCampaignDetail(@RequestBody String selectedId){
        if (campaignService.deleteCampaignDetailForm(Integer.parseInt(selectedId))) return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}
