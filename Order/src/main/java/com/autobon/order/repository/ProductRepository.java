package com.autobon.order.repository;

import com.autobon.order.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



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



}
