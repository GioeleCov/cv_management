package it.giocode.cv_managment.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import it.giocode.cv_managment.entity.CVEntity;
import it.giocode.cv_managment.entity.CandidateEntity;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;

@Component
public class PdfService {

    public PdfService() {

    }

    public void generatePdf(CandidateEntity candidate, CVEntity cv, String path) {
        String fileName = path + cv.getFileName();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            String cvTitle = "Curriculum vitae di " + candidate.getName() + " " + candidate.getSurname();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph(cvTitle, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            if (cv.getProfileImage() != null && !cv.getProfileImage().isEmpty()) {
                document.add(new Paragraph("\n\n"));
                Image img = Image.getInstance(cv.getProfileImage());
                img.scaleToFit(200, 200);
                img.setAlignment(Element.ALIGN_CENTER);
                document.add(img);
            }

            document.add(new Paragraph("\n\n\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(70);
            table.setSpacingBefore(20f);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            float[] columnWidths = {2f, 5f};
            table.setWidths(columnWidths);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            addRow(table, "Nome:", candidate.getName(), headerFont, normalFont);
            addRow(table, "Cognome:", candidate.getSurname(), headerFont, normalFont);
            addRow(table, "Eamil:", candidate.getUser().getEmail(), headerFont, normalFont);
            addRow(table, "Telefono:", candidate.getPhoneNumber(), headerFont, normalFont);
            addRow(table, "Istruzione:", cv.getEducation(), headerFont, normalFont);
            addRow(table, "Lingue Parlate:", cv.getSpokenLanguage(), headerFont, normalFont);
            addRow(table, "Esperienze:", cv.getExperiences(), headerFont, normalFont);
            addRow(table, "Competenze:", cv.getSkills(), headerFont, normalFont);

            document.add(table);

            document.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addRow(PdfPTable table, String field, String value, Font fieldFont, Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(field, fieldFont));
        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));

        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);

        cell1.setPaddingBottom(20f);
        cell2.setPaddingBottom(20f);

        table.addCell(cell1);
        table.addCell(cell2);
    }
}
