package me.t3sl4.hydraulic.Util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Util.Data.Excel.DataManipulator;
import org.apache.poi.ss.usermodel.*;

public class Util {

    public static DataManipulator dataManipulator = new DataManipulator();
    
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void filePath() {
        File profilePhotoTestFile = new File(Launcher.profilePhotoLocalPath);
        File pdfFileTestFile = new File(Launcher.pdfFileLocalPath);
        File excelFileTestFile = new File(Launcher.excelFileLocalPath);
        File dataFileTestFile = new File(Launcher.dataFileLocalPath);
        File loginFileTestFile = new File(Launcher.loginFilePath);

        if(!profilePhotoTestFile.exists()) {
            profilePhotoTestFile.mkdirs();
        }

        if(!pdfFileTestFile.exists()) {
            pdfFileTestFile.mkdirs();
        }

        if(!excelFileTestFile.exists()) {
            excelFileTestFile.mkdirs();
        }

        if (!dataFileTestFile.exists()) {
            dataFileTestFile.mkdirs();
        }

        if (!loginFileTestFile.exists()) {
            loginFileTestFile.mkdirs();
        }
    }

    public static void pdfGenerator(String pngFilePath1, String pngFilePath2, String pdfFilePath, String girilenSiparisNumarasi) {
        try {
            String userHome = System.getProperty("user.home");
            String ExPDFFilePath = userHome + File.separator + "Desktop" + File.separator + girilenSiparisNumarasi + ".pdf";

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(ExPDFFilePath));
            document.open();

            PdfContentByte contentByte = writer.getDirectContentUnder();
            //BaseColor backgroundColor = new BaseColor(53, 58, 70);
            //BaseColor backgroundColor = new BaseColor(249, 248, 113);
            BaseColor backgroundColor = new BaseColor(255, 255, 255);
            contentByte.setColorFill(backgroundColor);
            contentByte.rectangle(0, 0, document.getPageSize().getWidth(), document.getPageSize().getHeight());
            contentByte.fill();

            Image image1 = Image.getInstance(Objects.requireNonNull(Launcher.class.getResource(pngFilePath1)));
            float targetWidth1 = document.getPageSize().getWidth() * 0.5f;
            float targetHeight1 = (image1.getHeight() / (float) image1.getWidth()) * targetWidth1;
            image1.scaleToFit(targetWidth1, targetHeight1);
            image1.setAlignment(Image.ALIGN_CENTER);
            document.add(image1);

            Image image2 = Image.getInstance(pngFilePath2);

            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();
            float imageWidth = image2.getWidth();
            float imageHeight = image2.getHeight();

            float x = (documentWidth - imageWidth) / 2;
            float y = (documentHeight - imageHeight) / 2;

            image2.setAbsolutePosition(x, y);
            document.add(image2);

            PdfReader reader = new PdfReader(Objects.requireNonNull(Launcher.class.getResource(pdfFilePath)));
            PdfImportedPage page = writer.getImportedPage(reader, 1);
            document.newPage();
            writer.getDirectContent().addTemplate(page, 0, 0);

            document.close();
            writer.close();

            System.out.println("PDF oluşturuldu.");

            File pngFile2 = new File(pngFilePath2);
            if (pngFile2.exists()) {
                if (pngFile2.delete()) {
                    System.out.println("İkinci PNG dosyası silindi.");
                } else {
                    System.out.println("İkinci PNG dosyası silinemedi.");
                }
            } else {
                System.out.println("İkinci PNG dosyası bulunamadı.");
            }

            if (Desktop.isDesktopSupported()) {
                try {
                    File pdfFile = new File(ExPDFFilePath);
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4Bosluk(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Boşluk Değerleri";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            //Row variableNamesRow = sheet.getRow(0);
            Row variableValuesRow = sheet.getRow(1);

            dataManipulator.kampanaBoslukX = (int) variableValuesRow.getCell(0).getNumericCellValue();
            dataManipulator.kampanaBoslukY = (int) variableValuesRow.getCell(1).getNumericCellValue();
            dataManipulator.valfBoslukX = (int) variableValuesRow.getCell(2).getNumericCellValue();
            dataManipulator.valfBoslukYArka = (int) variableValuesRow.getCell(3).getNumericCellValue();
            dataManipulator.valfBoslukYOn = (int) variableValuesRow.getCell(4).getNumericCellValue();
            dataManipulator.kilitliBlokAraBoslukX = (int) variableValuesRow.getCell(5).getNumericCellValue();
            dataManipulator.tekHizAraBoslukX = (int) variableValuesRow.getCell(6).getNumericCellValue();
            dataManipulator.ciftHizAraBoslukX = (int) variableValuesRow.getCell(7).getNumericCellValue();
            dataManipulator.kompanzasyonTekHizAraBoslukX = (int) variableValuesRow.getCell(8).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukX = (int) variableValuesRow.getCell(9).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkOn = (int) variableValuesRow.getCell(10).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkArka = (int) variableValuesRow.getCell(11).getNumericCellValue();
            dataManipulator.kilitMotorKampanaBosluk = (int) variableValuesRow.getCell(12).getNumericCellValue();
            dataManipulator.kilitMotorMotorBoslukX = (int) variableValuesRow.getCell(13).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYOn = (int) variableValuesRow.getCell(14).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYArka = (int) variableValuesRow.getCell(15).getNumericCellValue();
            dataManipulator.kayipLitre = (int) variableValuesRow.getCell(16).getNumericCellValue();
            dataManipulator.kilitPlatformMotorBosluk = (int) variableValuesRow.getCell(17).getNumericCellValue();
            dataManipulator.valfXBoslukSogutma = (int) variableValuesRow.getCell(18).getNumericCellValue();

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4Kampana(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kampana";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                data = data.trim();
                data = data.replaceAll("[^0-9]", "");
                dataManipulator.kampanaDegerleri.add(Integer.parseInt(data));
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4Motor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4UniteTipi(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Ünite Tipi";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.uniteTipiDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4PompaHidros(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-1";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4PompaKlasik(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-2";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriKlasik.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4PompaTumu(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-3";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriTumu.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4KilitMotor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kilit Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.kilitMotorDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4KilitPompa(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kilit Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.kilitPompaDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ValfTipi1(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Valf Tipi-1";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfTipiDegerleri1.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ValfTipi2(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Valf Tipi-2";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfTipiDegerleri2.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiKampana(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Kampana";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiKampana250.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiKampana300.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiKampana350.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiKampana400.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiPompa(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 10; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiPompa95.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiPompa119.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiPompa14.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiPompa146.add(temp);
                                okunacakSatir++;
                            } else if (i == 4) {
                                dataManipulator.parcaListesiPompa168.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                dataManipulator.parcaListesiPompa192.add(temp);
                                okunacakSatir++;
                            } else if (i == 6) {
                                dataManipulator.parcaListesiPompa229.add(temp);
                                okunacakSatir++;
                            } else if (i == 7) {
                                dataManipulator.parcaListesiPompa281.add(temp);
                                okunacakSatir++;
                            } else if (i == 8) {
                                dataManipulator.parcaListesiPompa288.add(temp);
                                okunacakSatir++;
                            } else if (i == 9) {
                                dataManipulator.parcaListesiPompa333.add(temp);
                                okunacakSatir++;
                            } else if (i == 10) {
                                dataManipulator.parcaListesiPompa379.add(temp);
                                okunacakSatir++;
                            } else if (i == 11) {
                                dataManipulator.parcaListesiPompa426.add(temp);
                                okunacakSatir++;
                            } else if (i == 12) {
                                dataManipulator.parcaListesiPompa455.add(temp);
                                okunacakSatir++;
                            } else if (i == 13) {
                                dataManipulator.parcaListesiPompa494.add(temp);
                                okunacakSatir++;
                            } else if (i == 14) {
                                dataManipulator.parcaListesiPompa561.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiMotor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 1; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiMotor4.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiMotor55.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiMotor55Kompakt.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiMotor75Kompakt.add(temp);
                                okunacakSatir++;
                            } else if (i == 4) {
                                dataManipulator.parcaListesiMotor11.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                dataManipulator.parcaListesiMotor11Kompakt.add(temp);
                                okunacakSatir++;
                            } else if (i == 6) {
                                dataManipulator.parcaListesiMotor15.add(temp);
                                okunacakSatir++;
                            } else if (i == 7) {
                                dataManipulator.parcaListesiMotor185.add(temp);
                                okunacakSatir++;
                            } else if (i == 8) {
                                dataManipulator.parcaListesiMotor22.add(temp);
                                okunacakSatir++;
                            } else if (i == 9) {
                                dataManipulator.parcaListesiMotor37.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiKaplin(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Kaplin";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 1; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiKaplin1PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiKaplin1PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiKaplin1PN42.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiKaplin2PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 4) {
                                dataManipulator.parcaListesiKaplin2PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                dataManipulator.parcaListesiKaplin2PN42.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiValfBlok(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Valf Blokları";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 11; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiValfBloklariTekHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiValfBloklariCiftHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiValfBloklariKilitliBlok.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiValfBloklariKompanzasyon.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiBasincSalteri(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Basınç Şalteri";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiBasincSalteri.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaListesiStandart(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Standart";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                dataManipulator.parcaListesiStandart.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosMotorDegerleri380(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-380";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriHidros380.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosMotorDegerleri220(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-220";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriHidros220.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosPompaKapasite(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaKapasiteDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosTankDikey(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Tank-Dikey";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriHidrosDikey.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosTankYatay(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Tank-Yatay";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriHidrosYatay.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosPlatform(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Platform";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.platformDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4HidrosValf(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Valf-Deger";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void readExcel4ParcaHidrosMotor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Parça-Hidros-Motor380";

        try(InputStream file = new FileInputStream(filePath)) {
            assert file != null;
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    dataManipulator.hidros380Parca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void initMotorYukseklik() {
        Util.dataManipulator.motorYukseklikVerileri.add("345 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("375 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("365 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("410 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("500 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("470 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("540 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("565 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("565 mm");
        Util.dataManipulator.motorYukseklikVerileri.add("600 mm");
    }

    private static boolean isNumeric(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA;
    }

    public static void openURL(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void excelDataRead() {
        readExcel4Bosluk(Launcher.excelDBPath, dataManipulator);
        readExcel4Kampana(Launcher.excelDBPath, dataManipulator);
        readExcel4Motor(Launcher.excelDBPath, dataManipulator);
        readExcel4UniteTipi(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaHidros(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaKlasik(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaTumu(Launcher.excelDBPath, dataManipulator);
        readExcel4KilitMotor(Launcher.excelDBPath, dataManipulator);
        readExcel4KilitPompa(Launcher.excelDBPath, dataManipulator);
        readExcel4ValfTipi1(Launcher.excelDBPath, dataManipulator);
        readExcel4ValfTipi2(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiKampana(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiPompa(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiMotor(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiKaplin(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiValfBlok(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiBasincSalteri(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiStandart(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosMotorDegerleri380(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosMotorDegerleri220(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosPompaKapasite(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosTankDikey(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosTankYatay(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosPlatform(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosValf(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosMotor(Launcher.excelDBPath, dataManipulator);
        initMotorYukseklik();
        HashMap<String, String> testDeger = getValuesFromDoubleHashMap(Util.dataManipulator.hidros380Parca, "0.37 kw");
        for (Map.Entry<String, String> entry : testDeger.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }

    }

    public static void showErrorMessage(String hataMesaji) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(hataMesaji);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String basariMesaji) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Başarılı !");
        alert.setHeaderText(null);
        alert.setContentText(basariMesaji);
        alert.showAndWait();
    }

    public static void showErrorOnLabel(Label mainLabel, String message) {
        mainLabel.setText(message);

        Duration delay = Duration.seconds(1.5);

        KeyFrame keyFrame = new KeyFrame(delay, event -> {
            mainLabel.setText(null);
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.play();
    }

    public static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String getOperatingSystem() {
        return System.getProperty("os.name");
    }

    public static void changeDataStoragePath() {
        if(getOperatingSystem().contains("Windows")) {
            Launcher.profilePhotoLocalPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/profilePhoto/";
            Launcher.pdfFileLocalPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/hydraulicUnits/";
            Launcher.excelFileLocalPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/partList/";
            Launcher.dataFileLocalPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/data/";
            Launcher.loginFilePath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/login/";
            Launcher.excelDBPath = "C:/Users/" + System.getProperty("user.name") + "/OnderGrup/data/Hidrolik.xlsx";
        } else {
            Launcher.profilePhotoLocalPath = "/home/" + System.getProperty("user.name") + "/OnderGrup/profilePhoto/";
            Launcher.pdfFileLocalPath = "/home/" + System.getProperty("user.name") + "/OnderGrup/hydraulicUnits/";
            Launcher.excelFileLocalPath = "/home/" + System.getProperty("user.name") + "/OnderGrup/partList/";
            Launcher.dataFileLocalPath = "/home/" + System.getProperty("user.name") + "/OnderGrup/data/";
            Launcher.loginFilePath = "/home/" + System.getProperty("user.name") + "/OnderGrup/login/";
            Launcher.excelDBPath = "/home/" + System.getProperty("user.name") + "/OnderGrup/data/Hidrolik.xlsx";
        }
    }

    public static void textFilter(javafx.scene.control.TextField filteredField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        filteredField.setTextFormatter(textFormatter);
    }

    public static HashMap<String, String> getValuesFromDoubleHashMap(HashMap<String, HashMap<String, String>> inputHash, String searchKey) {
        HashMap<String, String> innerMap = new HashMap<>();
        for (Map.Entry<String, HashMap<String, String>> entry : inputHash.entrySet()) {
            String key = entry.getKey();
            if(Objects.equals(searchKey, key)) {
                System.out.println("Test");
                innerMap = entry.getValue();

                System.out.println("Key: " + key);
                System.out.println("Value (B): " + innerMap.get("B"));
                System.out.println("Value (C): " + innerMap.get("C"));
                System.out.println("------------------------");
            }
        }
        return innerMap;
    }
}