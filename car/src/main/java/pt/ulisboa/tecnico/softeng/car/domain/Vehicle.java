package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public abstract class Vehicle extends Vehicle_Base {
	private static Logger logger = LoggerFactory.getLogger(Vehicle.class);
	private static String plateFormat = "..-..-..";

	public final Set<Renting> rentings = new HashSet<>();

	public void init(String plate, int kilometers, double price, RentACar rentACar) {
		logger.debug("Vehicle plate: {}", plate);
		checkArguments(plate, kilometers, rentACar);

		setPlate(plate.toUpperCase());
		setKilometers(kilometers);
		setPrice(price);
		setRentACar(rentACar);

		rentACar.addVehicle(this);

	}



	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if (rentACar == null) {
			throw new CarException();
		} else if (plate == null || !plate.matches(plateFormat) || rentACar.hasVehicle(plate)) {
			throw new CarException();
		} else if (kilometers < 0) {
			throw new CarException();
		}
	}

	public void delete() {
		setRentACar(null);

		//TODO delete rentings

		deleteDomainObject();
	}

	/**
	 * @param kilometers
	 *            the kilometers to set
	 */
	public void addKilometers(int kilometers) {
		if (kilometers < 0) {
			throw new CarException();
		}
		setKilometers(this.getKilometers() + kilometers);
	}


	/**
	 * @return the rentACar
	 */

	public boolean isFree(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new CarException();
		}
		for (Renting renting : this.rentings) {
			if (renting.conflict(begin, end)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a <code>Renting</code> object to the vehicle. Use with caution --- no
	 * validation is being made.
	 *
	 * @param renting
	 */
	private void addRenting(Renting renting) {
		this.rentings.add(renting);
	}

	/**
	 * Lookup for a <code>Renting</code> with the given reference.
	 *
	 * @param reference
	 * @return Renting with the given reference
	 */
	public Renting getRenting(String reference) {
		return this.rentings
				.stream()
				.filter(renting -> renting.getReference().equals(reference)
                        || renting.isCancelled() && renting.getCancellationReference().equals(reference))
				.findFirst()
				.orElse(null);
	}

	/**
	 * @param drivingLicense
	 * @param begin
	 * @param end
	 * @return
	 */
	public Renting rent(String drivingLicense, LocalDate begin, LocalDate end, String buyerNIF, String buyerIBAN) {
		if (!isFree(begin, end)) {
			throw new CarException();
		}

		Renting renting = new Renting(drivingLicense, begin, end, this, buyerNIF, buyerIBAN);
		this.addRenting(renting);

        this.getRentACar().getProcessor().submitRenting(renting);


        return renting;
	}
}
