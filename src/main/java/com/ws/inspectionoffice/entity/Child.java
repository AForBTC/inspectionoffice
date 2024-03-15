package com.ws.inspectionoffice.entity;

import lombok.Data;

@Data
public class Child {
    private Long id;
    private Long contrastId;
    private String childfileName;
    private String childfileUrl;
}
