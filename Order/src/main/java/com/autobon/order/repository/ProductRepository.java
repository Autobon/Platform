package com.autobon.order.repository;

import com.autobon.order.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by wh on 2016/11/17.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    @Query("select p from Product p where 1=1 " +
            " and (?1 is null or p.type =?1)  " +
            " and (?2 is null or p.brand  like ?2)  " +
            " and (?3 is null or p.code  like ?3)  " +
            " and (?4 is null or p.model like ?4) " +
            " and (?5 is null or p.constructionPosition =?5)")
    Page<Product> find( Integer type, String brand, String code, String model, Integer constructionPosition, Pageable pageable);

    Product findById(int id);

    @Query(value = "SELECT" +
            " p.id as id," +
            " p.type as type," +
            " p.brand as brand," +
            " p.code as code," +
            " p.model as model," +
            " p.construction_position as construction_position," +
            " p.working_hours as working_hours," +
            " p.construction_commission as construction_commission," +
            " p.star_level as star_level," +
            " p.scrap_cost as scrap_cost," +
            " p.warranty as warranty" +
            " FROM" +
            " t_construction_project cp1 " +
            " INNER JOIN t_construction_position cp2 ON FIND_IN_SET(cp2.id, cp1.ids) " +
            " INNER JOIN t_product p ON cp1.id = p.type " +
            " WHERE " +
            " p.construction_position = cp2.id and " +
            " p.type IN (?1) " +
            " ORDER BY " +
            " p.type, " +
            " p.construction_position ASC" ,nativeQuery = true)
    List<Object[]> getByType(List<Integer> typeIds);



}
