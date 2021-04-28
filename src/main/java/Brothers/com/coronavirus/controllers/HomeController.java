package Brothers.com.coronavirus.controllers;

import Brothers.com.coronavirus.CoronaVirusDataService;
import Brothers.com.coronavirus.models.locationstats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/corona")
public class HomeController {
    @Autowired
    CoronaVirusDataService dataService;

//list of all countries by cases

    @GetMapping("/cases/inallcountries")
    public ResponseEntity<?> AllcountiresList(){
        List<locationstats> locationstatsList=dataService.getAllStats();
        return ResponseEntity.ok(locationstatsList);
    }

    //get cases by worldwide
    @GetMapping("/totalcases/worldwide")
    public ResponseEntity<?> TotalCasesInWorld(){
        List<locationstats> locationstatsList=dataService.getAllStats();
        int totalCases=locationstatsList.stream().mapToInt(status-> status.getLatestTotalCases()).sum();
        return ResponseEntity.ok("Total cases in worldwide  --> "+totalCases);
    }

    //To get total cases by country
    @GetMapping("/totalcases")
    public ResponseEntity<?> TotalCasesbyCountry(@RequestParam("country") String country){
        List<locationstats>  locationstatsList=dataService.getAllStats();
        int totalcases= locationstatsList.stream().filter(c->c.getCountry().matches(country)).mapToInt(status->status.getLatestTotalCases()).sum();
        return ResponseEntity.ok("Total cases in "+country+"----> "+totalcases+ "\n"+locationstatsList.stream().filter(c->c.getCountry().matches(country)).collect(Collectors.toList()));


    }



}
