package com.skilladmin.controller;

import com.skilladmin.model.Quote;
import com.skilladmin.repository.QuoteRepository;
import com.skilladmin.service.QuoteService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;

@CrossOrigin(origins = {"*"})
@RestController
public class QuotesController {
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private QuoteService quoteService;



    // UPLOAD QUOTES FOR APP
    @PostMapping("/uploadQuotes")
    public String uploadQuotes(@RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Read Quote Cell
                
                
                
                Cell quoteCell = row.getCell(0);
                String quoteText = "";
                if (quoteCell.getCellType() == CellType.STRING) {
                    quoteText = quoteCell.getStringCellValue();
                } else if (quoteCell.getCellType() == CellType.NUMERIC) {
                    quoteText = String.valueOf(quoteCell.getNumericCellValue());
                }

                // Read Author Cell
                Cell authorCell = row.getCell(1);
                String author = "";
                if (authorCell.getCellType() == CellType.STRING) {
                    author = authorCell.getStringCellValue();
                } else if (authorCell.getCellType() == CellType.NUMERIC) {
                    author = String.valueOf(authorCell.getNumericCellValue());
                }

                // Save to database
                Quote quote = new Quote();
                quote.setQuote(quoteText);
                quote.setAuthor(author);
                quoteRepository.save(quote);
            }

            return "File uploaded and quotes saved successfully!";
        } catch (Exception e) {
            return "File upload failed: " + e.getMessage();
        }
    }

    
    //  QUOTE PER DAY
    @GetMapping("/quote-of-the-day")
    public Quote  getQuoteOfTheDay() {
      return quoteService.getQuoteOfTheDay();
      }


}
