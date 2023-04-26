package com.Zaezul.CoronavirusTracker.controllers;

import com.Zaezul.CoronavirusTracker.models.LocationStats;
import com.Zaezul.CoronavirusTracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Creates our routes and uses our LocationStats model to display the data that we have fetched from our csv records.
 */
@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    /**
     * A mapping for the home page.
     * @param model is the LocationStats model that has our COVID-19 data.
     * @return index.html
     */
    @GetMapping("/")
    public String home(Model model) throws IOException, InterruptedException {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();

        String casesSum = String.valueOf(allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum());
        Double totalCases = Double.parseDouble(casesSum);
        DecimalFormat formatter = new DecimalFormat("#,###");

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", formatter.format(totalCases));

        return "index";
    }
}
