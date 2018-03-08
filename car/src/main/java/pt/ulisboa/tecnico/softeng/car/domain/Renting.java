package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting {
	private static int counter = 0;

	private final Vehicle vehicle;
	private final String reference;
	private final String drivingLicense;
	private final LocalDate begin;
	private LocalDate end;
	private boolean checkedOut = false;
	private int kilometers = 0;

	Renting(Vehicle vehicle, String drivingLicense, LocalDate begin, LocalDate end) {
		checkArguments(vehicle, drivingLicense, begin, end);

		this.vehicle = vehicle;
		this.reference = vehicle.getRentAcar().getCode() + Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.begin = begin;
		this.end = end;
	}

	private void checkArguments(Vehicle vehicle, String drivingLicense, LocalDate begin, LocalDate end) {
		if (vehicle == null || drivingLicense == null || begin == null || end == null) {
			throw new CarException();
		}
		
		if(! drivingLicense.matches("^[a-zA-Z]+[0-9]+$")){
			throw new CarException();
		}

		if (end.isBefore(begin)) {
			throw new CarException();
		}
	}

	public String getReference() {
		return this.reference;
	}
	
	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}
	
	public int getKilometers() {
		return kilometers;
	}

	boolean conflict(LocalDate begin, LocalDate end) {
		if (isCheckedOut()) {
			return false;
		}

		if (begin.equals(end)) {
			return true;
		}

		if (end.isBefore(begin)) {
			throw new CarException();
		}

		if ((begin.equals(this.begin) || begin.isAfter(this.begin)) && begin.isBefore(this.end)) {
			return true;
		}

		if ((end.equals(this.end) || end.isBefore(this.end))
				&& end.isAfter(this.begin)) {
			return true;
		}

		if ((begin.isBefore(this.begin) && end.isAfter(this.end))) {
			return true;
		}

		return false;
	}
	
	public void checkout(int kilometers) {
		if (isCheckedOut()) {
			throw new CarException();
		}
		
		if (kilometers < 0) {
			throw new CarException();
		}
		
		this.kilometers = kilometers;
		this.vehicle.addKilometers(kilometers);
		this.checkedOut = true;
	}

	public boolean isCheckedOut() {
		return this.checkedOut;
	}

}
