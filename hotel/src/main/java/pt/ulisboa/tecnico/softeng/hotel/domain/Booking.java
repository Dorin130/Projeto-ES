package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;
					
	private final Hotel hotel;
	private final String reference;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final Type type;
	private final String providerNif;
	private final String buyerNif;
	private final String buyerIban;
	private final int price;
	private String paymentReference;
	private String invoiceReference;
	private boolean cancelledInvoice = false;
	private String cancelledPaymentReference = null;
		

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure, Type type, String buyerNif, String buyerIban, int price) {
		checkArguments(hotel, arrival, departure, type, buyerNif, buyerIban, price);

		this.hotel = hotel;
		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
		this.type = type;
		this.providerNif = this.hotel.getNif();
		this.buyerNif = buyerNif;
		this.buyerIban = buyerIban;
		this.price = price;
		
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure, Type type, String buyerNif, String buyerIban, int price) {
		if (hotel == null || arrival == null || departure == null|| type == null|| buyerNif == null || buyerNif.trim().length() == 0||buyerIban == null || buyerIban.trim().length() == 0 || price <=0) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException(); 
		}
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public Type getType() {
		return type;
	}
	
	public Hotel getHotel() {
		return hotel;
	}
	
	public String getProviderNif() {
		return providerNif;
	}
	
	public String getBuyerNif() {
		return buyerNif;
	}
	public String getBuyerIban() {
		return buyerIban;
	}
	
	public String getPaymentReference() {
		return this.paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}

	public boolean isCancelledInvoice() {
		return this.cancelledInvoice;
	}

	public void setCancelledInvoice(boolean cancelledInvoice) {
		this.cancelledInvoice = cancelledInvoice;
	}

	public String getCancelledPaymentReference() {
		return this.cancelledPaymentReference;
	}
	
	public void setCancelledPaymentReference(String cancelledPaymentReference) {
		this.cancelledPaymentReference = cancelledPaymentReference;
	}
	public int getPrice() {
		return price;
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true; 
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		this.cancellation = "CANCEL" + this.reference;
		this.cancellationDate = new LocalDate();
		this.hotel.getProcessor().submitBooking(this);
		
		return this.cancellation; 
	}

	public boolean isCancelled() {
		return this.cancellation != null;
	}

	

}
