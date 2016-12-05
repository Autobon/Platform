CREATE VIEW t_order_view as  SELECT   o.id ,
                                          o.order_num,
                                          o.photo,
                                          o.agreed_start_time  ,
                                          o.agreed_end_time,
                                          o.status  ,
                                          o.creator_type ,
                                          o.main_tech_id as tech_id,
                                          tech.name as tech_name ,
                                          tech.phone as tech_phone ,
                                          tech.avatar as tech_avatar,
                                          o.before_photos ,
                                          o.after_photos ,
                                          o.start_time ,
                                          o.end_time ,
                                          o.sign_time ,
                                          o.taken_time ,
                                          o.add_time as create_time ,
                                          o.type ,
                                          o.coop_id ,
                                          ca.shortname as coop_name,
                                          o.creator_id ,
                                          o.creator_name ,
                                          ca.phone as contact_phone ,
                                          ct.address ,
                                          ct.longitude  ,
                                          ct.latitude  ,
                                          o.remark ,
                                          o.product_status ,
                                          o.reassignment_status ,
                                          wd.payment  ,
                                          wd.pay_status ,
                                          ts.total_orders as order_count,
                                           ts.star_rate as evaluate,
                                           0 as cancel_count,
                                           t.id as evaluate_status,
                                           ls.lat as  tech_latitude,
                                           ls.lng as tech_longitude,
                                           tech.work_status as work_status
                                          FROM
                                          t_order o
                                          LEFT JOIN t_technician tech ON tech.id = o.main_tech_id
                                          LEFT JOIN t_coop_account ca ON ca.id = o.creator_id
                                          LEFT JOIN t_cooperators ct ON ct.id = o.coop_id
                                          LEFT JOIN t_work_detail wd ON wd.order_id = o.id
                                          LEFT JOIN t_tech_stat ts ON ts.tech_id = o.main_tech_id
                                          LEFT JOIN t_comment t ON t.order_id = o.id
                                          LEFT JOIN t_location_status ls ON ls.tech_id = o.main_tech_id
                                          GROUP BY o.id
