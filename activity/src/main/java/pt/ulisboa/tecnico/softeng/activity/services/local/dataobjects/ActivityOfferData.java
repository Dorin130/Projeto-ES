package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;

public class ActivityOfferData {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end;
	private Integer capacity;
	private double amount;
	private List<ActivityReservationData> reservations;
	private String reference;
	private String activityName;
	private String activityCode;
	private String providerCode;

	public ActivityOfferData() {
	}

	public ActivityOfferData(ActivityOffer offer) {
		this.begin = offer.getBegin();
		this.end = offer.getEnd();
		this.capacity = offer.getCapacity();
		this.amount = offer.getAmount();
		this.reservations = offer.getBookingSet().stream().map(b -> new ActivityReservationData(b))
				.collect(Collectors.toList());
		this.reference = offer.getReference();
		this.activityName = offer.getActivity().getName();
		this.setActivityCode(offer.getActivity().getCode());
		this.setProviderCode(offer.getActivity().getActivityProvider().getCode());
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public Integer getCapacity() {
		return this.capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public List<ActivityReservationData> getReservations() {
		return this.reservations;
	}

	public void setReservations(List<ActivityReservationData> reservations) {
		this.reservations = reservations;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReference() {
		return this.reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getActivityName() {
		return this.activityName;
	}
	
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityCode() {
		return this.activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getProviderCode() {
		return this.providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
}
