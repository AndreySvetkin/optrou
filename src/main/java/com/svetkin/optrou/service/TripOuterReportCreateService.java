package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.entity.dto.RefuellingsVolumeDto;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component(TripOuterReportCreateService.NAME)
public class TripOuterReportCreateService {

    public static final String NAME = "optrou_TripOuterReportCreateService";

    private final TripFactLineService tripFactLineService;
    private final TripRefuellingsReportService tripRefuellingsReportService;
    private final TripRouteViolationService tripRouteViolationService;

    public TripOuterReportCreateService( TripFactLineService tripFactLineService,
                                         TripRefuellingsReportService tripRefuellingsReportService,
                                         TripRouteViolationService tripRouteViolationService) {
        this.tripFactLineService = tripFactLineService;
        this.tripRefuellingsReportService = tripRefuellingsReportService;
        this.tripRouteViolationService = tripRouteViolationService;
    }

    public byte[] createTripsReport(List<Trip> trips) {
        try (Workbook workbook = new SXSSFWorkbook()) {
            CellStyle cellStyle = createDefaultCellStyle(workbook);
            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Отчет по рейсам");

            int index = 0;
            createHeaderRow(sheet, index++);

            for (Trip trip : trips) {
                createRow(sheet, index, trip, cellStyle);
                index++;
            }

            sheet.trackAllColumnsForAutoSizing();
            sheet.getRow(0).forEach(cell -> sheet.autoSizeColumn(cell.getColumnIndex()));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Row createRow(Sheet sheet, int index, Trip trip, CellStyle cellStyle) {
        Row row = sheet.createRow(index);
        int cellIndex = 0;
        Cell cell;
        row.createCell(cellIndex++).setCellValue(trip.getNumber());

        row.createCell(cellIndex++).setCellValue(trip.getLength());
        row.createCell(cellIndex++).setCellValue(tripFactLineService.getTripFactLine(trip).getLength() * 100);

        row.createCell(cellIndex++).setCellValue(tripRouteViolationService.hasRouteViolations(trip));

        RefuellingsVolumeDto refuellingsVolumeDto = tripRefuellingsReportService.getRefuellingsVolume(trip);
        row.createCell(cellIndex++).setCellValue(refuellingsVolumeDto.getPlanningVolume());
        row.createCell(cellIndex++).setCellValue(refuellingsVolumeDto.getFactVolume());

        LocalDateTime planningDateStart = trip.getPlanningDateStart();
        LocalDateTime planningDateEnd = trip.getPlanningDateEnd();
        LocalDateTime factDateStart = trip.getFactDateStart();
        LocalDateTime factDateEnd = trip.getPlanningDateEnd();
        row.createCell(cellIndex++).setCellValue(planningDateStart != null ? planningDateStart.toString() : "");
        row.createCell(cellIndex++).setCellValue(planningDateEnd != null ? planningDateEnd.toString() : "");
        row.createCell(cellIndex++).setCellValue(factDateStart != null ? factDateStart.toString() : "");
        row.createCell(cellIndex++).setCellValue(factDateEnd != null ? factDateEnd.toString() : "");

        Driver driver = trip.getDriver();
        row.createCell(cellIndex++).setCellValue(driver.getFullName());
        row.createCell(cellIndex++).setCellValue(driver.getPhoneNumber());

        Vehicle vehicle = trip.getVehicle();
        row.createCell(cellIndex++).setCellValue(vehicle.getLicensePlate());
        row.createCell(cellIndex).setCellValue(vehicle.getModel());

        return row;
    }

    private Row createHeaderRow(Sheet sheet, int index) {
        Row row = sheet.createRow(index);
        int cellIndex = 0;
        row.createCell(cellIndex++).setCellValue("Номер рейса");

        row.createCell(cellIndex++).setCellValue("Планируемая длина маршрута(км.)");
        row.createCell(cellIndex++).setCellValue("Фактическое растояние(км.)");

        row.createCell(cellIndex++).setCellValue("Максимальное отклонение от маршрута(км.)");

        row.createCell(cellIndex++).setCellValue("Планируемый объем(л.)");
        row.createCell(cellIndex++).setCellValue("Фактический объем(л.)");

        row.createCell(cellIndex++).setCellValue("Планируемая дата начала");
        row.createCell(cellIndex++).setCellValue("Планируемая дата окончания");
        row.createCell(cellIndex++).setCellValue("Фактическая дата начала");
        row.createCell(cellIndex++).setCellValue("Фактическая дата окончания");

        row.createCell(cellIndex++).setCellValue("Имя водителя");
        row.createCell(cellIndex++).setCellValue("Номер водителя");
        row.createCell(cellIndex++).setCellValue("Гос номер авто");
        row.createCell(cellIndex).setCellValue("Модель авто");

//        while (index != -1) {
//            sheet.autoSizeColumn(index);
//            index--;
//        }

        return row;
    }

    private CellStyle createDefaultCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }
}