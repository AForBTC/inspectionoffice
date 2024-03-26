package com.ws.inspectionoffice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ws.inspectionoffice.MobileModelException;
import com.ws.inspectionoffice.entity.Child;
import com.ws.inspectionoffice.entity.Contrast;
import com.ws.inspectionoffice.entity.Result;
import com.ws.inspectionoffice.mapper.ContrastMapper;
import com.ws.inspectionoffice.model.Body;
import com.ws.inspectionoffice.utils.JsonResponse;
import com.ws.inspectionoffice.utils.ResponseCode;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @Transactional
    @PostMapping("/contrast")
    public JsonResponse contrast(@RequestBody Body postBody) {
        Contrast contrast = new Contrast();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ArrayList<Object> objects = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        ByteArrayResource file = getFile(postBody.getFatherUrl());
        contrast.setFatherfileUrl(postBody.getFatherUrl());
        contrast.setFatherfileName(file.getFilename());
        String modifiedFilename = file.getFilename() + "对比";
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
//        ResponseEntity<String> responseEntity = restTemplate.exchange(contrastUrl, HttpMethod.POST, requestEntity, String.class);
//        String res = responseEntity.getBody();
        Path path = Paths.get("D://a.txt");
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String res =  new String(bytes);
        JSONObject resObject = JSON.parseObject(res);
        String code = resObject.getString("code");
        if(code.equals("0")){
            JSONArray dataJsonArr = resObject.getJSONArray("data");
            ArrayList<Result> results = new ArrayList<>();
            for(Object data : dataJsonArr){
                JSONObject dataObj = (JSONObject) data;
                String filename = dataObj.getString("filename");
                Optional<Child> fileOptional = childList.stream()
                        .filter(child -> child.getChildfileName().startsWith(filename))
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
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }

    @GetMapping("/info")
    public JsonResponse contrastInfo(Long contrastId){
        Contrast contrast = new Contrast();
        contrast.setId(contrastId);
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }

    @PostMapping("/addFile")
    public JsonResponse addFiles(@RequestParam("file") MultipartFile file){
        UUID uuid = UUID.randomUUID();
        File uploadDirectory = null;
        if(uploadDir.endsWith("/")){
            uploadDirectory = new File(uploadDir + uuid);
        } else{
            uploadDirectory = new File(uploadDir + "/" + uuid);
        }
        String originalFilename = file.getOriginalFilename();

        // 如果目录不存在，则创建目录
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        try {
            // 将文件写入到指定目录
            file.transferTo(new File(uploadDirectory.getAbsolutePath() + "/" + originalFilename));
            return new JsonResponse().code(ResponseCode.OK).data(uploadDirectory.getAbsolutePath() + "/" + originalFilename);
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

    private void createResultDocx(JSONObject resObj, Contrast contrast, Child child, List<Result> list){
        Result result = new Result();
        result.setContrastId(contrast.getId());
        result.setChildId(child.getId());
        result.setResultfileName("对比结果" + child.getChildfileName().substring(0, child.getChildfileName().lastIndexOf(".")) + ".docx");
        XWPFDocument document = new XWPFDocument();
        // 添加标题
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("对比结果" + child.getChildfileName());
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        addSection(document, "1.1风险点审查结果", null, null);
        JSONObject riskpoint_review = resObj.getJSONObject("riskpoint_review");
        JSONArray correct = riskpoint_review.getJSONArray("correct");
        addSection(document, "1.1.1修改的风险点", null, null);
        for (Object correctobj : correct){
            JSONObject correctJsonObj = (JSONObject) correctobj;
            String superior_risk = correctJsonObj.getString("superior_risk");
            JSONArray subordinate_risk = correctJsonObj.getJSONArray("subordinate_risk");
            addSection(document, null, "上级风险点: " + superior_risk, null);
            addSection(document, null, "下级风险点: ", null);
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
                addSection(document, "上級风险点:" +  o.getString("subordinate_risk"), null, null);
                addSection(document, "下級风险点:" + o.getString("superior_risk"), null, null);
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
                    addSection(document, null, null, "上级防控措施: " + superior_measures);
                    addSection(document, null, null, "下级防控措施: " + subordinate_measures);
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
        try {
            UUID uuid = UUID.randomUUID();
            FileOutputStream out = null;
            if(uploadDir.endsWith("/")){
                String url = resultUrl  + uuid.toString();
                File file = new File(url);
                if (!file.exists()) {
                    file.mkdirs();
                }
                out = new FileOutputStream(url + "/" +result.getResultfileName());
                result.setResultfileUrl(url + "/" +result.getResultfileName()) ;
            } else{
                String url = resultUrl + "/"  + uuid.toString();
                File file = new File(url);
                if (!file.exists()) {
                    file.mkdirs();
                }
                out = new FileOutputStream(url + "/" + result.getResultfileName());


                result.setResultfileUrl(url + "/" + result.getResultfileName());
            }
//            document.createStyles();
            document.write(out);
            out.close();
        } catch (IOException e) {
            throw new MobileModelException("服务器异常");
        }
        String html = null;
//        try {
//            html = convertWorkbookToString(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new MobileModelException("服务器异常");
//        }
        result.setResultfileHtml(html);
        contrastMapper.insertResult(result);
        list.add(result);
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
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            XHTMLOptions options = XHTMLOptions.create();
//            options.setIgnoreStylesIfUnused(false);
//            options.setFragment(true);
            XHTMLConverter.getInstance().convert(document, outputStream, null);
            String html = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);

            // 使用Jsoup库处理HTML标签，添加样式等
            Document doc = Jsoup.parse(html);
            Elements paragraphs = doc.select("p");
            for (Element paragraph : paragraphs) {
                // 根据需要添加样式，例如设置段落对齐方式等
                paragraph.attr("style", "text-align: left;");
            }
            return doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
