CREATE VIEW t_order_product_view as  SELECT  op.id, op.order_id, op.construction_project_id,
 op.construction_position_id, op.product_id, p.type,
 p.brand,p.code, p.model,p.construction_position, p.working_hours,
 p.construction_commission,p.star_level,p.scrap_cost,p.warranty
  from t_order_product op left join t_product p on op.product_id = p.id;

