CREATE VIEW `v_technician` AS select t.*, s.star_rate, s.balance, s.unpaid_orders, s.total_orders, s.comment_count
from t_technician t left join t_tech_stat s on t.id = s.tech_id;
