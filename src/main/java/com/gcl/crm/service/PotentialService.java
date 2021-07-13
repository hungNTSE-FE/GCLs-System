package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.PotentialSearchForm;
import com.gcl.crm.repository.CareRepository;
import com.gcl.crm.repository.LevelRepository;
import com.gcl.crm.repository.PotentialRepository;
import com.gcl.crm.repository.PotentialRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PotentialService {

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

    public boolean importPotential(List<Potential> potentials){
        if (potentials.size() == 0){
            return false;
        }
        for (Potential potential : potentials){
            Potential savedData1 = potentialRepository.findPotentialByPhoneNumberAndIdNot(potential.getPhoneNumber(), null);
            Potential savedData2 = potentialRepository.findPotentialByEmailAndIdNot(potential.getEmail(), null);
            if (savedData1 != null || savedData2 != null){
                continue;
            }
            String sourceName = potential.getSourceName();
            Source source = sourceService.getSourceByName(sourceName);
            potential.setSource(source);
            potential.setStatus(Status.ACTIVE);
            potentialRepository.save(potential);
        }
        return true;
    }

    public boolean editPotential(Potential newPotential, Long id){
        Potential potential = potentialRepository.findPotentialByIdAndStatus(newPotential.getId(), Status.ACTIVE);
        if (potential == null){
            return false;
        }
        potential.setName(newPotential.getName());
        potential.setAddress(newPotential.getAddress());
        potential.setEmail(newPotential.getEmail());
        potential.setPhoneNumber(newPotential.getPhoneNumber());
        potential.setSource(sourceService.getSourceById(newPotential.getSource().getSourceId()));
        potential.setLastModified(getCurrentDate());
        potential.setLastModifier(id);
        potentialRepository.save(potential);
        return true;
    }

    public boolean editLevelPotential(Long id, int levelId) {
        Potential potential = potentialRepository.findPotentialByIdAndStatus(id, Status.ACTIVE);
        if (potential == null) {
            return false;
        }
        Level level = levelRepository.findByLevelId(levelId);
        potential.setLevel(level);
        potentialRepository.save(potential);
        return true;
    }

    public Potential getPotentialById(Long id){
        return potentialRepository.findPotentialByIdAndStatus(id, Status.ACTIVE);
    }

    public void removePotentials(List<Long> idList){
        for (Long id : idList){
            Potential potential = potentialRepository.findPotentialByIdAndStatus(id, Status.ACTIVE);
            potential.setStatus(Status.INACTIVE);
            potentialRepository.save(potential);
        }
    }

    public boolean createPotential(Potential potential, User user) {
        String sourceName = potential.getSourceName();
        Source source = sourceService.getSourceByName(sourceName);
        potential.setSource(source);
        potential.setStatus(Status.ACTIVE);
        potential.setMaker(user.getUserId());
        potential.setDate(getStringCurrentDate());
        Potential poten = potentialRepository.save(potential);
        return poten != null;
    }

    public boolean resetPotential(Long id) {
        Potential potential = potentialRepository.findPotentialByIdAndStatus(id, Status.INACTIVE);
        if (potential == null) {
            return false;
        }
        potential.setStatus(Status.ACTIVE);
        Potential potential1 = potentialRepository.save(potential);
        return potential1 != null;
    }

    public List<Potential> getPotentialsByIdList(List<Long> aidList) {
        if (aidList == null) {
            return null;
        }
        List<Potential> potentials = new ArrayList<>();
        for (int i=0; i < aidList.size(); i++) {
            Optional<Potential> potential = potentialRepository.findById(aidList.get(i));
            if (potential.isPresent()) {
                potentials.add(potential.get());
            }
        }
        return potentials;
    }

    public boolean resetAllPotential(Potential potential, List<Long> checkedPotential) {

        for (int i = 0; i < checkedPotential.size(); i++) {
            potential = potentialRepository.findPotentialByIdAndStatus(checkedPotential.get(i), Status.INACTIVE);
            potential.setStatus(Status.ACTIVE);
            potential = potentialRepository.save(potential);
        }
        return potential != null;
    }

    public List<Potential> getAllPotentials(){
        return potentialRepository.findAllByStatus(Status.ACTIVE);
    }

    public List<Potential> getAllDeletedPotentials(){
        return potentialRepository.findAllByStatus(Status.INACTIVE);
    }

    public List<Potential> search(PotentialSearchForm searchForm){
        Level level = null;
        if (searchForm.getLevel() != null){
            level = levelService.getLevelById(searchForm.getLevel());
        }
        Source source = sourceService.getSourceByName(searchForm.getSource());
        List<Potential> potentials = potentialRepository
                .findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndStatus
                        (searchForm.getName(), searchForm.getPhone(), searchForm.getEmail(), Status.ACTIVE);
        String[] dateRange = searchForm.getTime().split("-");
        Date date1 = new Date(Date.parse(dateRange[0].trim()));
        Date date2 = new Date(Date.parse(dateRange[1].trim()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Potential> result = new ArrayList<>();
        for (int i = 0; i < potentials.size(); i++) {
            Potential potential = potentials.get(i);
            boolean flag = true;
            Date date = null;
            try {
                date = simpleDateFormat.parse(potential.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((date.before(date1) || date.after(date2))){
                flag = false;
            }
            if (level != null){
                if (!level.equals(potential.getLevel())){
                    flag = false;
                }
            }
            if (source != null){
                if (!source.equals(potential.getSource())){
                    flag = false;
                }
            }
            if (flag){
                result.add(potential);
            }
        }
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

    public List<Potential> getListPotentialToShare() {
        return potentialRepository2.getListPotentialToShare();
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
