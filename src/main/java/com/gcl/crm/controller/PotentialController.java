package com.gcl.crm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcl.crm.config.AppConst;
import com.gcl.crm.config.CommonConst;
import com.gcl.crm.dto.CustomerDTO;
import com.gcl.crm.entity.*;
import com.gcl.crm.form.CustomerDistributionForm;
import com.gcl.crm.form.CustomerForm;
import com.gcl.crm.form.EmployeeSearchForm;
import com.gcl.crm.form.PotentialSearchForm;
import com.gcl.crm.repository.CustomerDistributionRepository;
import com.gcl.crm.repository.SourceRepository;
import com.gcl.crm.service.*;
import com.gcl.crm.utils.ExcelReader;
import com.gcl.crm.utils.ValidateUtil;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.mail.internet.MimeMessage;
import javax.swing.text.html.Option;

@Controller
@RequestMapping("/potential")
public class PotentialController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialController.class);

    @Autowired
    PotentialService potentialService;

    @Autowired
    DiaryService diaryService;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    LevelService levelService;

    @Autowired
    UserService userService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    MarketingGroupService marketingGroupService;

    @Autowired
    CustomerDistributionRepository customerDistributionRepository;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        String roleEmployee = potentialService.getDepartmentByUserName(user);

        if ("SALE".equals(roleEmployee)) {
            return CommonConst.REDIRECT + CommonConst.PATH_SALESMAN_POTENTIAL_HOME;
        }

        List<Source> sources = sourceRepository.getAll();
        List<Level> levels = levelService.getAll();
        PotentialSearchForm searchForm = new PotentialSearchForm();
        List<Potential> potentials = potentialService.getAllPotentials();
        List<MarketingGroup> marketingGroups = marketingGroupService.getAllMktByStatus();
        CustomerDistributionForm customerDistributionForm = new CustomerDistributionForm();
        Map<Long, String > marketingGroupsMap = Optional.ofNullable(marketingGroups)
                                                        .orElse(Collections.emptyList())
                                                        .stream()
                                                        .collect(Collectors.toMap(MarketingGroup::getId, MarketingGroup::getName));
        Map<Long, String> potentialMap = new HashMap<>();
        for (CustomerDistribution customerDistribution : customerDistributionRepository.getAll()) {
            if (customerDistribution.getPotential().getId() == null) {
                potentialMap.put(customerDistribution.getPotential().getId(),
                        marketingGroupsMap.get(customerDistribution.getMarketingGroup().getId()));
            }
        };
        model.addAttribute("potentials", potentials);
        model.addAttribute("marketingGroups", marketingGroups);
        model.addAttribute("customerDistributionForm", customerDistributionForm);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("sources", sources);
        model.addAttribute("levels", levels);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", user);
        model.addAttribute("potentialMap", potentialMap);

        if ("MARKETING".equals(roleEmployee)) {
            return CommonConst.HOME_POTENTIAL_TEMPLATE;
        }
        return CommonConst.ERROR_400_TEMPLATE;
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String goDetailInformationCustomer(Model model, @PathVariable("id") Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potentialDetail = potentialService.getPotentialById(id);
        Potential potentialEntity = new Potential();
        if (potentialDetail == null) {
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        model.addAttribute("potentialDetail", potentialDetail);
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potentialDetail.getLevel());
        model.addAttribute("potentialEntity", potentialEntity);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CommonConst.GET_DETAIL_INFORMATION_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/detail/takecare/MKT/{id}", method = RequestMethod.GET)
    public String goDetailTakeCarePotentialOfMKT(Model model, @PathVariable("id") Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potentialDetail = potentialService.getPotentialById(id);
        if (potentialDetail == null) {
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potentialDetail.getLevel());
        model.addAttribute("potentialDetail", potentialDetail);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CommonConst.GET_DETAIL_TAKECARE_MKT_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/detail/diary/{id}", method = RequestMethod.GET)
    public String goDetailDiary(Model model, @PathVariable("id") Long potentialId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potential = potentialService.getPotentialById(potentialId);
        if (potential == null) {
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        TreeMap<Diary, Employee> diaries = new TreeMap<>(diaryService.getDiaryMap(potentialId));
        model.addAttribute("diaries", diaries);
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potential.getLevel());
        model.addAttribute("potentialDetail", potential);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CommonConst.GET_DETAIL_DIARY_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @Nullable @ModelAttribute("searchForm") PotentialSearchForm searchForm,
                         Principal principal, RedirectAttributes redirectAttributes){
        if (searchForm == null){
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        CustomerDistributionForm customerDistributionForm = new CustomerDistributionForm();
        List<Source> sources = sourceRepository.getAll();
        List<Level> levels = levelService.getAll();

        List<MarketingGroup> marketingGroups = marketingGroupService.getAllMktByStatus();
        Map<Long, String > marketingGroupsMap = Optional.ofNullable(marketingGroups)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(MarketingGroup::getId, MarketingGroup::getName));
        Map<Long, String> potentialMap = new HashMap<>();
        for (CustomerDistribution customerDistribution : customerDistributionRepository.getAll()) {
            potentialMap.put(customerDistribution.getPotential().getId(),
                    marketingGroupsMap.get(customerDistribution.getMarketingGroup().getId()));
        }
        model.addAttribute("userInfo", currentUser);
        model.addAttribute("sources", sources);
        model.addAttribute("levels", levels);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("potentialMap", potentialMap);
        model.addAttribute("customerDistributionForm", customerDistributionForm);
        try {
            List<Potential> potentials = potentialService.search(searchForm);
            model.addAttribute("potentials", potentials);
        } catch (Exception e) {
            LOGGER.error("Has exception when search ", e);
        }
        return CommonConst.HOME_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String goCreatePage(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        List<Source> sources = sourceRepository.getAll();
        Potential potential = new Potential();
        model.addAttribute("potentialForm", potential);
        model.addAttribute("sources", sources);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CommonConst.CREATE_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/detail/edit/{id}", method = RequestMethod.GET)
    public String goEditPage(Model model, Principal principal, @Nullable @PathVariable("id") Long id) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potential = potentialService.getPotentialById(id);
        if (ValidateUtil.isNullOrEmpty(principal)) {
            return CommonConst.ERROR_400_TEMPLATE;
        }
        if (ValidateUtil.isNullOrEmpty(id) || ValidateUtil.isNullOrEmpty(potential)){
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        if (ValidateUtil.isNullOrEmpty(potential.getSource())){
            potential.setSource(new Source());
        }
        List<Source> sources = sourceRepository.getAll();
        model.addAttribute("potential", potential);
        model.addAttribute("sources", sources);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CommonConst.UPDATE_POTENTIAL_TEMPLATE;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("potentialForm") Potential potential, RedirectAttributes redirectAttributes, Principal principal) {
        boolean error = false;
        User currentUser = userService.getUserByUsername(principal.getName());
        if (potentialService.isPhoneExisted(potential.getPhoneNumber(), potential.getId())){
            model.addAttribute(CommonConst.DUPLICATE_PHONE_STRING, CommonConst.DUPLICATE_PHONE_MESSAGE);
            error = true;
        }
        if (potentialService.isEmailExisted(potential.getEmail(), potential.getId())){
            model.addAttribute(CommonConst.DUPLICATE_EMAIL_STRING, CommonConst.DUPLICATE_EMAIL_MESSAGE);
            error = true;
        }
        if (error){
            List<Source> sources = sourceRepository.getAll();
            model.addAttribute(CommonConst.SOURCE_STRING, sources);
            model.addAttribute(CommonConst.USER_STRING,currentUser);
            return CommonConst.CREATE_POTENTIAL_TEMPLATE;
        }
        potentialService.createPotential(potential, currentUser);
        redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING,CommonConst.SHOW_ALERT_CREATE_CUSTOMER_OK);
        redirectAttributes.addFlashAttribute(CommonConst.USER_STRING, currentUser);
        return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_CREATE;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Model model, RedirectAttributes redirectAttributes,
                       @Nullable @ModelAttribute("potential") Potential potential,
                       Principal principal) {
        boolean error = false;
        User user = userService.getUserByUsername(principal.getName());

        if (ValidateUtil.isNullOrEmpty(potential)){
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        if (potentialService.isPhoneExisted(potential.getPhoneNumber(), potential.getId())) {
            model.addAttribute(CommonConst.DUPLICATE_PHONE_STRING, CommonConst.DUPLICATE_PHONE_MESSAGE);
            error = true;
        }
        if (potentialService.isEmailExisted(potential.getEmail(), potential.getId())) {
            model.addAttribute(CommonConst.DUPLICATE_EMAIL_STRING, CommonConst.DUPLICATE_EMAIL_MESSAGE);
            error = true;
        }
        if (error){
            List<Source> sources = sourceRepository.getAll();
            model.addAttribute(CommonConst.SOURCE_STRING, sources);
            model.addAttribute(CommonConst.USER_STRING, user);
            model.addAttribute(CommonConst.FLAG_STRING, CommonConst.SHOW_ALERT_UPDATE_CUSTOMER_ERROR);
            return CommonConst.UPDATE_POTENTIAL_TEMPLATE;
        }
        boolean done = potentialService.editPotential(potential, user);
        if (!done){
            redirectAttributes.addFlashAttribute(CommonConst.MESSAGE_STRING, CommonConst.NOT_FOUND_POTENTIAL_MESSAGE);
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING,CommonConst.SHOW_ALERT_UPDATE_CUSTOMER_OK);
        return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_DETAIL + potential.getId();
    }

    @RequestMapping(value = "/detail/edit/level/MKT/{id}", method = RequestMethod.POST)
    public String editLevel(@Nullable @RequestParam("level") int levelId,
                            @Nullable @PathVariable("id") Long pid,
                            RedirectAttributes redirectAttributes, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        potentialService.editLevelPotential(pid, levelId, currentUser);
        redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING,CommonConst.SHOW_ALERT_UPDATE_LEVEL_CUSTOMER_OK);
        return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_DETAIL_TAKECARE_MKT + pid;
    }

    @RequestMapping(value = "/remove",  method = RequestMethod.POST)
    public String remove(Model model, RedirectAttributes redirectAttributes,
                         @Nullable @RequestParam("potential-id") List<Long> idList,
                         Principal principal) {
        if (idList == null){
            redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING,CommonConst.SHOW_ALERT_ERROR);
            return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        potentialService.removePotentials(idList, currentUser);
        redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING,CommonConst.SHOW_ALERT);
        return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
    }

    @PostMapping(value = "/importOldDataCustomer")
    public String importOldDataExcelFile(RedirectAttributes redirectAttributes,
                                         @RequestParam("upload") MultipartFile file,
                                         Principal principal) {
        try {
            if (ValidateUtil.isNullOrEmpty(file)) {
                redirectAttributes.addFlashAttribute(CommonConst.ERROR_STRING, CommonConst.NULL_FILE_MESSAGE);
                return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
            }
            ExcelReader excelReader = new ExcelReader();
            List<CustomerDTO> customerData = excelReader.getCustomerData(file.getInputStream(), file.getOriginalFilename());
            User currentUser = userService.getUserByUsername(principal.getName());
            customerService.importCustomer(customerData, currentUser);
            redirectAttributes.addFlashAttribute(CommonConst.FLAG_STRING, CommonConst.SHOW_ALERT_IMPORT_OK);
            redirectAttributes.addFlashAttribute(CommonConst.MESSAGE_STRING, CommonConst.IMPORT_SUCCESS_MESSAGE);
        } catch (Exception e) {
            LOGGER.error("Has exception when importOldDataExcelFile ", e);
        }
        return CommonConst.REDIRECT + CommonConst.PATH_POTENTIAL_HOME;
    }

    @PostMapping(value = {"/detail/take-care/MKT/{id}"})
    public String markTakeCareAsSeen(@PathVariable("id") Long id, Principal principal,
                                     @RequestParam("index") Integer index){
        Potential potential = potentialService.getPotentialById(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = potentialService.acceptTakeCareInfo(potential, currentUser, index);
        return "redirect:/potential/detail/takecare/MKT/" + id;
    }

    @PostMapping(value = {"/detail/take-care/{id}"})
    public String takeCare(@PathVariable("id") Long pid,
                           Principal principal,
                           @Nullable @RequestParam("description") String description,
                           @RequestParam("index") Integer index,
                           RedirectAttributes redirectAttributes){
        Potential potential = potentialService.getPotentialById(pid);
        if (potential == null){
            return "redirect:/potential/home";
        }
        if (index > 3){
            return "redirect:/potential/detail/takecare/" + pid;
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = potentialService.addTakeCarePotentialDetail(potential, currentUser, description);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/potential/detail/takecare/" + pid;
    }

    @PostMapping(value = "/getPotentialToShare")
    public ResponseEntity<CustomerDistributionForm> getPotentialToShare(@RequestBody String ids) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> listSelectedId = mapper.readValue(ids,
                mapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        List<PotentialSearchForm> potentialsSharing = potentialService.getListPotentialToShare(listSelectedId);
        CustomerDistributionForm customerDistributionForm = new CustomerDistributionForm();
        customerDistributionForm.setPotentialSearchFormList(potentialsSharing);
        return new ResponseEntity<>(customerDistributionForm, HttpStatus.OK);
    }

    @PostMapping(value = "/getEmployeeByDepartmentId")
    public ResponseEntity<List<EmployeeSearchForm>> getEmployeeByDepartmentId(@RequestBody String id) throws JsonProcessingException {
        List<EmployeeSearchForm> employeeList = null;
        try {
            employeeList = potentialService.getEmployeeByDepartmentId(Long.parseLong(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    //   SALE
    @RequestMapping(value = "/detail/sale/{id}", method = RequestMethod.GET)
    public String goDetailInformationCustomerSale(Model model, @PathVariable("id") Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potentialDetail = potentialService.getPotentialById(id);
        Potential potentialEntity = new Potential();
        if (potentialDetail == null) {
            return "redirect:/potential/home";
        }
        model.addAttribute("potentialDetail", potentialDetail);
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potentialDetail.getLevel());
        model.addAttribute("potentialEntity", potentialEntity);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return "/potential/sale/detail-potential-information-salesman";
    }

    @RequestMapping(value = "/detail/diary/sale/{id}", method = RequestMethod.GET)
    public String goDetailDiarySalesman(Model model, @PathVariable("id") Long potentialId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potential = potentialService.getPotentialById(potentialId);
        if (potential == null) {
            return "redirect:/potential/home";
        }
        TreeMap<Diary, Employee> diaries = new TreeMap<>(diaryService.getDiaryMap(potentialId));
        model.addAttribute("diaries", diaries);
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potential.getLevel());
        model.addAttribute("potentialDetail", potential);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return "/potential/sale/detail-potential-diary-salesman";
    }

    @RequestMapping(value = "/detail/takecare/{id}", method = RequestMethod.GET)
    public String goDetailTakeCarePotential(Model model, @PathVariable("id") Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Potential potential = potentialService.getPotentialById(id);
        if (potential == null){
            return "redirect:/potential/home";
        }
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potential.getLevel());
        model.addAttribute("potentialDetail", potential);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return "/potential/sale/detail-potential-takecare-page";
    }
    //END SALE

    @RequestMapping(value = "/detail/sendEmail", method = RequestMethod.POST)
    public String sendEmail(RedirectAttributes redirectAttributes,
                            @Nullable @RequestParam("id") String id,
                            @Nullable @RequestParam("emailForCustomer") String email,
                            @Nullable @RequestParam("description") String description,
                            @Nullable @RequestParam("subject") String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(description);
        emailSender.send(message);
        redirectAttributes.addFlashAttribute("flag", "showAlertSendEmail");
        return "redirect:/potential/detail/" + id;
    }

    @RequestMapping(value = "/detail/salesman/sendEmail", method = RequestMethod.POST)
    public String sendEmailForSale(RedirectAttributes redirectAttributes,
                            @Nullable @RequestParam("id") String id,
                            @Nullable @RequestParam("emailForCustomer") String email,
                            @Nullable @RequestParam("description") String description,
                            @Nullable @RequestParam("subject") String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(description);
        emailSender.send(message);
        redirectAttributes.addFlashAttribute("flag", "showAlertSendEmail");
        return "redirect:/potential/detail/sale/" + id;
    }

    @GetMapping("/openTradingAccount")
    public String goOpenTradingAccount(Model model,
                                       @Nullable @RequestParam("potentialId") Long potentialId,
                                       Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        CustomerForm customerForm = customerService.initForm(potentialId);
        model.addAttribute(AppConst.CUSTOMER_FORM, customerForm);
        model.addAttribute("userInfo", user);
        model.addAttribute("userName",principal.getName());
        return CommonConst.OPEN_TRADING_ACCOUNT_TEMPALTE;
    }

}
