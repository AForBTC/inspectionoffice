package com.ws.inspectionoffice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.inspectionoffice.entity.Child;
import com.ws.inspectionoffice.entity.Contrast;
import com.ws.inspectionoffice.entity.Result;
import com.ws.inspectionoffice.mapper.ContrastMapper;
import com.ws.inspectionoffice.model.Body;
import com.ws.inspectionoffice.utils.FileLoader;
import com.ws.inspectionoffice.utils.JsonResponse;
import com.ws.inspectionoffice.utils.ResponseCode;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ResponseBody
@RequestMapping("/api")
public class ContrastController {

    @Value("${contrast.url}")
    private String contrastUrl;
    @Value("${contrast.result.url}")
    private String resultUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ContrastMapper contrastMapper;

    @Transactional
    @PostMapping("/contrast")
    public JsonResponse contrast(@RequestBody Body postBody){
        Contrast contrast = new Contrast();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ArrayList<Object> objects = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        ByteArrayResource file = getFile(postBody.getFatherUrl());
        contrast.setFatherfileUrl(postBody.getFatherUrl());
        contrast.setFatherfileName(file.getFilename());
        String modifiedFilename = file.getFilename().substring(0, file.getFilename().lastIndexOf(".")) + "对比" + file.getFilename().substring(file.getFilename().lastIndexOf("."));
        contrast.setContrastName(modifiedFilename);
        contrast.setCreateTimestamp(new Date());
        contrastMapper.insertContrast(contrast);
        map.put("file1",file);
        objects.add(map);
        ArrayList<Child> childList = new ArrayList<>();
        for (String childUrl : postBody.getChildUrlList()){
            Child child = new Child();
            HashMap<String, Object> mapC = new HashMap<>();
            ByteArrayResource fileC = getFile(childUrl);
            mapC.put("file1",fileC);
            objects.add(mapC);
            child.setChildfileUrl(childUrl);
            child.setChildfileName(fileC.getFilename());
            child.setContrastId(contrast.getId());
            contrastMapper.insertChild(child);
            childList.add(child);
        }
        body.put("files", objects);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(contrastUrl, HttpMethod.POST, requestEntity, String.class);
        String res = responseEntity.getBody();
        JSONObject resObject = JSON.parseObject(res);
        String code = resObject.getString("code");
        if(code.equals(0)){
            JSONArray dataJsonArr = resObject.getJSONArray("data");
            ArrayList<Result> results = new ArrayList<>();
            for(Object data : dataJsonArr){
                JSONObject dataObj = (JSONObject) data;
                String filename = dataObj.getString("filename");
                Optional<Child> fileOptional = childList.stream()
                        .filter(child -> filename.equals(file.getFilename()))
                        .findFirst();
                createResultDocx(dataObj,contrast,fileOptional.get(), results);
            }
            return new JsonResponse().code(ResponseCode.OK).data(results);
        } else {
            return JsonResponse.wrapper().code(ResponseCode.ERROR_SERVER_ERROR).message("操作失败");
        }
    }

    @GetMapping("/history")
    public JsonResponse history(){
        Contrast contrast = new Contrast();
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }

    @GetMapping("/info")
    public JsonResponse contrastInfo(Long contrastId){
        Contrast contrast = new Contrast();
        contrast.setId(contrastId);
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }


    private ByteArrayResource getFile(String url) {
        Path path = Paths.get(url);
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return UUID.randomUUID() + "______" + path.getFileName().toString();
            }
        };
        return resource;
    }

    private void createResultDocx(JSONObject resObj, Contrast contrast, Child child, List<Result> list){
        Result result = new Result();
        result.setContrastId(contrast.getId());
        result.setChildId(child.getId());
        String filename = resObj.getString("filename");
        String modifiedFilename = filename.substring(0, filename.lastIndexOf(".")) + "对比结果" + filename.substring(filename.lastIndexOf("."));
        result.setResultfileName(modifiedFilename);
        result.setResultfileUrl(resultUrl + modifiedFilename);

        XWPFDocument document = new XWPFDocument();
        // 添加标题
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(modifiedFilename.substring(modifiedFilename.lastIndexOf("______") + 6));
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        addSection(document, "1.1风险点审查结果", null);
        JSONObject riskpoint_review = resObj.getJSONObject("riskpoint_review");
        JSONArray correct = riskpoint_review.getJSONArray("correct");
        StringBuilder  xiugaifengxiandian = new StringBuilder();
        for (Object correctobj : correct){
            JSONObject correctJsonObj = (JSONObject) correctobj;
            String superior_risk = correctJsonObj.getString("superior_risk");
            String subordinate_risk = correctJsonObj.getString("subordinate_risk");
            xiugaifengxiandian.append("\n上级风险点: " + superior_risk);
            xiugaifengxiandian.append("\n下载级风险点: " + subordinate_risk);
        }
        addSection(document, "1.1.1修改的风险点", xiugaifengxiandian.toString());
        JSONArray delete = riskpoint_review.getJSONArray("delete");
        StringBuilder  shanchufengxiandian = new StringBuilder();
        for (Object correctobj : delete){
            String correctJsonObj = (String) correctobj;
            shanchufengxiandian.append("\n" + correctJsonObj);
        }
        addSection(document, "1.1.2删除的风险点", shanchufengxiandian.toString());
        JSONArray add = riskpoint_review.getJSONArray("add");
        StringBuilder  zhengjiafengxiandian = new StringBuilder();
        for (Object correctobj : add){
            String correctJsonObj = (String) correctobj;
            zhengjiafengxiandian.append("\n" + correctJsonObj);
        }
        addSection(document, "1.1.3增加的风险点", zhengjiafengxiandian.toString());
        addSection(document, "1.2防控措施审查结果", null);
        JSONArray measures_review = resObj.getJSONArray("measures_review");
        for(int i = 1; i < measures_review.size(); i++){
            JSONObject o = (JSONObject) measures_review.get(i);
            addSection(document, i+ ")", null);
            addSection(document, "上級风险点:" +  o.getString("subordinate_risk"), null);
            StringBuilder  fangkongcuoshi = new StringBuilder();
            JSONArray addC = o.getJSONArray("add");
            fangkongcuoshi.append("增加防控措施:");
            for (Object a : addC){
                String correctJsonObj = (String) a;
                fangkongcuoshi.append("\n     " + correctJsonObj);
            }
            JSONArray correctC = o.getJSONArray("correct");
            fangkongcuoshi.append("\n修改防控措施:");
            for (Object c : correctC){
                JSONObject correctJsonObj = (JSONObject) c;
                String superior_measures = correctJsonObj.getString("superior_measures");
                String subordinate_measures = correctJsonObj.getString("subordinate_measures");
                xiugaifengxiandian.append("\n     上级防控措施: " + superior_measures);
                xiugaifengxiandian.append("\n     下级防控措施: " + subordinate_measures);
            }
            JSONArray delC = o.getJSONArray("delete");
            fangkongcuoshi.append("\n删除防控措施:");
            for (Object d : delC){
                String correctJsonObj = (String) d;
                fangkongcuoshi.append("\n     " + correctJsonObj);
            }
            addSection(document, "下級风险点:" + o.getString("superior_risk"), fangkongcuoshi.toString());
        }
        // 保存文档到D盘
        try {
            FileOutputStream out = new FileOutputStream(resultUrl + result.getResultfileName());
            document.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String html = convertWorkbookToString(document);
        result.setResultfileHtml(html);
        contrastMapper.insertResult(result);
        list.add(result);
    }

    private void addSection(XWPFDocument document, String sectionTitle, String sectionContent) {
        XWPFParagraph sectionTitlePara = document.createParagraph();
        sectionTitlePara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun sectionTitleRun = sectionTitlePara.createRun();
        sectionTitleRun.setBold(true);
        sectionTitleRun.setText(sectionTitle);
        sectionTitlePara.setIndentationFirstLine(500);
        XWPFParagraph sectionContentPara = document.createParagraph();
        sectionContentPara.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun sectionContentRun = sectionContentPara.createRun();
        sectionContentRun.setText(sectionContent);
    }

    private String convertWorkbookToString(XWPFDocument  document)  {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}
