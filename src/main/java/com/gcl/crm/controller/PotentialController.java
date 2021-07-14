package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.form.PotentialSearchForm;
import com.gcl.crm.form.CustomerDistributionForm;
import com.gcl.crm.repository.SourceRepository;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.service.EmployeeService;
import com.gcl.crm.service.LevelService;
import com.gcl.crm.service.PotentialService;
import com.gcl.crm.service.UserService;
import com.gcl.crm.utils.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/potential")
public class PotentialController {
    private static final String DASHBOARD_PAGE = "/potential/home-potential-hungNT-V2";
    private static final String CREATE_PAGE = "/potential/create-potential-hungNT-V2";
    private static final String UPDATE_PAGE = "/potential/edit-potential-hungNT-V2";
    private static final String ERROR_400 = "/error/error-400";
    private static final String DETAIL_OVERVIEW_PAGE = "/potential/details/detail-potential-overview-page-V2";
    private static final String DETAIL_INFORMATION_PAGE = "/potential/details/detail-potential-information-page-V2";
    private static final String DETAIL_ACTION_PAGE = "/potential/details/detail-potential-action-page-V2";
    private static final String DETAIL_TAKECARE_PAGE = "/potential/details/detail-potential-takecare-page";
    private static final String DETAIL_TAKECARE_MKTPAGE = "/potential/details/marketing/detail-potential-takecare-MKTpage";
    private static final String ERROR_USER = "loginPage";

    @Autowired
    PotentialService potentialService;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    LevelService levelService;

    @Autowired
    UserService userService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHomePage(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_USER;
        }
        List<Source> sources = sourceRepository.getAll();
        List<Level> levels = levelService.getAll();
        List<Potential> potentials = potentialService.getAllPotentials();
        List<Department> departments = departmentService.findAllDepartments();
        List<Employee> employees = employeeService.getAllWorkingEmployees();
        List<Potential> potentialsSharing = potentialService.getListPotentialToShare();
        PotentialSearchForm searchForm = new PotentialSearchForm();
        model.addAttribute("departments", departments);
        CustomerDistributionForm customerDistributionForm = new CustomerDistributionForm();
        model.addAttribute("sources", sources);
        model.addAttribute("levels", levels);
        model.addAttribute("potentials", potentials);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("employees", employees);
        model.addAttribute("potentialsSharing", potentialsSharing);
        model.addAttribute("customerDistributionForm", customerDistributionForm);
        return DASHBOARD_PAGE;
    }

    @RequestMapping(value = "/detail/overview/{id}", method = RequestMethod.GET)
    public String goPotentialDetail(Model model, @PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return ERROR_400;
        }
        Potential potential = potentialService.getPotentialById(id);
        if (potential == null) {
            return "redirect:/potential/home";
        }
        model.addAttribute("potentialDetail", potential);
        return DETAIL_OVERVIEW_PAGE;
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String goDetailInformationCustomer(Model model, @PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return ERROR_USER;
        }
        Potential potentialDetail = potentialService.getPotentialById(id);
        Potential potentialEntity = new Potential();
        if (potentialDetail == null) {
            return "redirect:/potential/home";
        }
        model.addAttribute("potentialDetail", potentialDetail);
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potentialDetail.getLevel());
        model.addAttribute("potentialEntity", potentialEntity);
        return DETAIL_INFORMATION_PAGE;
    }

    @RequestMapping(value = "/detail/takecare/{id}", method = RequestMethod.GET)
    public String goDetailTakeCarePotential(Model model, @PathVariable("id") Long id, Principal principal) {
        Potential potential = potentialService.getPotentialById(id);
        if (potential == null){
            return "redirect:/potential/home";
        }
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potential.getLevel());
        model.addAttribute("potentialDetail", potential);
        return DETAIL_TAKECARE_PAGE;
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
        Long uid = currentUser == null ? null : currentUser.getEmployee().getId();
        boolean done = potentialService.addTakeCarePotentialDetail(potential, currentUser, description);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/potential/detail/takecare/" + pid;
    }

    @RequestMapping(value = "/detail/takecare/MKT/{id}", method = RequestMethod.GET)
    public String goDetailTakeCarePotentialOfMKT(Model model, @PathVariable("id") Long id, Principal principal) {
        Potential potentialDetail = potentialService.getPotentialById(id);
        if (potentialDetail == null) {
            return "redirect:/potential/home";
        }
        model.addAttribute("levels", levelService.getAll());
        model.addAttribute("selectedLevel", potentialDetail.getLevel());
        model.addAttribute("potentialDetail", potentialDetail);
        return DETAIL_TAKECARE_MKTPAGE;
    }

    @PostMapping(value = {"/detail/take-care/MKT/{id}"})
    public String markTakeCareAsSeen(@PathVariable("id") Long id, Principal principal,
                                     @RequestParam("index") Integer index){
        Potential potential = potentialService.getPotentialById(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = potentialService.acceptTakeCareInfo(potential, currentUser, index);
        return "redirect:/potential/detail/takecare/MKT/" + id;
    }

    @RequestMapping(value = "/email/id1", method = RequestMethod.GET)
    public String goDetailEmailCustomer(Model model) {
        return "detail-potential-email-page-V2";
    }

    @RequestMapping(value = "/action/id1", method = RequestMethod.GET)
    public String goDetailActionCustomer(Model model) {
        return DETAIL_ACTION_PAGE;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @Nullable @ModelAttribute("searchForm") PotentialSearchForm searchForm, RedirectAttributes redirectAttributes){
        if (searchForm == null){
            return "redirect:/potential/home";
        }
        CustomerDistributionForm customerDistributionForm = new CustomerDistributionForm();
        List<Source> sources = sourceRepository.getAll();
        List<Level> levels = levelService.getAll();
        List<Potential> potentials = potentialService.search(searchForm);
        model.addAttribute("sources", sources);
        model.addAttribute("levels", levels);
        model.addAttribute("potentials", potentials);
        model.addAttribute("customerDistributionForm", customerDistributionForm);
        return DASHBOARD_PAGE;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String goCreatePage(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_400;
        }
        List<Source> sources = sourceRepository.getAll();
        Potential potential = new Potential();
        model.addAttribute("potentialForm", potential);
        model.addAttribute("sources", sources);
        return CREATE_PAGE;
    }

    @RequestMapping(value = "/detail/edit/{id}", method = RequestMethod.GET)
    public String goEditPage(Model model, Principal principal, @Nullable @PathVariable("id") Long id) {
        if (principal == null) {
            return ERROR_400;
        }
        if (id == null){
            return "redirect:/potential/home";
        }
        Potential potential = potentialService.getPotentialById(id);
        if (potential == null){
            return "redirect:/potential/home";
        }
        if (potential.getSource() == null){
            potential.setSource(new Source());
        }
        List<Source> sources = sourceRepository.getAll();
        model.addAttribute("potential", potential);
        model.addAttribute("sources", sources);
        return UPDATE_PAGE;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("potentialForm") Potential potential, RedirectAttributes redirectAttributes, Principal principal) {
        boolean error = false;
        if (potentialService.isPhoneExisted(potential.getPhoneNumber(), potential.getId())){
            model.addAttribute("duplicatePhone", "Số điện thoại này đã tồn tại");
            error = true;
        }
        if (potentialService.isEmailExisted(potential.getEmail(), potential.getId())){
            model.addAttribute("duplicateEmail", "Email này đã tồn tại");
            error = true;
        }
        if (error){
            List<Source> sources = sourceRepository.getAll();
            model.addAttribute("sources", sources);
            return CREATE_PAGE;
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = potentialService.createPotential(potential, currentUser);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/potential/create";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Model model, RedirectAttributes redirectAttributes,
                       @Nullable @ModelAttribute("potential") Potential potential,
                       Principal principal
    ) {
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
        User user = userService.getUserByUsername(loginedUser.getUsername());
        boolean error = false;

        if (potential == null){
            return "redirect:/potential/home";
        }
        if (potentialService.isPhoneExisted(potential.getPhoneNumber(), potential.getId())){
            model.addAttribute("duplicatePhone", "Số điện thoại này đã tồn tại");
            error = true;
        }
        if (potentialService.isEmailExisted(potential.getEmail(), potential.getId())){
            model.addAttribute("duplicateEmail", "Email này đã tồn tại");
            error = true;
        }
        if (error){
            List<Source> sources = sourceRepository.getAll();
            model.addAttribute("sources", sources);
            return UPDATE_PAGE;
        }
        boolean done = potentialService.editPotential(potential, user.getUserId());
        if (!done){
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy đầu mối");
            return "redirect:/potential/home";
        }
        redirectAttributes.addFlashAttribute("flag","showAlertUpdateSuccessful");
        return "redirect:/potential/detail/edit/" + potential.getId();
    }

    @RequestMapping(value = "/detail/edit/level/MKT/{id}", method = RequestMethod.POST)
    public String editLevel(@Nullable @RequestParam("level") int levelId,
                            @Nullable @PathVariable("id") Long pid,
                            RedirectAttributes redirectAttributes
    ) {
        boolean done = potentialService.editLevelPotential(pid, levelId);
        redirectAttributes.addFlashAttribute("flag","showAlertUpdateLevelSuccessful");
        return "redirect:/potential/detail/takecare/MKT/" + pid;
    }

    @RequestMapping(value = "/remove",  method = RequestMethod.POST)
    public String remove(Model model, RedirectAttributes redirectAttributes, @Nullable @RequestParam("pid") List<Long> idList) {
        if (idList == null){
            redirectAttributes.addFlashAttribute("flag","showAlertError");
            return "redirect:/potential/home";
        }
        potentialService.removePotentials(idList);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/potential/home";
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importExcelFile(Model model, @RequestParam("upload") MultipartFile file, Principal principal){
        ExcelReader excelReader = new ExcelReader();
        try {
            List<Potential> potentialData = excelReader.getPotentialData(file.getInputStream(), file.getOriginalFilename());
            org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
            User currentUser = userService.getUserByUsername(loginedUser.getUsername());
            potentialService.importPotential(potentialData, currentUser);
            model.addAttribute("message", "Dữ liệu mới đã được lưu vào hệ thống");
        } catch (IOException e) {
            model.addAttribute("error", "Không thể mở tệp đã chọn");
        } catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/potential/home";
    }

    @PostMapping(value = "/getPotentialToShare")
    public ResponseEntity<List<Potential>> getPotentialToShare(@RequestBody String ids) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Long> listSelectedId = mapper.readValue(ids,
                mapper.getTypeFactory().constructCollectionType(List.class, Long.class));

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private String getCurrentDate() {
        String newYorkDateTimePattern = "dd/MM/yyyy";
        DateTimeFormatter newYorkDateFormatter = DateTimeFormatter.ofPattern(newYorkDateTimePattern);
        String formattedDate = newYorkDateFormatter.format(LocalDate.now());
        return formattedDate;
    }

}
