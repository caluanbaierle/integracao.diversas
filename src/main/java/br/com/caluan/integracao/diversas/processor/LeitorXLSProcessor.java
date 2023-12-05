package br.com.caluan.integracao.diversas.processor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LeitorXLSProcessor {

    public LeitorXLSProcessor() {
    }


    public void leArquivo() {

        String excelFilePath = "E:\\controle_versao\\test\\convertDados\\File\\Teste Exportação.xlsx";
        String csvFilePath = "E:\\controle_versao\\test\\convertDados\\File\\Teste Exportação arquivo.csv";

        try {


            Workbook workbook = WorkbookFactory.create(new FileInputStream(excelFilePath));
            FileWriter csvWriter = new FileWriter(csvFilePath);

            Sheet sheet = workbook.getSheetAt(0); // Assume que você está interessado na primeira planilha

            List<Integer> ordemColunas = Arrays.asList(0, 31, 7, 6, 8, 20, 18, 21, 2, 3, 5, 9, 19, 13, 17, 11, 1);

            List<List<String>> dadosTransformados = new ArrayList<>();

            for (Row row : sheet) {
                List<String> novaLinha = new ArrayList<>();
                for (int colunaLetra : ordemColunas) {
                    novaLinha.add(quoteIfContains(getCellValueAsString(row.getCell(colunaLetra)), ';'));
                }
                dadosTransformados.add(novaLinha);
            }

            for (List<String> linha : dadosTransformados) {
                for (String valor : linha) {
                    csvWriter.append(valor);
                    csvWriter.append(";");
                }
                csvWriter.append("\n");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC: {

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                return decimalFormat.format(cell.getNumericCellValue());

            }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private static int getColunaIndex(Sheet sheet, String colunaLetra) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (colunaLetra.equals(getCellValueAsString(cell))) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    private static String quoteIfContains(String value, char delimiter) {
        if (value != null && value.contains(String.valueOf(delimiter))) {
            return "\"" + value + "\"";
        }
        return value;
    }
}