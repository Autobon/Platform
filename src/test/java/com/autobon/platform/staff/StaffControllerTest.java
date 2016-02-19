package com.autobon.platform.staff;


import com.autobon.platform.staff.controller.StaffController;
import com.autobon.platform.utils.JsonMessage;
import com.autobon.staff.service.StaffService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Liujingguo91year on 2016/1/21.
 */
public class StaffControllerTest {

    private StaffService staffService = null;

    @Before
    public void setup(){
        this.staffService = Mockito.mock(StaffService.class);
        Mockito.when(staffService.login("admin", "admin")).thenReturn(0);
    }
    @Test
    public void loginTest(){
        StaffController target = new StaffController();
        target.setStaffService(staffService);
        HttpServletResponse response = new MockHttpServletResponse();
        JsonMessage jsonMessage = target.login("admin", "admin",response);
        Assert.assertTrue(jsonMessage.getResult());

    }

    @Test
    public void logoutTest(){
        StaffController target = new StaffController();
        target.setStaffService(staffService);
        HttpServletResponse response = new MockHttpServletResponse();
        JsonMessage jsonMessage = target.logout(null, response, "E3C500A4C988CD744E9738D91ABDFCE2");
        Assert.assertTrue(jsonMessage.getResult());

    }
}
