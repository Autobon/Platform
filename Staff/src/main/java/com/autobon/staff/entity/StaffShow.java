package com.autobon.staff.entity;

import java.util.Date;

/**
 * Created by liz on 2016/2/18.
 */
public class StaffShow {

    /**
     * Created by samsung on 2016/2/3.
     */

        private Integer id;
        private String userName;
        private Date registerTime;

       public StaffShow(Integer id,String username,Date registerTime){
           this.id = id;
           this.userName = username;
           this.registerTime = registerTime;
       }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Date getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(Date registerTime) {
            this.registerTime = registerTime;
        }


}
