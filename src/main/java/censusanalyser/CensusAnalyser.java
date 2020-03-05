package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    Map<String, CensusDTO> censusCSVMap = null;
    List<CensusDTO> collect = null;

    public CensusAnalyser() {
        this.collect = new ArrayList<CensusDTO>();
        this.censusCSVMap = new HashMap<>();
    }

    public int loadIndiaCensusData(String... csvFilePath) {
        censusCSVMap = new CensusLoader().loadCensusData(IndiaCensusCSV.class, csvFilePath);
        return censusCSVMap.size();
    }

    public int loadUSCensusData(String... csvFilePath) {
        censusCSVMap = new CensusLoader().loadCensusData(USCensusCSV.class,csvFilePath);
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
