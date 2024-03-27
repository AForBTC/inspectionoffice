package com.ws.inspectionoffice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.inspectionoffice.MobileModelException;
import com.ws.inspectionoffice.entity.Contrast;
import com.ws.inspectionoffice.entity.Result;
import com.ws.inspectionoffice.mapper.ContrastMapper;
import com.ws.inspectionoffice.model.Body;
import com.ws.inspectionoffice.utils.JsonResponse;
import com.ws.inspectionoffice.utils.ResponseCode;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class ContrastController {

    @Value("${contrast.url}")
    private String contrastUrl;
    @Value("${contrast.result.url}")
    private String resultUrl;
    @Value("${contrast.uploadDir}")
    private String uploadDir;
    @Value("${contrast.getfile.url}")
    private String getfileUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ContrastMapper contrastMapper;

    private String[] cities = {
            "济南",
            "青岛",
            "淄博",
            "枣庄",
            "东营",
            "烟台",
            "潍坊",
            "济宁",
            "泰安",
            "威海",
            "日照",
            "临沂",
            "德州",
            "聊城",
            "滨州",
            "菏泽"
    };

    @Transactional
    @PostMapping("/contrast")
    public JsonResponse contrast(@RequestBody Body postBody) {
        Contrast contrast = new Contrast();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ArrayList<Object> objects = new ArrayList<>();
        ByteArrayResource file = getFile(getNoReqPath(postBody.getFatherUrl()));
        contrast.setFatherfileUrl(getNoReqPath(postBody.getFatherUrl()));
        contrast.setFatherfileName(file.getFilename());
        String modifiedFilename = file.getFilename() + "对比";
        contrast.setContrastName(modifiedFilename);
        contrast.setCreateTimestamp(new Date());
        contrastMapper.insertContrast(contrast);
        body.add("file1", new FileSystemResource(new File(getNoReqPath(postBody.getFatherUrl()))));
        ArrayList<Result> results = new ArrayList<>();
        String[] arr = postBody.getChildUrlList().split(",");
        for (String childUrl : arr){
            Result result = new Result();
            HashMap<String, Object> mapC = new HashMap<>();
            ByteArrayResource fileC = getFile(getNoReqPath(childUrl));
            mapC.put("file1",fileC);
            objects.add(mapC);
            result.setChildfileUrl(getNoReqPath(childUrl));
            result.setChildfileName(fileC.getFilename());
            result.setContrastId(contrast.getId());
            results.add(result);
            body.add("file1", new FileSystemResource(new File(getNoReqPath(childUrl))));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(contrastUrl, HttpMethod.POST, requestEntity, String.class);
        String res = responseEntity.getBody();
//        Path path = Paths.get("D://a.txt");
//        byte[] bytes = new byte[0];
//        try {
//            bytes = Files.readAllBytes(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String res =  new String(bytes);
        JSONObject resObject = JSON.parseObject(res);
        String code = resObject.getString("code");
        if(code.equals("0")){
            JSONArray dataJsonArr = resObject.getJSONArray("data");
            for(Object data : dataJsonArr){
                JSONObject dataObj = (JSONObject) data;
                String filename = dataObj.getString("filename");
                Optional<Result> fileOptional = results.stream()
                        .filter(result -> result.getChildfileName().startsWith(filename))
                        .findFirst();
                createResultDocx(dataObj,contrast,fileOptional.get(), results);
            }
            return new JsonResponse().code(ResponseCode.OK).data(results);
        } else {
            throw new MobileModelException("服务器异常");
        }
    }

    @GetMapping("/history")
    public JsonResponse history(){
        Contrast contrast = new Contrast();
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        for (Contrast contrastC : contrasts){
            contrastC.setFatherfileName(null);
            contrastC.setFatherfileUrl(null);
            contrastC.setResultList(null);
        }
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }

    @GetMapping("/info")
    public JsonResponse contrastInfo(Long contrastId) throws IOException {
        Contrast contrast = new Contrast();
        contrast.setId(contrastId);
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        if(contrasts != null && contrasts.size() > 0){
            setReqUrl(contrasts.get(0));
            for(Result result : contrasts.get(0).getResultList()){
                // 读取txt文件内容
                StringBuilder txtContent = new StringBuilder();
                BufferedReader txtReader = new BufferedReader(new FileReader(result.getResultfileHtmlUrl()));
                String line;
                while ((line = txtReader.readLine()) != null) {
                    txtContent.append(line);
                    txtContent.append("\n"); // 添加换行符
                }
                txtReader.close();
                String txtString = txtContent.toString();
                result.setResultfileHtml(txtString);
            }
        }
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts.get(0));
    }

    @PostMapping("/addFile")
    public JsonResponse addFiles(@RequestParam("file") MultipartFile file){
        UUID uuid = UUID.randomUUID();
        File uploadDirectory = null;
        uploadDirectory = new File(uploadDir + "/" + uuid);
        String originalFilename = file.getOriginalFilename();

        // 如果目录不存在，则创建目录
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        try {
            // 将文件写入到指定目录
            file.transferTo(new File(uploadDirectory.getAbsolutePath() + "/" + originalFilename));
            return new JsonResponse().code(ResponseCode.OK).data(getfileUrl + uploadDir + "/" + uuid + "/" + originalFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResponse().code(ResponseCode.ERROR_SERVER_ERROR);
        }
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
                return path.getFileName().toString();
            }
        };
        return resource;
    }

    private String getNoReqPath(String path){
        return path.substring(path.indexOf(uploadDir));
    }

    private void createResultDocx(JSONObject resObj, Contrast contrast, Result result, List<Result> list){
        String fatherfileName = contrast.getFatherfileName();
        String upStr = null;
        if(fatherfileName.contains("_")){
            String substring = fatherfileName.substring(fatherfileName.lastIndexOf("_"));
            upStr = getFXName(substring);
            if(upStr == null){
                upStr = getFXName(fatherfileName);
                if (upStr == null){
                    upStr = "省";
                }
            }
        } else {
            upStr = getFXName(fatherfileName);
            if (upStr == null){
                upStr = "省";
            }
        }
        String childfileName = result.getChildfileName();
        String downStr = null;
        if(childfileName.contains("_")){
            String substring = childfileName.substring(childfileName.lastIndexOf("_"));
            downStr = getFXName(substring);
            if(downStr == null){
                downStr = getFXName(childfileName);
                if (upStr == null){
                    downStr = "下级";
                }
            }
        } else {
            downStr = getFXName(childfileName);
            if (downStr == null){
                downStr = "下级";
            }
        }
        result.setResultfileName("对比结果" + result.getChildfileName().substring(0, result.getChildfileName().lastIndexOf(".")) + ".docx");
        XWPFDocument document = new XWPFDocument();
        // 添加标题
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("对比结果" + result.getChildfileName());
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        addSection(document, "1.1风险点审查结果", null, null);
        String riskpointTotal = resObj.getString("riskpoint_quantity");
        result.setRiskpointTotal(Integer.parseInt(riskpointTotal));
        JSONObject riskpoint_review = resObj.getJSONObject("riskpoint_review");
        JSONArray correct = riskpoint_review.getJSONArray("correct");
        addSection(document, "1.1.1修改的风险点", null, null);
        for (Object correctobj : correct){
            JSONObject correctJsonObj = (JSONObject) correctobj;
            String superior_risk = correctJsonObj.getString("superior_risk");
            JSONArray subordinate_risk = correctJsonObj.getJSONArray("subordinate_risk");
            addSection(document, null, upStr + "风险点: " + superior_risk, null);
            addSection(document, null, downStr + "风险点: ", null);
            for (Object s : subordinate_risk){
                String sStr = (String) s;
                addSection(document, null, null, sStr);
            }
        }
        JSONArray delete = riskpoint_review.getJSONArray("delete");
        addSection(document, "1.1.2删除的风险点", null, null);
        for (Object correctobj : delete){
            String correctJsonObj = (String) correctobj;
            addSection(document, null, correctJsonObj, null);

        }
        JSONArray add = riskpoint_review.getJSONArray("add");
        addSection(document, "1.1.3增加的风险点", null, null);
        for (Object correctobj : add){
            String correctJsonObj = (String) correctobj;
            addSection(document, null, correctJsonObj, null);

        }
        addSection(document, "1.2防控措施审查结果", null, null);
        JSONArray measures_review = resObj.getJSONArray("measures_review");
        if(measures_review != null && measures_review.size() > 0){
            for(int i = 0; i < measures_review.size(); i++){
                JSONObject o = (JSONObject) measures_review.get(i);
                addSection(document, i+ ")", null, null);
                addSection(document, upStr + "风险点: "+  o.getString("subordinate_risk"), null, null);
                addSection(document, downStr + "风险点: "+ o.getString("superior_risk"), null, null);
                JSONArray addC = o.getJSONArray("add");
                addSection(document, null, "增加防控措施:", null);
                for (Object a : addC){
                    String correctJsonObj = (String) a;
                    addSection(document, null, null, correctJsonObj);
                }
                JSONArray correctC = o.getJSONArray("correct");
                addSection(document, null, "修改防控措施:", null);
                for (Object c : correctC){
                    JSONObject correctJsonObj = (JSONObject) c;
                    String superior_measures = correctJsonObj.getString("superior_measures");
                    String subordinate_measures = correctJsonObj.getString("subordinate_measures");
                    addSection(document, null, null, upStr + "防控措施: " + superior_measures);
                    addSection(document, null, null, downStr + "防控措施: " + subordinate_measures);
                }
                JSONArray delC = o.getJSONArray("del");
                addSection(document, null, "删除防控措施:", null);
                for (Object d : delC){
                    String correctJsonObj = (String) d;
                    addSection(document, null, null, correctJsonObj);
                }
            }
        }

        // 保存文档到D盘
        String html = null;
        try {
            UUID uuid = UUID.randomUUID();
            FileOutputStream out = null;
            String url = resultUrl + "/"  + uuid.toString();
            File file = new File(url);
            if (!file.exists()) {
                file.mkdirs();
            }
            out = new FileOutputStream(url + "/" + result.getResultfileName());
            result.setResultfileUrl(url + "/" + result.getResultfileName());
            document.write(out);
            out.close();
            html = convertWorkbookToString(document);
            String txtFileName = result.getResultfileName().replace(".docx", ".txt");
            String txtFilePath = url + "/" + txtFileName;
            FileWriter txtWriter = new FileWriter(txtFilePath);
            txtWriter.write(html);
            txtWriter.close();
            result.setResultfileHtmlUrl(txtFilePath);
        } catch (IOException e) {
            throw new MobileModelException("服务器异常");
        }
        contrastMapper.insertResult(result);
        result.setResultfileHtml(html);
        result.setResultfileUrl(getfileUrl + result.getResultfileUrl());
        result.setChildfileUrl(getfileUrl + result.getChildfileUrl());
    }

    private String getFXName(String substring){
        String upStr = null;
        if(substring.contains("区县")){
            upStr = "区县";
        }else if(substring.contains("市公司")){
            upStr = "市";
        } else{
            boolean containsCity = false; // 标志位，表示是否包含城市名称
            for (String city : cities) {
                if (substring.contains(city)) {
                    containsCity = true;
                    break;
                }
            }
            if (containsCity) {
                upStr = "市";
            } else {
                if(substring.contains("政企")){
                    upStr = "政企 ";
                } else if(substring.contains("省")){
                    upStr = "省";
                }
            }
        }
        return upStr;
    }

    private void addSection(XWPFDocument document, String sectionTitle, String sectionContent, String sectionContent2) {
        if (sectionTitle != null && !sectionTitle.equals("")) {
            XWPFParagraph sectionTitlePara = document.createParagraph();
            sectionTitlePara.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun sectionTitleRun = sectionTitlePara.createRun();
            sectionTitleRun.setText(sectionTitle);
        }
        if (sectionContent != null && !sectionContent.equals("")) {
            XWPFParagraph sectionContentPara = document.createParagraph();
            sectionContentPara.setAlignment(ParagraphAlignment.LEFT);
            sectionContentPara.setIndentationFirstLine(400);
            XWPFRun sectionContentRun = sectionContentPara.createRun();
            sectionContentRun.setText(sectionContent);
        }
        if (sectionContent2 != null && !sectionContent2.equals("")) {
            XWPFParagraph sectionContentPara = document.createParagraph();
            sectionContentPara.setAlignment(ParagraphAlignment.LEFT);
            sectionContentPara.setIndentationFirstLine(800);
            XWPFRun sectionContentRun = sectionContentPara.createRun();
            sectionContentRun.setText(sectionContent2);
        }

    }

    private String convertWorkbookToString(XWPFDocument document) throws IOException {
        StringBuilder html = new StringBuilder("<html><head></head><body>");
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String paragraphHtml = convertParagraphToHtml_p(paragraph);
            html.append(paragraphHtml);
        }
        html.append("</body></html>");
        return html.toString();
    }

    private static String convertParagraphToHtml_p(XWPFParagraph paragraph) {
        StringBuilder paragraphHtml = new StringBuilder("<p>");

        // Apply indentation if present
        int indentationFirstLine = paragraph.getIndentationFirstLine();
        if (indentationFirstLine > 0) {
            // Convert indentation from twips to pixels (assuming 1 twip = 1/20 of a point)
            int indentationPixels = indentationFirstLine / 20;
            paragraphHtml.append("<span style=margin-left:").append(indentationPixels).append("px;>");
        }

        // Append text of the paragraph
        paragraphHtml.append(paragraph.getText());

        // Close indentation span if present
        if (indentationFirstLine > 0) {
            paragraphHtml.append("</span>");
        }

        paragraphHtml.append("</p>");
        return paragraphHtml.toString();
    }

    private void setReqUrl(Contrast contrast){
        contrast.setFatherfileUrl(getfileUrl + contrast.getFatherfileUrl());
        List<Result> resultList = contrast.getResultList();
        for(Result result : resultList){
            result.setChildfileUrl(getfileUrl + result.getChildfileUrl());
            result.setResultfileUrl(getfileUrl + result.getResultfileUrl());
        }
    }

}
