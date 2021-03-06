package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndiaStateCode {

    @CsvBindByName(column = "StateName", required = true)
    public String stateName;

    @CsvBindByName(column = "StateCode", required = true)
    public String stateId;

    @Override
    public String toString() {
        return "IndianStateCode{ 'State Name='" + stateName + '\'' + "'State Code'" + stateId + '\'' + '}';
    }
}
