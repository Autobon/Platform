create view t_work_detail_view  as
select w.*,o.real_order_num,o.add_time,o.vehicle_model,o.license,o.vin,o.remark,o.technician_remark,o.make_up_remark,c.fullname,t.name as tech_name,
cp1.name as project_name1,
cp2.name as project_name2,
cp3.name as project_name3,
cp4.name as project_name4,
cp5.name as project_name5,
cp6.name as project_name6
from t_work_detail w LEFT JOIN t_technician t ON w.tech_id = t.id
LEFT JOIN t_order o ON w.order_id = o.id
LEFT JOIN t_cooperators c ON o.coop_id = c.id
LEFT JOIN t_construction_project cp1 on w.project1 = cp1.id
LEFT JOIN t_construction_project cp2 on w.project2 = cp2.id
LEFT JOIN t_construction_project cp3 on w.project3 = cp3.id
LEFT JOIN t_construction_project cp4 on w.project4 = cp4.id
LEFT JOIN t_construction_project cp5 on w.project5 = cp5.id
LEFT JOIN t_construction_project cp6 on w.project6 = cp6.id