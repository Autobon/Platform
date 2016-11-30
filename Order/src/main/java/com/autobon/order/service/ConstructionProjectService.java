package com.autobon.order.service;

import com.autobon.order.entity.ConstructionPosition;
import com.autobon.order.entity.ConstructionProject;
import com.autobon.order.repository.ConstructionPositionRepository;
import com.autobon.order.repository.ConstructionProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wh on 2016/11/1.
 */

@Service
public class ConstructionProjectService {

    @Autowired
    ConstructionProjectRepository constructionProjectRepository;
    @Autowired
    ConstructionPositionRepository constructionPositionRepository;


    public List<ConstructionPosition> findByName(String name){
        ConstructionProject constructionProject = constructionProjectRepository.findByName(name);
        if(constructionProject!= null){
            String idsStr = constructionProject.getIds();
            String[] ids = idsStr.split(",");
            List<Integer> idList = new ArrayList<>();
            for(int i=0; i<ids.length;i++){
                idList.add(Integer.valueOf(ids[i]));
            }
            return constructionPositionRepository.getByIds(idList);
        }
        return null;
    }


    public List<ConstructionPosition> findByProject(int pid){
        ConstructionProject constructionProject =  constructionProjectRepository.getOne(pid);
        if(constructionProject!= null){
            String idsStr = constructionProject.getIds();
            String[] ids = idsStr.split(",");
            List<Integer> idList = new ArrayList<>();
            for(int i=0; i<ids.length;i++){
                idList.add(Integer.valueOf(ids[i]));
            }
            return constructionPositionRepository.getByIds(idList);
        }
        return null;
    }


    public Map<Integer,String> getProject(){
        Map<Integer, String> map = new HashMap<>();
        List<ConstructionProject> constructionProjects = constructionProjectRepository.findAll();
        for(ConstructionProject constructionProject: constructionProjects){
            map.put(constructionProject.getId(), constructionProject.getName());
        }
        return map;
    }

    public Map<Integer,String>  getPosition(){
        Map<Integer, String> map = new HashMap<>();
        List<ConstructionPosition> constructionPositions = constructionPositionRepository.findAll();
        for(ConstructionPosition constructionPosition: constructionPositions){
            map.put(constructionPosition.getId(), constructionPosition.getName());
        }
        return map;
    }

    public Map<String, Integer>  getProject1(){
        Map<String, Integer> map = new HashMap<>();
        List<ConstructionProject> constructionProjects = constructionProjectRepository.findAll();
        for(ConstructionProject constructionProject: constructionProjects){
            map.put(constructionProject.getName(), constructionProject.getId());
        }
        return map;
    }

    public Map<String, Integer>  getPosition1(){
        Map<String, Integer> map = new HashMap<>();
        List<ConstructionPosition> constructionPositions = constructionPositionRepository.findAll();
        for(ConstructionPosition constructionPosition: constructionPositions){
            map.put(constructionPosition.getName(), constructionPosition.getId());
        }
        return map;
    }

}
