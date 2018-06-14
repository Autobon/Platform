package com.autobon.platform.controller.admin;

import com.autobon.shared.JsonResult;
import com.autobon.staff.entity.Staff;
import com.autobon.technician.entity.Team;
import com.autobon.technician.entity.Technician;
import com.autobon.technician.service.TeamService;
import com.autobon.technician.service.TechnicianService;
import com.autobon.technician.vo.TeamShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by tian on 18/6/11.
 */
@RestController("adminTeamController")
@RequestMapping("/api/web/admin/team")
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    TechnicianService technicianService;


    /**
     * 查询团队
     *
     * @param name
     * @param managerId
     * @param managerName
     * @param managerPhone
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult getTeam(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "managerId",required = false) Integer managerId,
                              @RequestParam(value = "managerName",required = false) String managerName,
                              @RequestParam(value = "managerPhone",required = false) String managerPhone,
                              @RequestParam(value = "page",  defaultValue = "1" )  int page,
                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                              HttpServletRequest request) {
//
//        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
//        int coopId = coopAccount.getCooperatorId();

        Page<Team> teams = teamService.find(name, managerId, managerName, managerPhone, page, pageSize);
        return new JsonResult(true, teams);

    }

    /**
     * 查询团队详情
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public JsonResult getTeamDetail(@PathVariable("id") int id,
                                    HttpServletRequest request) {
//
//        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
//        int coopId = coopAccount.getCooperatorId();

        Team team = teamService.get(id);
        return new JsonResult(true, team);
    }

    /**
     * 查询团队成员
     *
     * @param request
     * @param teamId
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/{teamId:\\d+}/member", method = RequestMethod.GET)
    public JsonResult getTeamMember(HttpServletRequest request,
                                    @PathVariable("teamId") int teamId,
                                    @RequestParam(value = "page",  defaultValue = "1" )  int page,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
//
//        CoopAccount coopAccount = (CoopAccount) request.getAttribute("user");
//        int coopId = coopAccount.getCooperatorId();

        Page<Technician> technicians = teamService.findTechByTeam(teamId, page, pageSize);
        return new JsonResult(true, technicians);


    }

    /**
     * 添加团队成员
     *
     * @param request
     * @param id
     * @param techId
     * @return
     */
    @RequestMapping(value = "/{id:\\d+}/member/{techId:\\d+}", method = RequestMethod.POST)
    public JsonResult addTeamMember(HttpServletRequest request,@PathVariable("id") int id,
                                    @PathVariable("techId") int techId) {

        Team team = teamService.get(id);
        if(team == null) {
            return new JsonResult(false, "团队不存在");
        }
        Technician technician = technicianService.get(techId);
        if(technician == null) {
            return new JsonResult(false, "技师不存在");
        }
        if(technician.getTeamId() == id){
            return new JsonResult(false, "技师已在该团队，请勿重复添加");
        }
        technician.setTeamId(id);
        technicianService.save(technician);
        return new JsonResult(true, "添加成功");
    }

    /**
     * 删除团队成员
     *
     * @param request
     * @param id
     * @param tecId
     * @return
     */
    @RequestMapping(value = "/{id:\\d+}/member/{tecId:\\d+}", method = RequestMethod.DELETE)
    public JsonResult deleteTeamMember(HttpServletRequest request,@PathVariable("id") int id,
                                    @PathVariable("tecId") int tecId) {

        Team team = teamService.get(id);
        if(team == null) {
            return new JsonResult(false, "团队不存在");
        }
        Technician technician = technicianService.get(tecId);
        if(technician == null) {
            return new JsonResult(false, "技师不存在");
        }
        if(technician.getId() == team.getManagerId()){
            return new JsonResult(false, "团队负责人不能被删除");
        }
        technician.setTeamId(null);
        technicianService.save(technician);
        return new JsonResult(true, "删除成功");
    }

    /**
     * 新增团队
     *
     * @param request
     * @param name
     * @param managerName
     * @param managerPhone
     * @param managerId
     * @return
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult createTeam(HttpServletRequest request,
                                 @RequestParam("name") String name,
                                 @RequestParam("managerName") String managerName,
                                 @RequestParam("managerPhone") String managerPhone,
                                 @RequestParam("managerId") Integer managerId){
        Staff staff = (Staff) request.getAttribute("user");
        if(staff == null){
            return new JsonResult(false, "登陆过期");
        }
        if(name != null){
            Team check = teamService.getByName(name);
            if(check != null) {
                return new JsonResult(false, "团队姓名已存在");
            }
        }
        if(managerId != null){
            Team check = teamService.getByManagerId(managerId);
            if(check != null) {
                return new JsonResult(false, "该技师已拥有团队");
            }
        }

        Team team = new Team();
        team.setName(name);
        team.setManagerName(managerName);
        team.setManagerPhone(managerPhone);
        team.setManagerId(managerId);
        Team res = teamService.save(team);

        Technician technician = technicianService.get(managerId);
        technician.setTeamId(res.getId());
        technicianService.save(technician);

        return new JsonResult(true, res);

    }

    /**
     * 修改团队
     *
     * @param request
     * @param id
     * @param name
     * @param managerName
     * @param managerPhone
     * @param managerId
     * @return
     */
    @Transactional
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST)
    public JsonResult updateTeam(HttpServletRequest request,@PathVariable("id") Integer id,
                                 @RequestParam("name") String name,
                                 @RequestParam("managerName") String managerName,
                                 @RequestParam("managerPhone") String managerPhone,
                                 @RequestParam("managerId") Integer managerId){
        Staff staff = (Staff) request.getAttribute("user");
        if(staff == null){
            return new JsonResult(false, "登陆过期");
        }
        Team check1 = teamService.get(id);
        if(check1 == null) {
            return new JsonResult(false, "团队不存在");
        }
        if(name != null){
            Team check2 = teamService.getByName(name);
            if(check2 != null && check2.getId() != id) {
                return new JsonResult(false, "团队姓名已存在");
            }
        }
        if(managerId != null){
            Team check = teamService.getByManagerId(managerId);
            if(check != null) {
                return new JsonResult(false, "该技师已拥有团队");
            }
        }

        Team team = teamService.get(id);
        team.setName(name);
        team.setManagerName(managerName);
        team.setManagerPhone(managerPhone);
        team.setManagerId(managerId);
        Team res = teamService.save(team);

        Technician technician = technicianService.get(managerId);
        technician.setTeamId(res.getId());
        technicianService.save(technician);

        return new JsonResult(true, res);

    }

    /**
     * 删除团队
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.DELETE)
    public JsonResult deleteTeam(HttpServletRequest request,@PathVariable("id") int id){
        Staff staff = (Staff) request.getAttribute("user");
        if(staff == null){
            return new JsonResult(false, "登陆过期");
        }
        Team check1 = teamService.get(id);
        if(check1 == null) {
            return new JsonResult(false, "团队不存在");
        }
        teamService.deleteTeam(id);
        return new JsonResult(true, "删除成功");
    }
}
