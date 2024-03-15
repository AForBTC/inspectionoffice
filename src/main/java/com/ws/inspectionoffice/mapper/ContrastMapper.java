package com.ws.inspectionoffice.mapper;


import com.ws.inspectionoffice.entity.Child;
import com.ws.inspectionoffice.entity.Contrast;
import com.ws.inspectionoffice.entity.Result;

import java.util.List;

public interface ContrastMapper {

    public List<Contrast> selectContrastList(Contrast contrast);
    public void insertContrast(Contrast contrast);
    public void insertChild(Child child);
    public void insertResult(Result result);
}
