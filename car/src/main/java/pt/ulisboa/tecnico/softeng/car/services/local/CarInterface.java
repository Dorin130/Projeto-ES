package pt.ulisboa.tecnico.softeng.car.services.local;

import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.Motorcycle;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Renting;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CarInterface {

	@Atomic(mode = TxMode.READ)
	public static List<RentACarData> getRentACars() {
		return FenixFramework.getDomainRoot().getRentACarSet().stream()
				.sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).map(p -> new RentACarData(p))
				.collect(Collectors.toList());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createRentACar(RentACarData rentACar) {
		new RentACar(rentACar.getName(), rentACar.getNif(), rentACar.getIban());
		Set<RentACar> bla = FenixFramework.getDomainRoot().getRentACarSet();
		 System.out.println(bla.size());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createVehicle(VehicleData vehicle, String code) {
		if(vehicle.getType().equals("Car")) {
			new Car(vehicle.getPlate(), vehicle.getKilometers(), vehicle.getPrice(), getRentACarByCode(code));
		}
		else if(vehicle.getType().equals("Motorcycle")) {
			new Motorcycle(vehicle.getPlate(), vehicle.getKilometers(), vehicle.getPrice(), getRentACarByCode(code));
		}
	}

	@Atomic(mode = TxMode.READ)
	public static RentACarData getRentACarDataByCode(String code) {
		RentACar rentACar = getRentACarByCode(code);
		if (rentACar == null) {
			return null;
		}

		return new RentACarData(rentACar);
	}

	@Atomic(mode = TxMode.READ)
	public static VehicleData getVehicleDataDataByPlate(String code, String plate) {
		Vehicle vehicle = getVehicleByPlate(code, plate);
		if (vehicle == null) {
			return null;
		}

		return new VehicleData(vehicle, vehicle.getClass().getSimpleName());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createRenting(String code, String plate, RentingData renting) {
		Vehicle vehicle = getVehicleByPlate(code, plate);
		if (vehicle == null) {
			throw new CarException();
		}

		new Renting(renting.getDrivingLicense(), renting.getBegin(), renting.getEnd(), vehicle, renting.getBuyerNif(), renting.getBuyerIban());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static String rent(Class<? extends Vehicle> vehicleType, String drivingLicense, String buyerNIF,
							  String buyerIBAN, LocalDate begin, LocalDate end) {
		return RentACar.rent(vehicleType, drivingLicense, buyerNIF, buyerIBAN, begin, end);
	}


	@Atomic(mode = TxMode.WRITE)
	public static String cancelRenting(String reference) {
		return RentACar.cancelRenting(reference);
	}

	@Atomic(mode = TxMode.READ)
	public static RentingData getRentingDataByReference(String reference) {
		return RentACar.getRentingData(reference);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static Renting rentingCheckout(String code, String plate, String reference, RentingData rentingData) {
		Renting renting = getRentingByReference(code, plate, reference);
		
		if(renting == null) {
			throw new CarException();
		}
		renting.checkout(rentingData.getKilometers());
		return renting;
	}
	
	private static RentACar getRentACarByCode(String code) {
		return FenixFramework.getDomainRoot().getRentACarSet().stream().filter(p -> p.getCode().equals(code))
				.findFirst().orElse(null);
	}
	
	private static Vehicle getVehicleByPlate(String code, String plate) {
		RentACar rentacar = getRentACarByCode(code);
		
		if(rentacar == null) {
			return null;
		}

		return rentacar.getVehicleSet().stream().filter(v -> v.getPlate().equals(plate)).findFirst().orElse(null);
	}

	private static Renting getRentingByReference(String code, String plate, String reference) {
		Vehicle vehicle = getVehicleByPlate(code, plate);
		
		if(vehicle == null) {
			return null;
		}
		return vehicle.getRentingSet().stream().filter(r -> r.getReference().equals(reference)).findFirst().orElse(null);
	}
	
}
