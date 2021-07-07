package com.gcl.crm.service;

import com.gcl.crm.entity.Potential;
import com.gcl.crm.form.RecentPotentialForm;
import com.gcl.crm.repository.PotentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticService {

    @Autowired
    PotentialRepository potentialRepository;

    public List<Integer> getPotentialVolatility(String year){
        Integer[] result = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        List<Potential> potentials = potentialRepository.findAllByDateContaining("/" + year);
        for (Potential potential : potentials){
            String[] date = potential.getDate().split("/");
            switch (date[1]){
                case "01":
                    result[0]++;
                    break;
                case "02":
                    result[1]++;
                    break;
                case "03":
                    result[2]++;
                    break;
                case "04":
                    result[3]++;
                    break;
                case "05":
                    result[4]++;
                    break;
                case "06":
                    result[5]++;
                    break;
                case "07":
                    result[6]++;
                    break;
                case "08":
                    result[7]++;
                    break;
                case "09":
                    result[8]++;
                    break;
                case "10":
                    result[9]++;
                    break;
                case "11":
                    result[10]++;
                    break;
                case "12":
                    result[11]++;
                      break;
                default:
                    break;
            }
        }
        return Arrays.asList(result);
    }

    public List<RecentPotentialForm> getTodayPotential(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date(System.currentTimeMillis());
        List<Potential> potentials = potentialRepository.findAllByDate(simpleDateFormat.format(now));
        List<RecentPotentialForm> result = new ArrayList<>();
        for (Potential potential : potentials){
            result.add(new RecentPotentialForm(potential.getName(), null, potential.getEmail()));
        }
        return result;
    }

    public List<Integer> countPotentialByDateGroupByLevel(String date){
        Integer[] result = {0, 0, 0, 0, 0, 0, 0, 0};
        List<Potential> potentials = potentialRepository.findAllByDateContaining(date);
        for (Potential potential : potentials){
            if (potential.getLevel() == null){
                continue;
            }
            String level = potential.getLevel().getLevelName().toUpperCase();
            switch (level){
                case "LEVEL 1":
                    result[0]++;
                    break;
                case "LEVEL 2":
                    result[1]++;
                    break;
                case "LEVEL 3":
                    result[2]++;
                    break;
                case "LEVEL 4":
                    result[3]++;
                    break;
                case "LEVEL 5":
                    result[4]++;
                    break;
                case "LEVEL 6":
                    result[5]++;
                    break;
                case "LEVEL 7":
                    result[6]++;
                    break;
                case "LEVEL 0":
                    result[7]++;
                    break;
                default:
                    break;
            }
        }
        return Arrays.asList(result);
    }
}
