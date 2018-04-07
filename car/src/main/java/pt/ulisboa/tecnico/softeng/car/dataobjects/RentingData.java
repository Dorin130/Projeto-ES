package pt.ulisboa.tecnico.softeng.car.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.domain.Renting;

public class RentingData {
	private String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;
	private String cancel;
	private LocalDate cancellationDate;
	private int amount;

	public RentingData() {
	}

	public RentingData(Renting renting) {
		this.reference = renting.getReference();
		this.plate = renting.getVehicle().getPlate();
		this.drivingLicense = renting.getDrivingLicense();
		this.rentACarCode = renting.getVehicle().getRentACar().getCode();
		this.begin = renting.getBegin();
		this.end = renting.getEnd();
		this.amount = renting.getAmount();
		this.cancel = renting.getCancel();
		this.cancellationDate = renting.getCancellationDate();
	}

	/**
	 * @return the renting reference
	 */
	public String getReference() {
		return this.reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the vehicle plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate
	 *            the vehicle plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @param drivingLicense
	 *            the drivingLicense to set
	 */
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	/**
	 * @return the rentACarCode
	 */
	public String getRentACarCode() {
		return rentACarCode;
	}

	/**
	 * @param rentACarCode
	 *            the rentACarCode to set
	 */
	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public String getCancel() {
		return this.cancel;
	}
	
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	
	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public void setCancellationDate (LocalDate date) {
		this.cancellationDate = date;
	}
}
