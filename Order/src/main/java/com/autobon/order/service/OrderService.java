package com.autobon.order.service;

import com.autobon.order.entity.ConstructionWaste;
import com.autobon.order.entity.Order;
import com.autobon.order.entity.WorkDetail;
import com.autobon.order.repository.ConstructionWasteRepository;
import com.autobon.order.repository.OrderRepository;
import com.autobon.order.repository.WorkDetailRepository;
import com.autobon.order.vo.ConstructionDetail;
import com.autobon.order.vo.ConstructionShow;
import com.autobon.order.vo.ProjectPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by yuh on 2016/2/22.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private WorkDetailRepository workDetailRepository;
    @Autowired
    private ConstructionWasteRepository constructionWasteRepository;


    public Order get(int orderId) {
        return repository.findOne(orderId);
    }

    public void save(Order order) {
        repository.save(order);
    }

    @Transactional
    public void save(Order order,ConstructionShow constructionShow){
        repository.save(order);

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

    public Page<Order> findFinishedOrderByMainTechId(int techId, int page, int pageSize) {
        return repository.findFinishedOrderByMainTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findFinishedOrderBySecondTechId(int techId, int page, int pageSize) {
        return repository.findFinishedOrderBySecondTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> findUnfinishedOrderByTechId(int techId, int page, int pageSize) {
        return repository.findUnfinishedOrderByTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public Page<Order> find(String orderNum, String creatorName, String contactPhone,
                            List<Integer> orderType, Integer statusCode, String sort, Sort.Direction direction, int page, int pageSize) {
        return repository.find(orderNum, creatorName, contactPhone, orderType,
                statusCode, new PageRequest(page - 1, pageSize, direction, sort));
    }

    public Page<Order> findExpired(Date signInBefore, Date finishBefore,  int page, int pageSize){
        return repository.findExpired(signInBefore, finishBefore, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public Page<Order> findBetweenByTechId(int techId, Date start, Date end, int page, int pageSize) {
        return repository.findBetweenByTechId(techId, start, end, new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "id"));
    }

    public int countOfNew(Date from, Date to) {
        return repository.countOfNew(from, to);
    }

    public int countOfFinished(Date from, Date to) {
        return repository.countOfFinished(from, to);
    }

    public int totalOfCreated() {
        return repository.totalOfCreated();
    }

    public int totalOfFinished() {
        return repository.totalOfFinished();
    }

    public int countOfCoopAccount(int accountId) {
        return repository.countOfCoopAccount(accountId);
    }
}
