package com.autobon.technician.service;

import com.autobon.technician.entity.CodeItem;
import com.autobon.technician.repository.CodeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dave on 16/3/2.
 */
@Service
public class CodeItemService {
    @Autowired
    CodeItemRepository repository;

    public List<CodeItem> findByCodemap(String codemap) {
        return repository.findByCodemap(codemap);
    }
}
