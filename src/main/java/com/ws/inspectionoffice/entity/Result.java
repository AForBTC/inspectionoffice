package com.ws.inspectionoffice.entity;

import lombok.Data;

@Data
public class Result {
    private Long id;
    private Long contrastId;
    private Long childId;
    private String resultfileName;
    private String resultfileUrl;
    private String resultfileHtml;
}
