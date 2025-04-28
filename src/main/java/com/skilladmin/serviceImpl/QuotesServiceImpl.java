package com.skilladmin.serviceImpl;

import com.skilladmin.model.Quote;
import com.skilladmin.repository.QuoteRepository;
import com.skilladmin.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QuotesServiceImpl implements QuoteService{
    @Autowired
    private QuoteRepository quoteRepository;


    @Override
    public Quote  getQuoteOfTheDay() {
        List<Quote> quotes = quoteRepository.findAll();
        int dayOfYear = LocalDate.now().getDayOfYear();
        return quotes.get(dayOfYear % quotes.size());  }
}
