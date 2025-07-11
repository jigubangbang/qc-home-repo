package com.jigubangbang.com_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jigubangbang.com_service.model.CreateReportRequest;
import com.jigubangbang.com_service.service.CommonService;
import com.jigubangbang.com_service.service.S3Service;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/com")
public class CommonController {
    @Autowired
    private CommonService commonService;

    @Resource
    private S3Service s3Service;

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> createReport(
            @RequestBody CreateReportRequest request) {
        // String userId = authentication.getName();
                //#NeedToChange
        String reporterId="aaa";
        
        try {
            commonService.createReport(request, reporterId);
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "신고 접수가 완료되었습니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user-profile/{userId}")
    public ResponseEntity<String> getUserProfile(@PathVariable String userId){
        try{
            String profile =  commonService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/upload-image/{type}")
    public ResponseEntity<Map<String, Object>> uploadAdminImage(
        @RequestParam("file") MultipartFile file, 
        @PathVariable("type") String fileType) {
        try {
            String s3Url = null;
            if (fileType.equals("badge")){
                s3Url = s3Service.uploadFile(file, "badge-images/");
            }else{
                s3Url = s3Service.uploadFile(file, "quest-images/");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Uploaded image successfully");
            response.put("imageUrl", s3Url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Upload failed"));
        }
    }
}
