package com.Zaezul.CoronavirusTracker.controllers;

import com.Zaezul.CoronavirusTracker.models.LocationStats;
import com.Zaezul.CoronavirusTracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();

        String casesSum = String.valueOf(allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum());
        Double totalCases = Double.parseDouble(casesSum);
        DecimalFormat formatter = new DecimalFormat("#,###");

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", formatter.format(totalCases));

        return "index";
    }
}
