package com.example.orderbook.reporting;

import com.example.orderbook.dtos.ExecutionDTO;
import com.example.orderbook.dtos.ExecutionHistoryDTO;
import com.example.orderbook.dtos.OrderEntryDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ExecutionReportPDFGenerator {

    public byte[] generateReport(List<ExecutionHistoryDTO> executionHistoryDTOList) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        Paragraph paragraph1 = new Paragraph();
        paragraph1.add("Execution history\n");
        document.add(paragraph1);

        PdfPTable executionHistoryTable = getTableWithHeader(new String[] {"executionId", "orderId", "filledQuantity", "filledPrice"});
        addExecutionHistoryRows(executionHistoryTable, executionHistoryDTOList);
        document.add(executionHistoryTable);

        Paragraph paragraph2 = new Paragraph();
        paragraph2.add("Orders\n");
        document.add(paragraph2);

        PdfPTable orderTable = getTableWithHeader(new String[] {"orderId", "financialInstrumentId", "quantity", "availableQuantity", "entryDate", "orderType", "price", "status"});
        addOrderRows(orderTable, executionHistoryDTOList);
        document.add(orderTable);

        Paragraph paragraph3 = new Paragraph();
        paragraph3.add("Execution\n");
        document.add(paragraph3);


        PdfPTable executionTable = getTableWithHeader(new String[] {"executionId", "financialInstrumentId", "quantity", "price", "executionType"});
        addExecutionRows(executionTable, executionHistoryDTOList);
        document.add(executionTable);

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private PdfPTable getTableWithHeader(String[] columnNames) {
        PdfPTable table = new PdfPTable(columnNames.length);
        Stream.of(columnNames)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
        return table;
    }

    private void addExecutionHistoryRows(PdfPTable table, List<ExecutionHistoryDTO> executionHistoryDTOList) {
        executionHistoryDTOList.forEach(executionHistoryDTO -> {
            table.addCell(executionHistoryDTO.getExecution().getId().toString());
            table.addCell(executionHistoryDTO.getOrderEntry().getId().toString());
            table.addCell(executionHistoryDTO.getFilledQuantity().toString());
            table.addCell(executionHistoryDTO.getFilledQuantity().toString());
        });

    }

    private void addOrderRows(PdfPTable table, List<ExecutionHistoryDTO> executionHistoryDTOList) {
        List<OrderEntryDTO> orderEntryDTOList = executionHistoryDTOList.stream().map(ExecutionHistoryDTO::getOrderEntry).distinct().toList();
        orderEntryDTOList.forEach(orderEntry -> {
            table.addCell(orderEntry.getId().toString());
            table.addCell(orderEntry.getFinancialInstrumentId());
            table.addCell(orderEntry.getQuantity().toString());
            table.addCell(orderEntry.getAvailableQuantity().toString());
            table.addCell(orderEntry.getEntryDate().toString());
            table.addCell(orderEntry.getOrderType().toString());
            table.addCell(orderEntry.getPrice().toString());
            table.addCell(orderEntry.getStatus().toString());
        });
    }

    private void addExecutionRows(PdfPTable table, List<ExecutionHistoryDTO> executionHistoryDTOList) {
        List<ExecutionDTO> executionDTOList = executionHistoryDTOList.stream().map(ExecutionHistoryDTO::getExecution).distinct().toList();
        executionDTOList.forEach(execution -> {
            table.addCell(execution.getId().toString());
            table.addCell(execution.getFinancialInstrumentId());
            table.addCell(execution.getQuantity().toString());
            table.addCell(execution.getPrice().toString());
            table.addCell(execution.getExecutionType().toString());
        });
    }

}
