package com.gcl.crm.service;

import com.gcl.crm.entity.Diary;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.User;
import com.gcl.crm.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiaryService {

    @Autowired
    DiaryRepository diaryRepository;

    public List<Diary> getDiariesByPotentialId(Long id){
        return diaryRepository.findDiariesByPotentialId(id);
    }

    public boolean createDiary(String content, User currentUser, Potential potential){
        Diary diary = new Diary();
        diary.setContent(content);
        diary.setCreatedDate(this.getCurrentDate());
        diary.setMaker(currentUser.getEmployee().getId());
        diary.setPotential(potential);
        diaryRepository.save(diary);
        return true;
    }

    public Map<Diary, Employee> getDiaryMap(Long potentialId){
        List<Object[]> objects = diaryRepository.findDiaryListByPotentialId(potentialId);
        Map<Diary, Employee> result = new HashMap<>();
        for (Object[] item : objects){
            Diary diary = (Diary) item[0];
            Employee employee = (Employee) item[1];
            result.put(diary, employee);
        }
        return result;
    }

    private Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }
}
