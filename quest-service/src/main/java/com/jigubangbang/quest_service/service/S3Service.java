package com.jigubangbang.quest_service.service;


import org.springframework.beans.factory.annotation.Value; // Spring 설정값 주입용
import org.springframework.stereotype.Service; // Spring 서비스 빈으로 등록
import org.springframework.web.multipart.MultipartFile; // 웹에서 업로드된 파일 처리용

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID; // 파일명 중복 방지용

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}") // S3 버킷 이름 주입
    private String bucketName;

    @Value("${aws.region}") // AWS 리전 주입
    private String regionString;

    private S3Client s3Client;

    @PostConstruct // Spring 빈 초기화 시 실행
    public void init() {
        Region region = Region.of(regionString);
        this.s3Client = S3Client.builder()
                               .region(region)
                               .build();
    }


// 웹에서 업로드된 MultipartFile을 S3에 업로드하고, 업로드된 파일의 '공개 S3 URL'을 반환
// file: 사용자가 웹에서 업로드한 MultipartFile 객체.
// s3Folder: S3 버킷 내에 파일을 저장할 논리적 폴더 경로 (예: "feed-images/", "profile-pictures/"). 반드시 '/'로 끝나야 합니다.
// return: 업로드된 파일의 공개 S3 URL.

    public String uploadFile(MultipartFile file, String s3Folder) throws IOException, S3Exception {
        String originalFilename = file.getOriginalFilename();
        if (!s3Folder.endsWith("/")) { // 폴더 경로 보정
            s3Folder += "/";
        }
        String s3Key = s3Folder + UUID.randomUUID().toString() + "_" + originalFilename; // S3 객체 키 생성

        PutObjectRequest putObjectRequest = PutObjectRequest.builder() // PutObject 요청 빌드
                .bucket(bucketName)
                .key(s3Key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize())); // S3 업로드 실행

        System.out.println("S3 파일 업로드 성공. 객체 키: " + s3Key);
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, regionString, s3Key); // 공개 URL 반환
    }

    @PreDestroy // Spring 빈 소멸 시 실행
    public void destroy() {
        if (s3Client != null) {
            s3Client.close(); // S3 클라이언트 리소스 해제
        }
    }
}

