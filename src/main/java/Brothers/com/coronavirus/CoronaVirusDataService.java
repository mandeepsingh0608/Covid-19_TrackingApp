package Brothers.com.coronavirus;

import Brothers.com.coronavirus.models.locationstats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

//this service to collect data for corona Virus from http url

@Service
public class CoronaVirusDataService {
    private static String Virus_Data_Url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<locationstats> allStats=new ArrayList<>();



    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<locationstats> newStats=new ArrayList<>();

        //Creating Http Client

        HttpClient client = HttpClient.newHttpClient();

        //building a request
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Virus_Data_Url)).build();

        //sending request and collect in http response
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println(httpResponse.body());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
           locationstats locationstats=new locationstats();
             locationstats.setState(record.get("Province/State"));
            locationstats.setCountry(record.get("Country/Region"));
            locationstats.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
           //System.out.println(locationstats);
            newStats.add(locationstats);

        }

        this.allStats=newStats;
        //System.out.println(this.allStats);


    }
    public List<locationstats> getAllStats() {
        return allStats;
    }

}

