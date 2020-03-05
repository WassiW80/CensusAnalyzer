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
    Map<String, IndiaCensusDTO> censusCSVMap = null;
    List<IndiaCensusDTO> collect = null;

    public CensusAnalyser() {
        this.collect = new ArrayList<IndiaCensusDTO>();
        this.censusCSVMap = new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while (csvFileIterator.hasNext()) {
                IndiaCensusCSV censusCSVIterator = csvFileIterator.next();
                this.censusCSVMap.put(censusCSVIterator.state, new IndiaCensusDTO(censusCSVIterator));
            }

            collect = censusCSVMap.values().stream().collect(Collectors.toList());

            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfEnteries;
    }

    public int loadIndianStateCode(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = (ICSVBuilder) CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCode> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCode.class);
            Iterable<IndiaStateCode> csvIterable = () -> censusCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false).count();
            StreamSupport.stream(csvIterable.spliterator(), false).filter(csvState -> censusCSVMap.get(csvState.stateName) != null)
                    .forEach(csvState -> censusCSVMap.get(csvState.stateName).stateCode = csvState.stateCode);
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData() {
        if (collect == null || collect.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(collect);
        return sortedStateCensus;

    }

    private void sort(Comparator<IndiaCensusDTO> censusComparator) {
        for (int i = 0; i < collect.size() - 1; i++) {
            for (int j = 0; j < collect.size() - 1 - i; j++) {
                IndiaCensusDTO census1 = collect.get(j);
                IndiaCensusDTO census2 = collect.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    collect.set(j, census2);
                    collect.set(j + 1, census1);
                }
            }
        }
    }

    public String getPopulationWiseSortedCensusData() {
        if (collect == null || collect.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDTO> censusComparator = Comparator.comparing(census -> census.population);
        this.sort(censusComparator);
        String sortedPopulation = new Gson().toJson(collect);
        return sortedPopulation;
    }
}
