package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.Renting;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import java.util.Set;

public class CarInterface {

    public static  String processVehicleRenting(String drivingLicense, LocalDate begin, LocalDate end, String nif, String iban) { return RentACar.rentVehicle(Car.class, drivingLicense, begin, end, nif, iban);}
    public static  String cancelVehicleRenting(String reference) { return RentACar.cancelRenting(reference);}
    public static RentingData getVehicleRentingData(String reference) {
        return RentACar.getRentingData(reference);
    }
}
