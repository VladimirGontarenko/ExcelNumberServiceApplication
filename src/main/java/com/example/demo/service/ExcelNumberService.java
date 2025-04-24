package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

@Slf4j
@Service
public class ExcelNumberService {

    public int processFile(MultipartFile file, int n) throws IOException {
        validateFile(file);
        List<Integer> numbers = parseNumbersFromExcel(file);
        validateN(n, numbers.size());
        return findNthMinOptimized(numbers, n);
    }

    private List<Integer> parseNumbersFromExcel(MultipartFile file) throws IOException {
        List<Integer> numbers = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                processRow(row, numbers);
            }
        }

        log.info("Parsed {} numbers from file", numbers.size());
        return numbers;
    }

    private void processRow(Row row, List<Integer> numbers) {
        Cell cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            numbers.add((int) Math.floor(cell.getNumericCellValue()));
        }
    }

    public int findNthMinOptimized(List<Integer> numbers, int n) {

        PriorityQueue<Integer> pq = new PriorityQueue<>(n, Collections.reverseOrder());

        for (int num : numbers) {
            if (pq.size() < n) {
                pq.offer(num);
            } else if (num < pq.peek()) {
                pq.poll();
                pq.offer(num);
            }
        }
        log.info("min number : {}", pq.peek());
        return pq.peek();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("File validation failed: Empty file received");
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            log.error("Invalid file type detected: {} (Filename: {})", contentType, file.getOriginalFilename());
            throw new IllegalArgumentException("Invalid file format. Only XLSX allowed");
        }
    }

    private void validateN(int n, int size) {
        if (n > size) {
            String errorMessage = String.format(
                    "N validation failed: Requested N=%d, Available elements=%d",
                    n, size
            );
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
