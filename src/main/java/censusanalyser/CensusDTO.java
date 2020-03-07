package censusanalyser;

public class CensusDTO {
    public double populationDensity;
    public String state;
    public double totalArea;
    public int population;
    public String stateId;


    public CensusDTO(IndiaCensusCSV next) {
        state = next.state;
        totalArea = next.totalArea;
        populationDensity = next.populationDensity;
        population = next.population;
    }

    public CensusDTO(USCensusCSV usCensusCSV) {
        population = usCensusCSV.population;
        populationDensity = usCensusCSV.populationDensity;
        state = usCensusCSV.state;
        stateId = usCensusCSV.stateId;
        totalArea = usCensusCSV.totalArea;
    }
}
