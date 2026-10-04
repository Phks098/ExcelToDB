package org.example;

import org.apache.poi.ss.usermodel.*;
import org.example.controller.ExcelDbController;

import javax.swing.*;
import java.io.File;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        // 파일 위치 주소
        String fileURL = "C:/Users/phks0/Downloads/5.진로취업지원 프로그램 수행 이력 DB.xlsx";
        ExcelInsertFrame excelInsertFrame = new ExcelInsertFrame();
        File file = new File(fileURL);
        DataFormatter formatter = new DataFormatter();


        // TODO(Jusung): [테스트] 읽기 동작 확인 용 출력 - DB 저장 로직 완성 후 삭제 예정
        try(Workbook workbook = WorkbookFactory.create(file)){
            Sheet sheet = workbook.getSheetAt(0);

            for(Row row : sheet) {
                if(row.getRowNum() == 0 ) continue;

                String name  = formatter.formatCellValue(row.getCell(0)); // A열
                String price = formatter.formatCellValue(row.getCell(1)); // B열
                String date  = formatter.formatCellValue(row.getCell(2)); // C열
                if(!name.isEmpty() && !price.isEmpty() && !date.isEmpty()) {
                    System.out.println(name + " / " + price + " / " + date);
                }
            }

        } catch (Exception e) {
            System.err.println("엑셀 파일을 읽는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ExcelInsertFrame view = new ExcelInsertFrame();
            new ExcelDbController(view);
            view.setVisible(true);
        });
    }
}