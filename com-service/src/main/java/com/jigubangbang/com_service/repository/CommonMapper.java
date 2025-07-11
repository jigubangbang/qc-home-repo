package com.jigubangbang.com_service.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.Report;

@Mapper
public interface CommonMapper {

    void insertReport(Report report);
    
    boolean existsByReporterAndContent(
        @Param("reporterId") String reporterId,
        @Param("contentSubtype") String contentSubtype,
        @Param("contentId") Long contentId
    );

    String getUserProfile(String userId);
}
