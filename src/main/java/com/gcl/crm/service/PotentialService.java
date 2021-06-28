package com.gcl.crm.service;

import com.gcl.crm.entity.Level;
import com.gcl.crm.entity.Potential;
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
            potential.setAvailable(true);
            potentialRepository.save(potential);
        }
        return true;
    }

    public boolean createPotential(Potential potential) {
        Potential poten = potentialRepository.save(potential);
        return poten != null;
    }

    public boolean resetPotential(Long id) {
        Potential potential = potentialRepository.findPotentialById(id);
        if (potential == null) {
            return false;
        }
        potential.setAvailable(true);
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
            potential = potentialRepository.findPotentialById(checkedPotential.get(i));
            potential.setAvailable(true);
            potential = potentialRepository.save(potential);
        }
        return potential != null;
    }

    public List<Potential> getAllPotentials(){
        return potentialRepository.findAllByAvailable(true);
    }
    public List<Potential> getAllDeletedPotentials(){
        return potentialRepository.findAllByAvailable(false);
    }

    public List<Potential> search(PotentialSearchForm searchForm){
        Level level = null;
        if (searchForm.getLevel() != null){
            level = levelService.getLevelById(searchForm.getLevel());
        }
        List<Potential> potentials = potentialRepository
                .findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndSourceContainingAndLevel
                        (searchForm.getName(), searchForm.getPhone(), searchForm.getEmail(), searchForm.getSource(), level);
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
