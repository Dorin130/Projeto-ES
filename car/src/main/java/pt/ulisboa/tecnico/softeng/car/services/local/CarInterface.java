package pt.ulisboa.tecnico.softeng.car.services.local;

import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;

import java.util.List;
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
	}

	@Atomic(mode = TxMode.READ)
	public static RentACarData getRentACarDataByCode(String code) {
		RentACar rentACar = getRentACarByCode(code);
		if (rentACar == null) {
			return null;
		}

		return new RentACarData(rentACar);
	}

	/* missing create car/motorcycle/vehicle */
	/* missing getVehicleDataByPlate */

	@Atomic(mode = TxMode.WRITE)
	public static String rent(Class<? extends Vehicle> vehicleType, String drivingLicense, String buyerNIF,
							  String buyerIBAN, LocalDate begin, LocalDate end) {
		return RentACar.rent(vehicleType, drivingLicense, buyerNIF, buyerIBAN, begin, end);
	}


	@Atomic(mode = TxMode.WRITE)
	public static String cancelRenting(String reference) {
		return RentACar.cancelRenting(reference);
	}

	private static RentingData getRentingDataByReference(String reference) {
		return RentACar.getRentingData(reference);
	}

	public static RentACar getRentACarByCode(String code) {
		return FenixFramework.getDomainRoot().getRentACarSet().stream().filter(p -> p.getCode().equals(code))
				.findFirst().orElse(null);
	}
}
