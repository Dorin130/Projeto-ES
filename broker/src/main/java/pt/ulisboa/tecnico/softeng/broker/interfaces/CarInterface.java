package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.domain.Renting;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import java.util.Set;

public class CarInterface {

    public static  String processRenting() { return RentACar.processRenting();}
    public static  String cancelRenting(String reference) { return RentACar.cancelRenting(reference);}
    public static RentingData getRentingData(String reference) {
        return RentACar.getRentingData(reference);
    }
}
