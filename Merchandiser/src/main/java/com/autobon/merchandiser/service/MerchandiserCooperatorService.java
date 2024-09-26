package com.autobon.merchandiser.service;

import com.autobon.merchandiser.repository.MerchandiserCooperatorRepository;
import com.autobon.merchandiser.vo.MerchandiserCooperatorShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wh on 2018/6/5.
 */
@Service
public class MerchandiserCooperatorService {

    @Autowired
    MerchandiserCooperatorRepository merchandiserCooperatorRepository;



    public List<MerchandiserCooperatorShow> findByMerchandiserId(int merchandiserId){

        List<MerchandiserCooperatorShow> merchandiserCooperatorShows = new ArrayList<>();
        List<Object[]> objects = merchandiserCooperatorRepository.find(merchandiserId);
        for(Object[] o: objects){
            merchandiserCooperatorShows.add(new MerchandiserCooperatorShow(o));
        }

        return merchandiserCooperatorShows;
    }


}
