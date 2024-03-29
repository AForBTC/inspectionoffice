package com.ws.inspectionoffice.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Contrast {
    private Long id;
    private String contrastName;
    private String fatherfileUrl;
    private String fatherfileName;
    private String zipUrl;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimestamp;
    private List<Result> resultList;
}
