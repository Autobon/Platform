create view t_tech_finance_view  as
select f.*,t.phone,t.name,t.gender from t_tech_finance f LEFT JOIN t_technician t on t.id = f.tech_id