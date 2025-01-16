package com.encription.controller;

import com.encription.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{format}")
    public ResponseEntity<String> generateReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return new ResponseEntity<>(reportService.exportString(format), HttpStatus.OK);
    }

}
