package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndiaCensusAdaptor extends CensusAdaptor {
    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) {
        Map<String, CensusDTO> censusStateMap = super.loadCensusData(IndiaCensusCSV.class, csvFilePath[0]);
        this.loadIndianStateCode(censusStateMap, csvFilePath[1]);
        return censusStateMap;
    }
/*
    public Map<String, CensusDTO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return this.loadCensusData(IndiaCensusCSV.class, csvFilePath);
        else if (country.equals(CensusAnalyser.Country.US))
            return this.loadCensusData(USCensusCSV.class, csvFilePath);
        else
            throw new CensusAnalyserException("Invalid Country Name", CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }

    private <E> Map<String, CensusDTO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) {
        Map<String, CensusDTO> censusCSVMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvIterable = () -> csvFileIterator;
            if (censusCSVClass.getName() == "censusanalyser.IndiaCensusCSV") {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(csvState -> censusCSVMap.put(csvState.state, new CensusDTO(csvState)));
            } else if (censusCSVClass.getName() == "censusanalyser.USCensusCSV") {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(csvState -> censusCSVMap.put(csvState.state, new CensusDTO(csvState)));
            }
            if (csvFilePath.length == 1)
                return censusCSVMap;
            this.loadIndianStateCode(censusCSVMap, csvFilePath[1]);
            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }*/

    private int loadIndianStateCode(Map<String, CensusDTO> censusCSVMap, String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCode> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCode.class);
            Iterable<IndiaStateCode> csvIterable = () -> censusCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false).filter(csvState -> censusCSVMap.get(csvState.stateName) != null)
                    .forEach(csvState -> censusCSVMap.get(csvState.stateName).stateCode = csvState.stateCode);

            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}
