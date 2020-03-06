package censusanalyser;

import java.util.Map;

public class USCensusAdaptor extends CensusAdaptor {
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) {
        Map<String, CensusDTO> censusDTOMap = super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
        return censusDTOMap;
    }
}
