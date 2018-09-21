package com.autobon.order.service;

import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.entity.WorkDetailView;
import com.autobon.order.repository.ConstructionWasteRepository;
import com.autobon.order.repository.OrderProductRepository;
import com.autobon.order.repository.WorkDetailRepository;

import com.autobon.order.repository.WorkDetailViewRepository;
import com.autobon.order.vo.ConstructionDetail;
import com.autobon.order.vo.ConstructionShow;
import com.autobon.order.vo.ProjectPosition;
import com.autobon.order.vo.WorkDetailShow;
import com.autobon.technician.entity.TechFinance;
import com.autobon.technician.entity.TechStat;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.repository.TechFinanceRepository;
import com.autobon.technician.repository.TechStatRepository;
import com.autobon.technician.repository.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    TechStatRepository techStatRepository;
    @Autowired
    TechFinanceRepository techFinanceRepository;
    @Autowired
    WorkDetailViewRepository workDetailViewRepository;
    @Autowired
    TechnicianRepository technicianRepository;




    public Page<WorkDetail> find(int techId, Date start, Date end, int page, int pageSize){

        return workDetailRepository.find(techId, start, end,
                new PageRequest(page - 1, pageSize, Sort.Direction.ASC, "id"));
    }



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


    public List<WorkDetail> findByOrderId(int orderId){
        return workDetailRepository.findByOrderId(orderId);
    }


    @Transactional
    public float balance(int oid){
        List<WorkDetail> workDetails =  workDetailRepository.findByOrderId(oid);

        if(workDetails !=null && workDetails.size()> 0){

            for(WorkDetail workDetail: workDetails){
                int techId = workDetail.getTechId();

                float money = 0;
                float cost = 0;
                int orderId = workDetail.getOrderId();
                if(workDetail.getProject1() != null&&workDetail.getPosition1()!=null){
                    int projectId = workDetail.getProject1();
                    String positionId = workDetail.getPosition1();
                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;

                }
                if(workDetail.getProject2() != null&&workDetail.getPosition2()!=null){
                    int projectId = workDetail.getProject2();
                    String positionId = workDetail.getPosition2();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }

                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;
                }
                if(workDetail.getProject3() != null&&workDetail.getPosition3()!=null){
                    int projectId = workDetail.getProject3();
                    String positionId = workDetail.getPosition3();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;
                }
                if(workDetail.getProject4() != null&&workDetail.getPosition4()!=null){
                    int projectId = workDetail.getProject4();
                    String positionId = workDetail.getPosition4();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;
                }

                if(workDetail.getProject5() != null&&workDetail.getPosition5()!=null){
                    int projectId = workDetail.getProject5();
                    String positionId = workDetail.getPosition5();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;
                }

                if(workDetail.getProject6() != null&&workDetail.getPosition6()!=null){
                    int projectId = workDetail.getProject6();
                    String positionId = workDetail.getPosition6();

                    List<Integer> positionIds = new ArrayList<>();
                    String[] pStr = positionId.split(",");
                    for(String id: pStr){
                        positionIds.add(Integer.valueOf(id));
                    }
                    float sum = orderProductRepository.getMoney(orderId, projectId, positionIds) == null? 0:orderProductRepository.getMoney(orderId, projectId, positionIds);
                    float total = orderProductRepository.getCost(orderId, projectId, positionIds) == null? 0:orderProductRepository.getCost(orderId, projectId, positionIds);

                    money += sum;
                    cost += total;
                }
                workDetail.setPayment(money);
                workDetail.setTotalCost(cost);
                workDetail.setPayStatus(1);
                workDetail.setCreateDate(new Date());
                workDetailRepository.save(workDetail);


                TechStat techStat = techStatRepository.getByTechId(techId);
                techStat.setBalance(techStat.getBalance() + money);
                techStatRepository.save(techStat);

                TechFinance techFinance = techFinanceRepository.getByTechId(techId);
                List<WorkDetailShow> list = getByTechId(techId);
                int total = 0;
                if (list != null && list.size() > 0) {
                    for (WorkDetailShow workDetailShow : list) {
                        total += workDetailShow.getPayment();
                    }
                }
                techFinance.setSumIncome(new BigDecimal(total));
                techFinance.setNotCash(techFinance.getSumCash() == null ? new BigDecimal(total) : new BigDecimal(total).subtract(techFinance.getSumCash()));
                techFinanceRepository.save(techFinance);
            }
        }
        return 0;
    }

    //临时用，修改财务
    public void testFinance(){
        List<Technician> techList = technicianRepository.findAll();
        for(Technician t : techList){
            TechFinance techFinance = techFinanceRepository.getByTechId(t.getId());
            if(techFinance != null){
                List<WorkDetailShow> list = getByTechId(t.getId());
                int total = 0;
                if (list != null && list.size() > 0) {
                    for (WorkDetailShow workDetailShow : list) {
                        total += workDetailShow.getPayment();
                    }
                }
                techFinance.setSumIncome(new BigDecimal(total));
                techFinance.setNotCash(techFinance.getSumCash() == null ? new BigDecimal(total) : new BigDecimal(total).subtract(techFinance.getSumCash()));
                techFinanceRepository.save(techFinance);
            }
        }
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

    public List<WorkDetailShow> getByTechId(int techId){
        List<Object[]> list = workDetailRepository.getByTechId(techId);
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

    public Page<WorkDetailView> findViews(int techId, int page, int pageSize){
        return workDetailViewRepository.findViews(techId, new PageRequest(page - 1, pageSize, new Sort(Sort.Direction.ASC, "id")));
    }

    public List<WorkDetailView> findAllViews(){
        return workDetailViewRepository.findAllViews();
    }

    public String findLargest(int techId){
        return workDetailRepository.findlargest(techId);
    }

    public String findLargest(){
        return workDetailRepository.findlargest();
    }
}
