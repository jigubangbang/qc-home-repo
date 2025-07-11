package com.jigubangbang.com_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.CreateReportRequest;
import com.jigubangbang.com_service.model.Report;
import com.jigubangbang.com_service.repository.CommonMapper;

@Service
public class CommonService {
    @Autowired
    private CommonMapper commonMapper;

    public void createReport(CreateReportRequest request, String reporterId) {
        // 자기 자신 신고 방지
        if (reporterId.equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }
        
        // 중복 신고 체크
        boolean isDuplicate = commonMapper.existsByReporterAndContent(
            reporterId, 
            request.getContentSubtype(), 
            request.getContentId()
        );
        
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 신고한 컨텐츠입니다.");
        }
        
        // 신고 생성
        Report report = Report.builder()
            .reporterId(reporterId)
            .targetUserId(request.getTargetUserId())
            .contentSubtype(request.getContentSubtype())
            .contentType(request.getContentType())
            .contentId(request.getContentId())
            .reasonCode(request.getReasonCode())
            .reasonText(request.getReasonText())
            .reportStatus("PENDING")
            .build();
            
        commonMapper.insertReport(report);
    }

    public String getUserProfile(String userId){
        return commonMapper.getUserProfile(userId);
    }
}
