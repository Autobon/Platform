drop view t_tech_cash_apply_view;
create view t_tech_cash_apply_view  as
select a.*,t.name as tech_name,o.order_num from t_tech_cash_apply a LEFT JOIN t_technician t on t.id = a.tech_id LEFT JOIN t_order o on o.id = a.order_id