package com.ws.inspectionoffice.model;

import lombok.Data;

import java.util.List;

@Data
public class Body {
    private String fatherUrl;
    private List<String> childUrlList;
}
