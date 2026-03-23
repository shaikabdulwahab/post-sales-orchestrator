package com.mod.cx.post_sales_orchestrator.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class CsvService {

    public byte[] generateTemplate(String[] headers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.writeNext(headers);
        } catch (Exception e) {
            log.error("Error generating csv template", e);
            throw new RuntimeException("Failed to generate csv template");
        }

        return out.toByteArray();
    }

    public <T> List<T> parseCsv(InputStream inputStream, Class<T> clazz) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            log.error("Error parsing csv", e);
            throw new RuntimeException("Failed to parse csv");
        }
    }
}
