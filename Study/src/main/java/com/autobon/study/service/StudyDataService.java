package com.autobon.study.service;

import com.autobon.study.entity.StudyData;
import com.autobon.study.repository.StudyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/14.
 */
@Service
public class StudyDataService {
    @Autowired
    StudyDataRepository repository;

    public StudyData get(int id) {
        return repository.findOne(id);
    }

    public StudyData getByName(String fileName) {

        return repository.findByFileName(fileName);
    }

    public StudyData save(StudyData studyData) {
        return repository.save(studyData);
    }

    public Page<StudyData> find(String fileName, Integer type, int page, int pageSize) {
        if(fileName != null){
            fileName = "%"+fileName+"%";
        }
        return repository.find(fileName, type,
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }
}
