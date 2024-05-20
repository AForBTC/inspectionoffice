package com.ws.inspectionoffice.mapper;


import com.ws.inspectionoffice.entity.Contrast;
import com.ws.inspectionoffice.entity.Number;
import com.ws.inspectionoffice.entity.Result;

import java.util.List;

public interface ContrastMapper {

    public List<Contrast> selectContrastList(Contrast contrast);
    void updateContrast(Contrast contrast);
    public void insertContrast(Contrast contrast);
    public void insertResult(Result result);
    void deleteContrastById(Long contrastId);
    public void updateNumber(Number number);
    public Number selectNumber();

}
