package com.autobon.order.service;

import com.autobon.order.entity.*;
import com.autobon.order.repository.*;
import com.autobon.order.vo.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    @Autowired
    private ConstructionProjectRepository constructionProjectRepository;

    @Autowired
    private ConstructionPositionRepository constructionPositionRepository;

    @Autowired
    private AgentRebateRepository agentRebateRepository;

    @Autowired
    private OrderStatusRecordService orderStatusRecordService;



    public Order get(int orderId) {
        return repository.findOne(orderId);
    }


    @Transactional
    public void save(Order order) {
        Order res = repository.save(order);

        OrderStatusRecord record = orderStatusRecordService.findByOrderIdAndStatus(res.getId(), res.getStatusCode());
        if(record == null) {
            OrderStatusRecord orderStatusRecord = new OrderStatusRecord();
            orderStatusRecord.setOrderId(res.getId());
            orderStatusRecord.setRecordTime(new Date());
            if(res.getStatusCode() < 10) orderStatusRecord.setStatus(0);
            else if(res.getStatusCode() == 10) orderStatusRecord.setStatus(10);
            else if(res.getStatusCode() == 50) orderStatusRecord.setStatus(20);
            else if(res.getStatusCode() == 55) orderStatusRecord.setStatus(30);
            else if(res.getStatusCode() == 56) orderStatusRecord.setStatus(40);
            else if(res.getStatusCode() == 60) orderStatusRecord.setStatus(50);

            orderStatusRecordService.save(orderStatusRecord);
        }
    }

    @Transactional
    public void save(List<Order> list) {
        List<Order> res = repository.save(list);

        for(Order o : res){
            OrderStatusRecord record = orderStatusRecordService.findByOrderIdAndStatus(o.getId(), o.getStatusCode());
            if(record == null) {
                OrderStatusRecord orderStatusRecord = new OrderStatusRecord();
                orderStatusRecord.setOrderId(o.getId());
                orderStatusRecord.setRecordTime(new Date());
                if(o.getStatusCode() < 10) orderStatusRecord.setStatus(0);
                else if(o.getStatusCode() == 10) orderStatusRecord.setStatus(10);
                else if(o.getStatusCode() == 50) orderStatusRecord.setStatus(20);
                else if(o.getStatusCode() == 55) orderStatusRecord.setStatus(30);
                else if(o.getStatusCode() == 56) orderStatusRecord.setStatus(40);
                else if(o.getStatusCode() == 60) orderStatusRecord.setStatus(50);

                orderStatusRecordService.save(orderStatusRecord);
            }
        }
    }

    /**
     * 通过订单编号获取订单施工项目
     * @return
     */
    public List<ConstructionProjectShow> getAllProject(){

        List<ConstructionProjectShow> constructionProjectShowList = new ArrayList<>();

        List<ConstructionProject> constructionProjects = constructionProjectRepository.findAll();
        for(ConstructionProject constructionProject:constructionProjects){
            ConstructionProjectShow constructionProjectShow = new ConstructionProjectShow();
            constructionProjectShow.setId(constructionProject.getId());
            constructionProjectShow.setName(constructionProject.getName());
            String position = constructionProject.getIds();
            String[] positionArr = position.split(",");
            List<Integer> plist = new ArrayList<>();
            for(String positionId: positionArr){
                plist.add( Integer.valueOf(positionId));
            }

            List<ConstructionPosition> constructionPositions = constructionPositionRepository.getByIds(plist);
            constructionProjectShow.setConstructionPositions(constructionPositions);
            constructionProjectShowList.add(constructionProjectShow);
        }
        return constructionProjectShowList;

    }


    /**
     * 通过订单编号获取订单施工项目
     * @param orderId
     * @return
     */
    public List<ConstructionProjectShow> getProject(int orderId){
        Order order = repository.findOne(orderId);
        if(order != null) {
            List<ConstructionProjectShow> constructionProjectShowList = new ArrayList<>();

            String type = order.getType();
            String[] typeArr = type.split(",");
            List<Integer> list = new ArrayList<>();
            for (String projectId : typeArr) {
                list.add(Integer.valueOf(projectId));
            }

            if (list.size()>0) {

                List<ConstructionProject> constructionProjects = constructionProjectRepository.getByIds(list);
                for (ConstructionProject constructionProject : constructionProjects) {
                    ConstructionProjectShow constructionProjectShow = new ConstructionProjectShow();
                    constructionProjectShow.setId(constructionProject.getId());
                    constructionProjectShow.setName(constructionProject.getName());
                    String position = constructionProject.getIds();
                    String[] positionArr = position.split(",");
                    List<Integer> plist = new ArrayList<>();
                    for (String positionId : positionArr) {
                        plist.add(Integer.valueOf(positionId));
                    }

                    List<ConstructionPosition> constructionPositions = constructionPositionRepository.getByIds2(plist, "," + position + ",");
                    constructionProjectShow.setConstructionPositions(constructionPositions);
                    constructionProjectShowList.add(constructionProjectShow);
                }
                return constructionProjectShowList;
            }
        }
        return null;
    }

    /**
     *
     * @param order
     * @param constructionShow
     */
    @Transactional
    public void save(Order order,ConstructionShow constructionShow){
        repository.save(order);
        OrderStatusRecord record = orderStatusRecordService.findByOrderIdAndStatus(order.getId(), order.getStatusCode());
        if(record == null) {
            OrderStatusRecord orderStatusRecord = new OrderStatusRecord();
            orderStatusRecord.setOrderId(order.getId());
            orderStatusRecord.setRecordTime(new Date());
            if(order.getStatusCode() < 10) orderStatusRecord.setStatus(0);
            else if(order.getStatusCode() == 10) orderStatusRecord.setStatus(10);
            else if(order.getStatusCode() == 50) orderStatusRecord.setStatus(20);
            else if(order.getStatusCode() == 55) orderStatusRecord.setStatus(30);
            else if(order.getStatusCode() == 56) orderStatusRecord.setStatus(40);
            else if(order.getStatusCode() == 60) orderStatusRecord.setStatus(50);

            orderStatusRecordService.save(orderStatusRecord);
        }

        List<ConstructionDetail> constructionDetails = constructionShow.getConstructionDetails();
        for(ConstructionDetail constructionDetail: constructionDetails){
            WorkDetail workDetail = new WorkDetail();
            workDetail.setOrderId(constructionShow.getOrderId());
            workDetail.setOrderNum(order.getOrderNum());
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
                }else if(i+1 ==4){
                    workDetail.setProject4(projectPositions.get(i).getProject());
                    workDetail.setPosition4(projectPositions.get(i).getPosition());
                }
                else if(i+1 ==5){
                    workDetail.setProject5(projectPositions.get(i).getProject());
                    workDetail.setPosition5(projectPositions.get(i).getPosition());
                }
                else if(i+1 ==6){
                    workDetail.setProject6(projectPositions.get(i).getProject());
                    workDetail.setPosition6(projectPositions.get(i).getPosition());
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


    public OrderShow getByOrderId(int orderId){

        List<Object[]> o = repository.getByOrderId(orderId);

        return new OrderShow(o.get(0));

    }
    public Order getbyOrderId(int orderId){

        Order order = repository.findOrderById(orderId);

        return order;

    }



    public Page<Order> findFinishedOrder(int techId, int page, int pageSize){
        return  repository.findFinishedOrder(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }


    public Page<Order> findUnfinishedOrder(int techId, int page, int pageSize){
        return  repository.findUnfinishedOrder(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }




    public Page<Order> findAllOrder(int techId, int page, int pageSize){
        return  repository.findAllOrder(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }


    public Page<Order> findOrderByTechId(int techId, int page, int pageSize){
        return  repository.findOrderByTechId(techId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
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
                            List<Integer> orderType, Integer statusCode, String sort, Sort.Direction direction, Integer page, Integer pageSize) {
        if(orderNum != null){
            orderNum = "%"+ orderNum+"%";
        }
        if(creatorName != null){
            creatorName = "%"+ creatorName+"%";
        }
        if(contactPhone != null){
            contactPhone = "%"+ contactPhone+"%";
        }
        String type = null;
        if(orderType != null && orderType.size()>0){
            type = "%" +String.valueOf(orderType.get(0)) +"%";
        }
        return repository.findOrder(orderNum, creatorName, contactPhone, type,
                statusCode, new PageRequest(page - 1, pageSize, direction, sort));
    }
    public Page<Order> find2(String orderNum, String creatorName, String contactPhone,
                            List<Integer> orderType, Integer statusCode, List<String> coopIds,
                             String vin, String license, String addTime, String orderTime, String sort, int page, int pageSize) {
        if(vin != null){
            vin = "%"+ vin+"%";
        }
        if(license != null){
            license = "%"+ license+"%";
        }
        if(orderNum != null){
            orderNum = "%"+ orderNum+"%";
        }
        if(creatorName != null){
            creatorName = "%"+ creatorName+"%";
        }
        if(contactPhone != null){
            contactPhone = "%"+ contactPhone+"%";
        }
        String type = null;
        if(orderType != null && orderType.size()>0){
            type = "%" +String.valueOf(orderType.get(0)) +"%";
        }
        List<Order> list;
        if(sort.equals("id")) {
            list =  repository.findOrder2(orderNum, creatorName, contactPhone, type,
                    statusCode, coopIds, vin, license, addTime, orderTime, (page - 1) * pageSize, pageSize);
        }else{
            list =  repository.findOrder2ByTime(orderNum, creatorName, contactPhone, type,
                    statusCode, coopIds, vin, license, addTime, orderTime, (page - 1) * pageSize, pageSize);
        }
        int count = repository.findOrder2Count(orderNum, creatorName, contactPhone, type,
                statusCode, coopIds, vin, license, addTime, orderTime);
        return new PageImpl<>(list, new PageRequest(page-1,pageSize), count);
    }


    public Page<Order> findOrder(String orderNum, String creatorName, String contactPhone, List<Integer> tech,
                            List<Integer> orderType, Integer statusCode, String sort, Sort.Direction direction, int page, int pageSize) {


        if(orderNum != null){
            orderNum = "%"+ orderNum+"%";
        }

        if(creatorName != null){
            creatorName = "%"+ creatorName+"%";
        }

        if(contactPhone != null){
            contactPhone = "%"+ contactPhone+"%";
        }
        String type = null;

        if(orderType != null && orderType.size()>0){
            type = "%" +String.valueOf(orderType.get(0)) +"%";

        }
        return repository.findOrder(orderNum, creatorName, contactPhone, type,
                statusCode, tech , new PageRequest(page - 1, pageSize, direction, sort));
    }

    public Page<Order> findOrder2(String orderNum, String creatorName, String contactPhone, List<Integer> oids,
                                 List<Integer> orderType, Integer statusCode, List<String> coopIds,
                                  String vin, String license, String addTime, String orderTime, String sort, int page, int pageSize) {
        if(vin != null){
            vin = "%"+ vin+"%";
        }
        if(license != null){
            license = "%"+ license+"%";
        }
        if(orderNum != null){
            orderNum = "%"+ orderNum+"%";
        }
        if(creatorName != null){
            creatorName = "%"+ creatorName+"%";
        }
        if(contactPhone != null){
            contactPhone = "%"+ contactPhone+"%";
        }
        String type = null;
        if(orderType != null && orderType.size()>0){
            type = "%" +String.valueOf(orderType.get(0)) +"%";

        }
        List<Order> list;
        if(sort.equals("id")){
            list =  repository.findOrder2(orderNum, creatorName, contactPhone, type,
                    statusCode, coopIds, vin, license, addTime, orderTime, oids, (page - 1) * pageSize, pageSize);
        }else{
            list =  repository.findOrder2ByTime(orderNum, creatorName, contactPhone, type,
                    statusCode, coopIds, vin, license, addTime, orderTime, oids, (page - 1) * pageSize, pageSize);
        }
        int count =  repository.findOrder2Count(orderNum, creatorName, contactPhone, type,
                statusCode, coopIds, vin, license, addTime, orderTime, oids);
        return new PageImpl<>(list, new PageRequest(page-1,pageSize), count);
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



    public HSSFWorkbook exportExcel(){
      return null;
    }


    public Page<OrderShow> getOrders(Integer techId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        List<Object[]> orderList = new ArrayList<>() ;
        int count = 0;
        if(status == 1){
            orderList = repository.getAllOrder(techId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getAllOrderCount(techId);
        }else if(status == 2){
            orderList = repository.getUnfinishOrder(techId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getUnfinishOrderCount(techId);
        }else if(status == 3) {
            orderList = repository.getfinishOrder(techId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getfinishOrderCount(techId);
        }else{
            orderList = repository.getCooperationOrder(techId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getCooperationOrderCount(techId);
        }



        List<OrderShow> orderShows =  new ArrayList<>();
        for(Object[] objects: orderList){
            OrderShow orderShow = new OrderShow(objects);
            orderShows.add(orderShow);
        }
        return new PageImpl<>(orderShows,p,count);
    }

    public Page<OrderShow> getCoopOrders(Integer coopId,Integer status, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        List<Object[]> orderList = new ArrayList<>() ;
        int count = 0;
        if(status == 1){
            orderList = repository.getCoopUnfinishOrder(coopId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getCoopUnfinishOrderCount(coopId);
        }else if(status == 2){
            orderList = repository.getCoopfinishOrder(coopId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getCoopfinishOrderCount(coopId);
        }else if(status == 3){
            orderList = repository.getUnEvaluateOrder(coopId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getUnfinishOrderCount(coopId);
        }else if(status == 4){
            orderList = repository.getCoopAllOrder(coopId, (currentPage - 1) * pageSize, pageSize);
            count = repository.getCoopAllOrderCount(coopId);
        }



        List<OrderShow> orderShows =  new ArrayList<>();
        for(Object[] objects: orderList){
            OrderShow orderShow = new OrderShow(objects,0);
            orderShows.add(orderShow);
        }
        return new PageImpl<>(orderShows,p,count);
    }

    public Page<OrderShow> getNewCreateOrder(Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        List<Object[]> orderList = repository.getNewCreateOrder((currentPage - 1) * pageSize, pageSize);
        int count = repository.getNewCreateCount();

        List<OrderShow> orderShows =  new ArrayList<>();
        for(Object[] objects: orderList){
            OrderShow orderShow = new OrderShow(objects,1,1);
            orderShows.add(orderShow);
        }
        return new PageImpl<>(orderShows,p,count);
    }


    public int saveAgentRebate(AgentRebate agentRebate){

        agentRebateRepository.save(agentRebate);
        return 0;
    }

    public AgentRebate getAgentRebate(int rid){

        return agentRebateRepository.getOne(rid);
    }


    public List<AgentRebate> findAgentRebate(){

        return agentRebateRepository.findAll();
    }

    public Order saveRemark(int orderId, String remark){
        Order order = repository.findOrderById(orderId);

        order.setTechnicianRemark(remark == null ? order.getTechnicianRemark() : remark);
        return order;
    }

    public Page<Order> findAllByCoop(int coopId, int page, int pageSize) {
        return repository.findAllByCoop(coopId, new PageRequest(page - 1, pageSize,
                new Sort(Sort.Direction.DESC, "id")));
    }

    public List<Integer> findByTechs(List<Integer> list){
        return repository.findByTechs(list);
    }
}
