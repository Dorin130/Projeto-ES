package pt.ulisboa.tecnico.softeng.car.domain;

//license plate test incomplete

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.VehicleException;

public class Renting {
	private static int counter = 0;

	private final String reference;
	private final String drivingLicense;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers;

	Renting(RentACar rentacar, String drivingLicense, LocalDate begin, LocalDate end) {
		checkArguments(rentacar, drivingLicense, begin, end);

		this.reference = rentacar.getCode() + Integer.toString(++Renting.counter);
		this.begin = begin;
		this.end = end;
	}

	private void checkArguments(RentACar rentacar, String drivingLicense, LocalDate begin, LocalDate end) {
		if (rentacar == null || drivingLicense == null || begin == null || end == null) {
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
	
	public String getCancellation() {
		return this.cancellation;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}
	
	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public int getKilometers() {
		return kilometers;
	}

	boolean conflict(LocalDate begin, LocalDate end) {
		if (isCancelled()) {
			return false;
		}

		if (begin.equals(end)) {
			return true;
		}

		if (end.isBefore(begin)) {
			throw new VehicleException();
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

	public String cancel() {
		this.cancellation = this.reference + "CANCEL";
		this.cancellationDate = new LocalDate();
		return this.cancellation;
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

}
