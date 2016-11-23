package com.autobon.order.service;

import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.ConstructionWasteRepository;
import com.autobon.order.repository.OrderProductRepository;
import com.autobon.order.repository.WorkDetailRepository;

import com.autobon.order.vo.ConstructionDetail;
import com.autobon.order.vo.ConstructionShow;
import com.autobon.order.vo.ProjectPosition;
import com.autobon.order.vo.WorkDetailShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wh on 2016/11/2.
 */

@Service
public class WorkDetailService {

    @Autowired
    WorkDetailRepository workDetailRepository;
    @Autowired
    ConstructionWasteRepository constructionWasteRepository;
    @Autowired
    OrderProductRepository orderProductRepository;



    public int save(WorkDetail workDetail){
        workDetailRepository.save(workDetail);
        return 0;
    }

    public int save(List<WorkDetail> workDetails){
        if(workDetails!=null&& workDetails.size()>0){
           for(WorkDetail workDetail:workDetails){
               workDetailRepository.save(workDetail);
           }
        }
        return 0;
    }



    public int balance(List<WorkDetail> workDetails){
        int money = 0;
        if(workDetails !=null && workDetails.size()> 0){
            for(WorkDetail workDetail: workDetails){
                int orderId = workDetail.getOrderId();
                if(workDetail.getProject1() != null&&workDetail.getPosition1()!=null){
                    int projectId = workDetail.getProject1();
                    String positionId = workDetail.getPosition1();
                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    int sum = orderProductRepository.getMoney(orderId, projectId, positionIds);
                    money += sum;

                }
                if(workDetail.getProject2() != null&&workDetail.getPosition2()!=null){
                    int projectId = workDetail.getProject2();
                    String positionId = workDetail.getPosition2();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }

                    int sum = orderProductRepository.getMoney(orderId, projectId, positionIds);
                    money += sum;
                }
                if(workDetail.getProject3() != null&&workDetail.getPosition3()!=null){
                    int projectId = workDetail.getProject3();
                    String positionId = workDetail.getPosition3();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    int sum = orderProductRepository.getMoney(orderId, projectId, positionIds);
                    money += sum;
                }
                if(workDetail.getProject4() != null&&workDetail.getPosition4()!=null){
                    int projectId = workDetail.getProject4();
                    String positionId = workDetail.getPosition4();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    int sum = orderProductRepository.getMoney(orderId, projectId, positionIds);

                    money += sum;
                }

                workDetail.setPayment(money);
                workDetail.setPayStatus(1);
                workDetail.setCreateDate(new Date());
                workDetailRepository.save(workDetail);
            }
        }

        return 0;
    }



    public WorkDetail getByOderIdAndTechId(int orderId, int techId){

        return workDetailRepository.findByOrderIdAndTechId(orderId, techId);
    }


    public List<WorkDetailShow> getByOrderId(int orderId){
        List<Object[]> list = workDetailRepository.getByOrderId(orderId);
        List<WorkDetailShow> workDetailShows = new ArrayList<>();
        for(Object[] objects : list){
            WorkDetailShow workDetailShow = new WorkDetailShow(objects);
            workDetailShows.add(workDetailShow);
        }

        return workDetailShows;
    }



    public void save(ConstructionShow constructionShow){
        List<ConstructionDetail> constructionDetails = constructionShow.getConstructionDetails();
        for(ConstructionDetail constructionDetail: constructionDetails){
            WorkDetail workDetail = new WorkDetail();
            workDetail.setOrderId(constructionShow.getOrderId());
            workDetail.setTechId(constructionDetail.getTechId());
            List<ProjectPosition> projectPositions = constructionDetail.getProjectPositions();
            for(int i= 0; i<projectPositions.size();i++){
                if(i+1 ==1){
                    workDetail.setProject1(projectPositions.get(i).getProject());
                    workDetail.setPosition1(projectPositions.get(i).getPosition());
                }else if(i+1 ==2){
                    workDetail.setProject2(projectPositions.get(i).getProject());
                    workDetail.setPosition2(projectPositions.get(i).getPosition());
                }
                else if(i+1 ==3){
                    workDetail.setProject3(projectPositions.get(i).getProject());
                    workDetail.setPosition3(projectPositions.get(i).getPosition());
                }else{
                    workDetail.setProject4(projectPositions.get(i).getProject());
                    workDetail.setPosition4(projectPositions.get(i).getPosition());
                }
            }
            workDetail.setCreateDate(new Date());
            workDetailRepository.save(workDetail);
        }
        List<ConstructionWaste> constructionWastes = constructionShow.getConstructionWastes();
        for(ConstructionWaste constructionWaste: constructionWastes){
            constructionWaste.setOrderId(constructionShow.getOrderId());
            constructionWasteRepository.save(constructionWastes);
        }
    }
}
