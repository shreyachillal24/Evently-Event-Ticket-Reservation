package com.eventticketing.model;

public class Seat {

    private int eventSeatId;
    private int seatId;
    private String seatNumber;
    private String seatType;
    private String seatStatus;

    public Seat(int eventSeatId, int seatId, String seatNumber,
                String seatType, String seatStatus) {

        this.eventSeatId = eventSeatId;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.seatStatus = seatStatus;
    }

	public int getEventSeatId() {
		return eventSeatId;
	}

	public void setEventSeatId(int eventSeatId) {
		this.eventSeatId = eventSeatId;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(String seatStatus) {
		this.seatStatus = seatStatus;
	}
}



