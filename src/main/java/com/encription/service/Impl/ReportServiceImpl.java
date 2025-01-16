package com.encription.service.Impl;

import com.encription.CipherKeepApplication;
import com.encription.service.ReportService;
import com.encription.service.SecretService;
import com.encription.dto.SecretResponseDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private SecretService secretService;

    public String exportString(String reportFormat) {
        CipherKeepApplication.logger.info("Starting report generation in {} format", reportFormat);
        try {
            String path = "C:\\Users\\vikatiyar\\Desktop\\Reports";

            List<SecretResponseDTO> allSecrets =secretService.getAllSecret();
            CipherKeepApplication.logger.debug("Retrieved {} secret(s) to include in the report.", allSecrets.size());
            //Load File and compile it
            File file= ResourceUtils.getFile("classpath:secrets_details.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(allSecrets);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Vikas Katiyar");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            if (reportFormat.equalsIgnoreCase("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\secrets.html");
                CipherKeepApplication.logger.info("Report generated in HTML format at: {}", path + "\\secrets.html");
            }
            if (reportFormat.equalsIgnoreCase("pdf")) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\secrets.pdf");
                CipherKeepApplication.logger.info("Report generated in PDF format at: {}", path + "\\secrets.pdf");
            }

            return "Report generated in path : " + path;
        } catch (FileNotFoundException | JRException e) {
            throw new RuntimeException(e);
        }

    }
}
