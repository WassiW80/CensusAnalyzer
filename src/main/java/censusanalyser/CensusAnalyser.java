package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    public enum Country {INDIA, US}

    Map<String, CensusDTO> censusCSVMap;
    List<CensusDTO> collect;

    public CensusAnalyser() {
        this.collect = new ArrayList<>();
        this.censusCSVMap = new HashMap<>();
    }

    public int loadCensusData(Country country, String... csvFilePath) {
        censusCSVMap = new CensusLoader().loadCensusData(country, csvFilePath);
        return censusCSVMap.size();
    }

    public String getStateWiseSortedCensusData() {
        collect = censusCSVMap.values().stream().collect(Collectors.toList());
        if (collect == null || collect.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusComparator = Comparator.comparing(census -> census.state);

        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(collect);
        return sortedStateCensus;

    }

    public String getPopulationWiseSortedCensusData() {
        collect = censusCSVMap.values().stream().collect(Collectors.toList());
        if (collect == null || collect.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedPopulation = new Gson().toJson(collect);
        return sortedPopulation;
    }

    private void sort(Comparator<CensusDTO> censusComparator) {
        for (int i = 0; i < collect.size() - 1; i++) {
            for (int j = 0; j < collect.size() - 1 - i; j++) {
                CensusDTO census1 = collect.get(j);
                CensusDTO census2 = collect.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    collect.set(j, census2);
                    collect.set(j + 1, census1);
                }
            }
        }
    }


}
