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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class ContrastController {

    @Value("${contrast.url}")
    private String contrastUrl;
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
//        Path path = Paths.get("D://b.txt");
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
                createResultexcel(dataObj,contrast,fileOptional.get());
                createResultDocx(dataObj,contrast,fileOptional.get());
            }
            contrast.setResultList(results);
            createZip(contrast);
            return new JsonResponse().code(ResponseCode.OK).data(contrast);
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
            contrastC.setZipUrl(getfileUrl + contrastC.getZipUrl());
        }
        return  new JsonResponse().code(ResponseCode.OK).data(contrasts);
    }

    @PostMapping("/del")
    public JsonResponse delete(@RequestBody Map map){
        Integer contrastIdI = (Integer) map.get("id");
        Long contrastId = contrastIdI.longValue();
        Contrast contrast = new Contrast();
        contrast.setId(contrastId);
        Contrast contrastC = contrastMapper.selectContrastList(contrast).get(0);
        contrastMapper.deleteContrastById(contrastId);
        try {
            File file = new File(uploadDir + contrastC.getFatherfileUrl());
            file.delete();
            List<Result> resultList = contrastC.getResultList();
            File zfile = new File(uploadDir + contrastC.getZipUrl());
            zfile.delete();
            for(Result result: resultList){
                File Cfile = new File(uploadDir + result.getChildfileUrl());
                Cfile.delete();
                File Rfile = new File(uploadDir + result.getResultfileUrl());
                Rfile.delete();
                File Hfile = new File(uploadDir + result.getResultfileHtmlUrl());
                Hfile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new MobileModelException("删除失败，服务器异常");
        }
        return  new JsonResponse().code(ResponseCode.OK);
    }

    @GetMapping("/info")
    public JsonResponse contrastInfo(@RequestParam Long contrastld) throws IOException {
        Contrast contrast = new Contrast();
        contrast.setId(contrastld);
        List<Contrast> contrasts = contrastMapper.selectContrastList(contrast);
        if(contrasts != null && contrasts.size() > 0){
            setReqUrl(contrasts.get(0));
            for(Result result : contrasts.get(0).getResultList()){
                // 读取txt文件内容
                StringBuilder txtContent = new StringBuilder();
                BufferedReader txtReader = new BufferedReader(new FileReader(uploadDir + result.getResultfileHtmlUrl()));
                String line;
                while ((line = txtReader.readLine()) != null) {
                    txtContent.append(line);
                    txtContent.append("\n"); // 添加换行符
                }
                txtReader.close();
                String txtString = txtContent.toString();
                result.setResultfileHtml(txtString);
            }
            return  new JsonResponse().code(ResponseCode.OK).data(contrasts.get(0));
        } else {
            return  new JsonResponse().code(ResponseCode.OK);
        }
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
            return new JsonResponse().code(ResponseCode.OK).data(getfileUrl + "/" + uuid + "/" + originalFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResponse().code(ResponseCode.ERROR_SERVER_ERROR);
        }
    }



    private ByteArrayResource getFile(String url) {
        Path path = Paths.get(uploadDir + url);
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
        return path.substring(path.indexOf(getfileUrl) + getfileUrl.length());
    }

    // 计算文本的高度
    private short  calculateTextHeight(String text, int width, Workbook workbook) {
        int length = text.length();
            int n = 0;
        if(length <= 27){
            n = 1;
        } else{
            n = (int) Math.ceil((double) length / 27);
        }

        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = text.indexOf("\n", lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += "\n".length();
            }
        }
        n = n + count;


        Font font = workbook.createFont();
        font.setFontName("Arial"); // 您可以根据需要更改字体
        font.setFontHeightInPoints((short) 12);
        java.awt.Font awtFont = new java.awt.Font(font.getFontName(), java.awt.Font.PLAIN, font.getFontHeightInPoints());
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(text, awtFont, frc);
        Rectangle2D bounds = layout.getBounds();
        double textWidth = bounds.getWidth();
        double lines = Math.ceil(textWidth / 200);
        double lineHeight = bounds.getHeight();
        return (short) (n * 16 * 20);
    }

    private void createResultexcel(JSONObject resObj, Contrast contrast, Result result){
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
        result.setResultfileName("对比结果" + result.getChildfileName().substring(0, result.getChildfileName().lastIndexOf(".")) + ".xlsx");
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建第一个表格（风险点对比）
            Sheet sheet1 = workbook.createSheet("风险点对比");
            // 设置表头样式
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setWrapText(true);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true); // 表头加粗
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // 设置水平居中
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            // 设置普通单元格样式 非居中
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            CellStyle cellStyleC = workbook.createCellStyle();
            cellStyleC.setWrapText(true);
            cellStyleC.setBorderTop(BorderStyle.THIN);
            cellStyleC.setBorderBottom(BorderStyle.THIN);
            cellStyleC.setBorderLeft(BorderStyle.THIN);
            cellStyleC.setBorderRight(BorderStyle.THIN);
            cellStyleC.setAlignment(HorizontalAlignment.CENTER); // 设置水平居中
            cellStyleC.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中
            // 创建表头行
            Row headerRow1 = sheet1.createRow(0);
            String[] columns1 = {"增加风险点", "删除风险点"};
            for (int i = 0; i < columns1.length; i++) {
                Cell cell = headerRow1.createCell(i);
                cell.setCellValue(columns1[i]);
                cell.setCellStyle(headerCellStyle);
            }
            // 添加框线
            for (int i = 0; i < columns1.length; i++) {
                sheet1.autoSizeColumn(i);
                sheet1.setColumnWidth(i, 13000);
            }
            JSONObject riskpoint_review = resObj.getJSONObject("riskpoint_review");
            JSONArray add = riskpoint_review.getJSONArray("add");
            JSONArray delete = riskpoint_review.getJSONArray("delete");
            Integer h = (add.size() > delete.size()) ? add.size() : delete.size();
            for (int i = 0; i < h; i++) {
                Row row = sheet1.createRow(i + 1);
                // 设置行高自适应
                Cell cell = row.createCell(0);
                Short ii = null;
                if(i < add.size()){
                    ii = calculateTextHeight(add.getString(i), 13000, workbook);
                    StringBuilder stringBuilder = new StringBuilder(add.getString(i));
                    int insertions = add.getString(i).length() / 26;
                    for (int ic = 1; ic <= insertions; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index, '\n');
                    }
                    cell.setCellValue(stringBuilder.toString());
                } else {
                    cell.setCellValue("无");
                }
                cell.setCellStyle(cellStyleC); // 非表

                Cell cell2 = row.createCell(1);
                Short i2 = null;
                if(i < delete.size()){
                    i2 = calculateTextHeight(delete.getString(i), 13000, workbook);
                    StringBuilder stringBuilder = new StringBuilder(delete.getString(i));
                    int insertionsA = delete.getString(i).length() / 26;
                    for (int ic = 1; ic <= insertionsA; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index , '\n');
                    }
                    cell2.setCellValue(stringBuilder.toString());
                } else {
                    cell2.setCellValue("无");
                }
                cell2.setCellStyle(cellStyle); //
                if(ii == null && i2 != null){
                    row.setHeight(i2);
                } else if(ii != null && i2 == null){
                    row.setHeight(ii);
                } else if(ii !=null && i2 != null){
                    Short h2 = (ii > i2) ? ii : i2;
                    row.setHeight(h2);
                }
            }

            // 创建第二个表格（防控措施对比）
            Sheet sheet2 = workbook.createSheet("防控措施对比");
            // 创建表头行
            Row headerRow2 = sheet2.createRow(0);
            String[] columns2 = {upStr + "风险点", upStr + "防控措施", downStr + "风险点", downStr + "市防控措施"};
            for (int i = 0; i < columns2.length; i++) {
                Cell cell = headerRow2.createCell(i);
                cell.setCellValue(columns2[i]);
                cell.setCellStyle(headerCellStyle);
            }
            // 添加框线
            for (int i = 0; i < columns2.length; i++) {
                sheet2.autoSizeColumn(i);
                if(i == 0 || i == 2){
                    sheet2.setColumnWidth(i, 8000);
                } else {
                    sheet2.setColumnWidth(i, 13000);
                }
            }
            JSONArray measures_review = resObj.getJSONArray("measures_review");
            for (int i = 0; i < measures_review.size(); i++) {
                Row row = sheet2.createRow(i + 1);
                // 设置行高自适应
//                row.setHeight((short) -1);
                JSONObject o = measures_review.getJSONObject(i);
                String superior_risk = o.getString("superior_risk");
                String superior_measures = o.getString("superior_measures");
                short i11 = calculateTextHeight(superior_measures, 13000, workbook);
                String subordinate_risk = o.getString("subordinate_risk");
                String subordinate_measures = o.getString("subordinate_measures");
                short i12 = calculateTextHeight(subordinate_measures, 13000, workbook);
                if(i11 >= i12){
                    row.setHeight(i11);
                }else{
                    row.setHeight(i12);
                }
                JSONArray addC = o.getJSONArray("add");
                JSONArray deleteC = o.getJSONArray("del");

                Cell cell1 = row.createCell(0);
                StringBuilder stringBuilderA = new StringBuilder(superior_risk);
                int insertionsA = superior_risk.length() / 26;
                for (int ic = 1; ic <= insertionsA; ic++) {
                    int index = 26 * ic;
                    stringBuilderA.insert(index , '\n');
                }
                cell1.setCellValue(stringBuilderA.toString());
                cell1.setCellStyle(cellStyleC); // 非表头部分使用普通单元格样式

                Cell cell2 = row.createCell(1);
                if(deleteC !=null && deleteC.size() >= 1){
                    Font font = workbook.createFont();
                    font.setColor(IndexedColors.BLUE.getIndex()); // 设
                    StringBuilder stringBuilder = new StringBuilder(superior_measures);
                    int insertions = superior_measures.length() / 26;
                    for (int ic = 1; ic <= insertions; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index , '\n');
                    }
                    RichTextString richText = new XSSFRichTextString(stringBuilder.toString()); // 设置部分文字的样式
                    for(Object d : deleteC){
                        String str = (String) d;
                        int i1 = superior_measures.indexOf(str);
                        if(i1 == -1){
                            i1 = superior_measures.replaceAll("\\s+", "").indexOf(str);
                        }
                        richText.applyFont(i1, i1 + str.length(), font); //
                    }

                    cell2.setCellValue(richText);
                } else {
                    StringBuilder stringBuilder = new StringBuilder(superior_measures);
                    int insertions = superior_measures.length() / 26;
                    for (int ic = 1; ic <= insertions; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index , '\n');
                    }
                    cell2.setCellValue(stringBuilder.toString());
                }
                cell2.setCellStyle(cellStyle); // 非表头部分使用普通单元格样式
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(subordinate_risk);
                cell3.setCellStyle(cellStyleC); // 非表头部分使用普通单元格样式

                Cell cell4 = row.createCell(3);
                if(addC !=null && addC.size() >= 1){
                    Font fontC = workbook.createFont();
                    fontC.setColor(IndexedColors.RED.getIndex()); // 设
                    StringBuilder stringBuilder = new StringBuilder(subordinate_measures);
                    int insertions = subordinate_measures.length() / 26;
                    for (int ic = 1; ic <= insertions; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index , '\n');
                    }
                    RichTextString richTextC = new XSSFRichTextString(stringBuilder.toString()); // 设置部分文字的样式
                    for(Object a : addC){
                        String str = (String) a;
                        int i1 = subordinate_measures.indexOf(str);
                        if(i1 == -1) {
                            i1 = subordinate_measures.replaceAll("\\s+", "").indexOf(str);
                        }
                        richTextC.applyFont(i1, i1 + str.length(), fontC); //
                    }

                    cell4.setCellValue(richTextC);
                } else {
                    StringBuilder stringBuilder = new StringBuilder(subordinate_measures);
                    int insertions = subordinate_measures.length() / 26;
                    for (int ic = 1; ic <= insertions; ic++) {
                        int index = 26 * ic;
                        stringBuilder.insert(index , '\n');
                    }
                    cell4.setCellValue(stringBuilder.toString());
                }
                cell4.setCellStyle(cellStyle); // 非表头部分使用普通单元格样式
            }
            FileOutputStream out = null;
            String childurl = result.getChildfileUrl();
            out = new FileOutputStream((uploadDir + childurl).replaceAll(childfileName, result.getResultfileName()));
            result.setResultfileUrl(childurl.replaceAll(childfileName, result.getResultfileName()));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MobileModelException("服务器异常");
        }
    }

    private void createResultDocx(JSONObject resObj, Contrast contrast, Result result){
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
        JSONArray delete = riskpoint_review.getJSONArray("delete");
        addSection(document, "1.1.1删除的风险点", null, null);
        for (Object correctobj : delete){
            String correctJsonObj = (String) correctobj;
            addSection(document, null, correctJsonObj, null);

        }
        JSONArray add = riskpoint_review.getJSONArray("add");
        addSection(document, "1.1.2增加的风险点", null, null);
        for (Object correctobj : add){
            String correctJsonObj = (String) correctobj;
            addSection(document, null, correctJsonObj, null);

        }
        addSection(document, "1.2防控措施审查结果", null, null);
        JSONArray measures_review = resObj.getJSONArray("measures_review");
        if(measures_review != null && measures_review.size() > 0){
            for(int i = 0; i < measures_review.size(); i++){
                JSONObject o = (JSONObject) measures_review.get(i);
                addSection(document, (i + 1) + ")", null, null);
//                addSection(document, upStr + "防控措施: ", o.getString("superior_measures"), null);
//                addSection(document, downStr + "防控措施: ", o.getString("subordinate_measures"), null);
                addSection(document, upStr + "风险点: "+  o.getString("superior_risk"), null, null);
                addSection(document, downStr + "风险点: "+ o.getString("subordinate_risk"), null, null);
                JSONArray addC = o.getJSONArray("add");
                addSection(document, null, "增加防控措施:", null);
                for (Object a : addC){
                    String correctJsonObj = (String) a;
                    addSection(document, null, null, correctJsonObj);
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
            html = convertWorkbookToString(document);
            String txtFilePath = result.getResultfileUrl().replace(".xlsx", ".txt");
//            String noReqPath = getNoReqPath(txtFilePath);
            FileWriter txtWriter = new FileWriter(uploadDir + txtFilePath);
            txtWriter.write(html);
            txtWriter.close();
            result.setResultfileHtmlUrl(txtFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MobileModelException("服务器异常");
        }
        contrastMapper.insertResult(result);
        result.setResultfileHtml(html);
        result.setResultfileHtmlUrl(getfileUrl + result.getResultfileHtmlUrl());
        result.setResultfileUrl(getfileUrl + result.getResultfileUrl());
        result.setChildfileUrl(getfileUrl + result.getChildfileUrl());
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

    private void createZip(Contrast contrast){
        List<String> filePath = new ArrayList<>();
        for (Result result : contrast.getResultList()){
            filePath.add(uploadDir + getNoReqPath(result.getResultfileUrl()));
        }
        String[] filePathArr = filePath.toArray(new String[0]);
        String zipPath = uploadDir + contrast.getFatherfileUrl().replace(".xlsx", ".zip").replace(".xls", ".zip");
        contrast.setZipUrl(contrast.getFatherfileUrl().replace(".xlsx", ".zip").replace(".xls", ".zip"));
        contrastMapper.updateContrast(contrast);
        try {
            FileOutputStream fos = new FileOutputStream(zipPath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String fileToCompress : filePathArr) {
                File file = new File(fileToCompress);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    private void setReqUrl(Contrast contrast){
        contrast.setFatherfileUrl(getfileUrl + contrast.getFatherfileUrl());
        contrast.setZipUrl(getfileUrl + contrast.getZipUrl());
        List<Result> resultList = contrast.getResultList();
        for(Result result : resultList){
            result.setChildfileUrl(getfileUrl + result.getChildfileUrl());
            result.setResultfileUrl(getfileUrl + result.getResultfileUrl());
        }
    }

}
