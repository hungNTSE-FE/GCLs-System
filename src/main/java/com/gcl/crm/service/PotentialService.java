package com.gcl.crm.service;

import com.gcl.crm.entity.Level;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.Source;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.PotentialSearchForm;
import com.gcl.crm.repository.PotentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PotentialService {

    @Autowired
    PotentialRepository potentialRepository;

    @Autowired
    LevelService levelService;

    @Autowired
    SourceService sourceService;

    public boolean importPotential(List<Potential> potentials){
        if (potentials.size() == 0){
            return false;
        }
        for (Potential potential : potentials){
            Potential savedData1 = potentialRepository.findPotentialByPhoneNumber(potential.getPhoneNumber());
            Potential savedData2 = potentialRepository.findPotentialByEmail(potential.getEmail());
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

    public boolean editPotential(Potential newPotential){
        Potential potential = potentialRepository.findPotentialByIdAndStatus(newPotential.getId(), Status.ACTIVE);
        if (potential == null){
            return false;
        }
        potential.setName(newPotential.getName());
        potential.setAddress(newPotential.getAddress());
        potential.setEmail(newPotential.getEmail());
        potential.setPhoneNumber(newPotential.getPhoneNumber());
        potential.setSource(sourceService.getSourceById(newPotential.getId()));
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

    public boolean createPotential(Potential potential) {
        String sourceName = potential.getSourceName();
        Source source = sourceService.getSourceByName(sourceName);
        potential.setSource(source);
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
                .findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndSourceAndLevelAndStatus
                        (searchForm.getName(), searchForm.getPhone(), searchForm.getEmail(), source, level, Status.ACTIVE);
        String[] dateRange = searchForm.getTime().split("-");
        Date date1 = new Date(Date.parse(dateRange[0].trim()));
        Date date2 = new Date(Date.parse(dateRange[1].trim()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Potential> result = new ArrayList<>();
        for (int i = 0; i < potentials.size(); i++) {
            Potential potential = potentials.get(i);
            Date date = null;
            try {
                date = simpleDateFormat.parse(potential.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!(date.before(date1) || date.after(date2))){
                result.add(potential);
            }
        }
        return result;
    }

    public boolean isPhoneExisted(String phone){
        Potential potential = potentialRepository.findPotentialByPhoneNumber(phone);
        return potential != null;
    }

    public boolean isEmailExisted(String email){
        Potential potential = potentialRepository.findPotentialByEmail(email);
        return potential != null;
    }
}
