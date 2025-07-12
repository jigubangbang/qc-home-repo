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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jigubangbang.com_service.model.ChatRoomDto;
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
            @RequestBody CreateReportRequest request,
            @RequestHeader("User-Id") String reporterId) {
        
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
            if (fileType.equals("travelinfo")){
                s3Url = s3Service.uploadFile(file, "travelinfo-images/");
            }else{
                s3Url = s3Service.uploadFile(file, "travelmate-images/");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Uploaded image successfully");
            response.put("imageUrl", s3Url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); 

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload failed");
            errorResponse.put("message", e.getMessage()); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    //채팅방 id 가져오기
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> getChatRoom(@RequestBody Map<String, Object> request) {
        try {
            String groupType = (String) request.get("groupType");
            Object groupIdObj = request.get("groupId");
            
            // groupId 타입 변환
            Long groupId = null;
            if (groupIdObj instanceof Integer) {
                groupId = ((Integer) groupIdObj).longValue();
            } else if (groupIdObj instanceof Long) {
                groupId = (Long) groupIdObj;
            } else if (groupIdObj instanceof String) {
                groupId = Long.parseLong((String) groupIdObj);
            }
            
            if (groupType == null || groupId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "groupType과 groupId는 필수입니다."));
            }
            
            // 채팅방 조회 또는 생성
            ChatRoomDto chatRoom = commonService.getChatRoom(groupType, groupId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "chatRoomId", chatRoom.getId(),
                "message", "채팅방 정보를 성공적으로 가져왔습니다."
            ));
            
        } catch (Exception e) {
            System.err.println("채팅방 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "채팅방을 불러오는데 실패했습니다."));
        }
    }
}
