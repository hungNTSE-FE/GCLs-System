package com.gcl.crm.service;

import com.gcl.crm.config.AppConst;
import com.gcl.crm.entity.*;
import com.gcl.crm.enums.LevelEnum;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.EmployeeSearchForm;
import com.gcl.crm.form.PotentialSearchForm;
import com.gcl.crm.repository.*;
import com.gcl.crm.utils.DateTimeUtil;
import com.gcl.crm.utils.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PotentialService {

    @Autowired
    DiaryService diaryService;

    @Autowired
    PotentialRepository potentialRepository;

    @Autowired
    PotentialRepository2 potentialRepository2;

    @Autowired
    LevelService levelService;

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    CareRepository careRepository;

    @Autowired
    SourceService sourceService;

    @Autowired
    EmployeesRepository employeesRepository;

    @Autowired
    UserService userService;

    public boolean editPotential(Potential newPotential, User currentUser){
        Potential potential = potentialRepository.findPotentialByIdAndStatus(newPotential.getId(), true);
        if (potential == null){
            return false;
        }
        potential.setName(newPotential.getName());
        potential.setAddress(newPotential.getAddress());
        potential.setEmail(newPotential.getEmail());
        potential.setPhoneNumber(newPotential.getPhoneNumber());
        potential.setSource(sourceService.getSourceById(newPotential.getSource().getSourceId()));
        potential.setLastModified(getCurrentDate());
        potential.setLastModifier(currentUser.getEmployee().getId());
        potential = potentialRepository.save(potential);
        diaryService.createDiary("Thông tin đầu mối được cập nhật", currentUser, potential);
        return true;
    }

    public boolean editLevelPotential(Long id, int levelId, User currentUser) {
        Potential potential = potentialRepository.findPotentialByIdAndStatus(id, true);
        if (potential == null) {
            return false;
        }
        Level level = levelRepository.findByLevelId(levelId);
        potential.setLevel(level);
        potential.setLastModifier(currentUser.getEmployee().getId());
        potential.setLastModified(this.getCurrentDate());
        potential = potentialRepository.save(potential);
        diaryService.createDiary("Cập nhật cấp độ", currentUser, potential);
        return true;
    }

    public Potential getPotentialById(Long id){
        return potentialRepository.findPotentialByIdAndStatus(id, true);
    }

    public void removePotentials(List<Long> idList, User currentUser){
        for (Long id : idList){
            Potential potential = potentialRepository.findPotentialByIdAndStatus(id, true);
            potential.setStatus(false);
            potential.setLastModified(this.getCurrentDate());
            potential.setLastModifier(currentUser.getEmployee().getId());
            potential = potentialRepository.save(potential);
            diaryService.createDiary("Bị xóa và được chuyển tới thùng rác", currentUser, potential);
        }
    }

    public boolean createPotential(Potential potential, User user) {
        String sourceName = potential.getSourceName();
        Source source = sourceService.getSourceByName(sourceName);
        potential.setSource(source);
        potential.setStatus(true);
        potential.setMaker(user.getUserId());
        potential.setDate(getStringCurrentDate());
        Potential poten = potentialRepository.save(potential);
        diaryService.createDiary("Đầu mối được tạo", user, potential);
        return poten != null;
    }

    public boolean resetPotential(Long id,User currentUser) {
        Potential potential = potentialRepository.findPotentialByIdAndStatus(id, false);
        if (potential == null) {
            return false;
        }
        potential.setStatus(true);
        Potential potential1 = potentialRepository.save(potential);
        diaryService.createDiary("Đầu mối được phục hồi từ thùng rác", currentUser, potential);
        return potential1 != null;
    }

    public boolean resetAllPotential(List<Long> checkedPotential, User currentUser) {
        for (int i = 0; i < checkedPotential.size(); i++) {
            Potential potential = potentialRepository.findPotentialByIdAndStatus(checkedPotential.get(i), false);
            potential.setStatus(true);
            potential.setLastModified(this.getCurrentDate());
            potential.setLastModifier(currentUser.getEmployee().getId());
            potential = potentialRepository.save(potential);
            diaryService.createDiary("Đầu mối được phục hồi từ thùng rác", currentUser, potential);
        }
        return true;
    }

    public List<Potential> getAllPotentials(){
        List<Potential> potentials = potentialRepository.findAllByStatus(true);
        Collections.sort(potentials);
        return potentials;
    }

    public List<Potential> getAllDeletedPotentials(){
        List<Potential> potentials = potentialRepository.findAllByStatus(false);
        Collections.sort(potentials);
        return potentials;
    }

    public List<Potential> search(PotentialSearchForm searchForm) throws ParseException{
        Level level = null;
        Source source = null;
        if (ValidateUtil.isNotNullOrEmpty(searchForm.getLevel())){
            level = levelService.getLevelById(searchForm.getLevel());
        }
        if (ValidateUtil.isNotNullOrEmpty(searchForm.getSource())) {
            source = sourceService.getSourceByName(searchForm.getSource());
        }

        List<Potential> potentials = potentialRepository
                .findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndStatus(searchForm.getName().trim(),
                        searchForm.getPhone().trim(),
                        searchForm.getEmail().trim(),
                        true);
        List<Potential> result = new ArrayList<>();
        for (int i = 0; i < potentials.size(); i++) {
            Potential potential = potentials.get(i);
            boolean flag = true;
            Date date1 = null;
            Date date2 = null;
            if (ValidateUtil.isNotNullOrEmpty(searchForm.getTime())) {
                String[] dateRange = searchForm.getTime().split("-");
                date1 = DateTimeUtil.convertStringToDate(dateRange[0].trim(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES);
                date2 = DateTimeUtil.convertStringToDate(dateRange[1].trim(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES);
                Date date = DateTimeUtil.convertStringToDate(potential.getDate(), AppConst.FORMAT_DD_MM_YYYY_CROOSSIES);
                if ((date.before(date1) || date.after(date2))){
                    flag = false;
                }
            }
            if (ValidateUtil.isNotNullOrEmpty(source)) {
                if (!source.equals(potential.getSource())) {
                    flag = false;
                }
            }
            if (ValidateUtil.isNotNullOrEmpty(level)){
                if (!level.equals(potential.getLevel())){
                    flag = false;
                }
            }
            if (flag){
                result.add(potential);
            }
        }
        Collections.sort(result);
        return result;
    }

    public boolean isPhoneExisted(String phone, Long id){
        Potential potential = (id != null) ? potentialRepository.findPotentialByPhoneNumberAndIdNot(phone, id)
                : potentialRepository.findPotentialByPhoneNumber(phone);
        return potential != null;
    }

    public boolean isEmailExisted(String email, Long id){
        Potential potential = (id != null) ? potentialRepository.findPotentialByEmailAndIdNot(email, id)
                : potentialRepository.findPotentialByEmail(email);
        return potential != null;
    }

    public List<PotentialSearchForm> getListPotentialToShare(List<Long> listSelectedId) {
        return potentialRepository2.getListPotentialToShare(listSelectedId);
    }

    public boolean addTakeCarePotentialDetail(Potential potential, User user, String description){
        Care care = new Care();
        care.setDescription(description);
        care.setPotential(potential);
        care.setLastModified(this.getCurrentDate());
        care.setLastModifier(user.getUserId());
        care.setModifierName(user.getEmployee().getName());
        care.setAccepted(false);
        Care confirm = careRepository.save(care);
        diaryService.createDiary("Cập nhật chăm sóc lần " + potential.getCares().size(), user, potential);
        return confirm.equals(care);
    }

    public boolean acceptTakeCareInfo(Potential potential, User user, Integer index){
        List<Care> cares = potential.getCares();
        if (cares.size() <= index){
            return false;
        }
        Care care = cares.get(index);
        if (care.isAccepted()){
            return false;
        }
        care.setAccepted(true);
        care.setAcceptDate(this.getCurrentDate());
        care.setAcceptor(user.getEmployee().getId());
        Care confirm = careRepository.save(care);
        return care.equals(confirm);
    }

    public List<EmployeeSearchForm> getEmployeeByDepartmentId(Long id) {
        return employeesRepository.getListEmployeeByDepartmentId(id);
    }

    public String getDepartmentByUserName(User user) {
        String depName = user.getEmployee().getDepartment().getName();
        if (depName.toUpperCase().contains("SALE")) {
            return "SALE";
        }
        if (depName.toUpperCase().contains("MARKETING")) {
            return "MARKETING";
        }
        return null;
    }

    public List<Potential> getAllPotentialsOfSale(User user){
        return potentialRepository2.getListPotentialOfSale(user.getEmployee().getMarketingGroup().getId());
    }

    public void updateLevelPotentialAfterRegistCus(Long potentialId) {
        potentialRepository2.updateLevelPotential(potentialId, LevelEnum.LEVEL_6.getValue());
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    private String getStringCurrentDate() {
        String newYorkDateTimePattern = "dd/MM/yyyy";
        DateTimeFormatter newYorkDateFormatter = DateTimeFormatter.ofPattern(newYorkDateTimePattern);
        String formattedDate = newYorkDateFormatter.format(LocalDate.now());
        return formattedDate;
    }
}
