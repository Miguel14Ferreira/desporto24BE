package com.example.desporto24.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Normalizes spreadsheet exports to a Workbook for the existing
 * preview -> mapping -> validation -> import flow.
 * Text inputs are always kept as literal cells, never Excel formulas.
 */
final class CommunityImportWorkbookReader {

    private static final int MAX_ROWS = 20_000;
    private static final int MAX_COLUMNS = 128;
    private static final int MAX_CELL_LENGTH = 32_000;

    private CommunityImportWorkbookReader() {
    }

    static Workbook open(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String name = filename == null ? "" : filename.toLowerCase(Locale.ROOT);
        if (name.endsWith(".xls") || name.endsWith(".xlsx")) {
            try (var input = new BufferedInputStream(file.getInputStream())) {
                try {
                    return WorkbookFactory.create(input);
                } catch (RuntimeException malformedWorkbook) {
                    // POI may throw unchecked exceptions for malformed Excel bytes.
                    // Normalize them to IOException so all historical import previews
                    // return the existing localized HTTP 400 read-error contract.
                    throw new IOException("Ficheiro Excel inválido", malformedWorkbook);
                }
            }
        }
        if (!name.endsWith(".csv") && !name.endsWith(".tsv")) {
            throw new IllegalArgumentException("Formato de importação não suportado");
        }

        final String text;
        try {
            text = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(file.getBytes()))
                    .toString()
                    .replaceFirst("^\\uFEFF", "");
        } catch (CharacterCodingException exception) {
            throw new IllegalArgumentException(
                    "O ficheiro CSV/TSV deve estar codificado em UTF-8", exception);
        }
        if (text.indexOf('\0') >= 0) {
            throw new IllegalArgumentException("O ficheiro contém caracteres binários inválidos");
        }

        char separator = name.endsWith(".tsv") ? '\t' : detectSeparator(text);
        Workbook workbook = new XSSFWorkbook();
        try (CSVParser parser = CSVFormat.DEFAULT.builder()
                .setDelimiter(separator)
                .setIgnoreEmptyLines(true)
                .get().parse(new StringReader(text))) {
            Sheet sheet = workbook.createSheet("Dados");
            int rowIndex = 0;
            for (CSVRecord record : parser) {
                if (rowIndex >= MAX_ROWS) {
                    throw new IllegalArgumentException("O ficheiro excede 20 000 linhas");
                }
                if (record.size() > MAX_COLUMNS) {
                    throw new IllegalArgumentException("O ficheiro excede 128 colunas");
                }
                Row row = sheet.createRow(rowIndex++);
                for (int index = 0; index < record.size(); index++) {
                    String value = record.get(index);
                    if (value.length() > MAX_CELL_LENGTH) {
                        throw new IllegalArgumentException("Uma célula excede 32 000 caracteres");
                    }
                    // createCell(String) forces a literal value, so '=' from a CSV is never evaluated.
                    row.createCell(index).setCellValue(value);
                }
            }
            return workbook;
        } catch (IOException | RuntimeException exception) {
            workbook.close();
            throw exception;
        }
    }

    private static char detectSeparator(String text) {
        boolean quoted = false;
        int commas = 0;
        int semicolons = 0;
        int tabs = 0;
        for (int i = 0; i < text.length() && i < 16_384; i++) {
            char value = text.charAt(i);
            if (value == '"') {
                if (quoted && i + 1 < text.length() && text.charAt(i + 1) == '"') {
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (!quoted) {
                if (value == '\n' || value == '\r') break;
                if (value == ',') commas++;
                if (value == ';') semicolons++;
                if (value == '\t') tabs++;
            }
        }
        if (tabs > commas && tabs > semicolons) return '\t';
        return semicolons > commas ? ';' : ',';
    }
}
