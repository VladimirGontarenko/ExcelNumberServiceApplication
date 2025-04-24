package com.example.demo.controller;


import com.example.demo.service.ExcelNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "main-methods")
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MinNumberController {
    private final ExcelNumberService numberService;

    @PostMapping(value = "/find-nth-min", consumes = "multipart/form-data")
    @Operation(summary = "Find Nth minimum number from XLSX file")
    public ResponseEntity<Integer> findNthMin(
            @Parameter(description = "XLSX file with numbers in first column")
            @RequestPart("file") MultipartFile file,

            @Schema(description = "N-th minimum to find (1-based)", example = "3")
            @RequestParam("n") @Min(1) int n) throws IOException {

        return ResponseEntity.ok(numberService.processFile(file, n));
    }
}
