package com.autobon.order.service;

import com.autobon.order.entity.Product;
import com.autobon.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2016/11/17.
 */
@Service
public class ProductService {
    @PersistenceContext
    protected EntityManager em;

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public void batchInsert(List<Product> list) {
        for(int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if(i % 30== 0) {
                em.flush();
                em.clear();
            }
        }
    }


    public Page<Product> find(Integer type, String brand, String code, String model, Integer constructionPosition, Integer currentPage, Integer pageSize){
        currentPage = currentPage==null?1:currentPage;
        currentPage = currentPage<=0?1:currentPage;
        pageSize = pageSize==null?10:pageSize;
        pageSize = pageSize<=0?10:pageSize;
        pageSize = pageSize>20?20:pageSize;
        Pageable p = new PageRequest(currentPage-1,pageSize);
        if(brand != null){
            brand = "%"+brand+"%";
        }
        if(code != null){
            code = "%"+code+"%";
        }
        if(model != null){
            model = "%"+model+"%";
        }

        Page<Product> page = productRepository.find(type, brand, code ,model ,constructionPosition, p);

        return page;
    }

    public Product get(int pid){
        Product product = productRepository.findById(pid);
        if(product != null){
            return product;
        }

        return null;
    }


    public Product save(Product product){

        return productRepository.save(product);
    }


    public int delete(int pid){
        productRepository.delete(pid);
        return 0;
    }



    public int deleteAll(){
        productRepository.deleteAll();
        return 0;
    }


    public List<Product> getByType(List<Integer> type){
        List<Object[]> list = productRepository.getByType(type);
        List<Product> products = new ArrayList<>();
        if(list!=null && list.size()>0){
            for(Object[] objects: list) {
                Product product = new Product(objects);
                products.add(product);
            }
        }

        return products;
    }



}
