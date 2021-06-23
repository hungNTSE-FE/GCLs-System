package com.gcl.crm.service;

import com.gcl.crm.entity.Potential;
import com.gcl.crm.repository.PotentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotentialService {

    @Autowired
    PotentialRepository potentialRepository;

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
            potentialRepository.save(potential);
        }
        return true;
    }
}
