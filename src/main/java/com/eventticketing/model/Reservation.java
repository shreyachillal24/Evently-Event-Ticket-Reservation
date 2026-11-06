package com.eventticketing.model;

import java.sql.Timestamp;

public class Reservation {

	    private int reservationId;
	    private int userId;
	    private int eventSeatId;
	    private String reservationStatus;
	    private Timestamp reservedAt;
	    private Timestamp expiresAt;
	    private String paymentStatus;

	    public Reservation(int reservationId, int userId,
	                       int eventSeatId, String reservationStatus,
	                       Timestamp reservedAt, Timestamp expiresAt,String paymentStatus) {

	        this.setReservationId(reservationId);
	        this.setUserId(userId);
	        this.setEventSeatId(eventSeatId);
	        this.setReservationStatus(reservationStatus);
	        this.setReservedAt(reservedAt);
	        this.setExpiresAt(expiresAt);
	        this.setPaymentStatus(paymentStatus);
	    }

		public int getReservationId() {
			return reservationId;
		}

		public void setReservationId(int reservationId) {
			this.reservationId = reservationId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getEventSeatId() {
			return eventSeatId;
		}

		public void setEventSeatId(int eventSeatId) {
			this.eventSeatId = eventSeatId;
		}

		public String getReservationStatus() {
			return reservationStatus;
		}

		public void setReservationStatus(String reservationStatus) {
			this.reservationStatus = reservationStatus;
		}

		public Timestamp getReservedAt() {
			return reservedAt;
		}

		public void setReservedAt(Timestamp reservedAt) {
			this.reservedAt = reservedAt;
		}

		public Timestamp getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Timestamp expiresAt) {
			this.expiresAt = expiresAt;
		}

		public String getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(String paymentStatus) {
			this.paymentStatus = paymentStatus;
		}
	}

