package com.eventticketing.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Event {
	

	    private int eventId;
	    private int venueId;
	    private String eventName;
	    private Timestamp eventDate;
	    private String eventDescription;
	    private BigDecimal ticketPrice;
	    private String eventStatus;

	    public Event(int eventId, int venueId, String eventName,
	                 Timestamp eventDate, String eventDescription,
	                 BigDecimal ticketPrice, String eventStatus) {
	        this.setEventId(eventId);
	        this.setVenueId(venueId);
	        this.setEventName(eventName);
	        this.setEventDate(eventDate);
	        this.setEventDescription(eventDescription);
	        this.setTicketPrice(ticketPrice);
	        this.setEventStatus(eventStatus);
	    }

		public int getEventId() {
			return eventId;
		}

		public void setEventId(int eventId) {
			this.eventId = eventId;
		}

		public int getVenueId() {
			return venueId;
		}

		public void setVenueId(int venueId) {
			this.venueId = venueId;
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName;
		}

		public Timestamp getEventDate() {
			return eventDate;
		}

		public void setEventDate(Timestamp eventDate) {
			this.eventDate = eventDate;
		}

		public String getEventDescription() {
			return eventDescription;
		}

		public void setEventDescription(String eventDescription) {
			this.eventDescription = eventDescription;
		}

		public BigDecimal getTicketPrice() {
			return ticketPrice;
		}

		public void setTicketPrice(BigDecimal ticketPrice) {
			this.ticketPrice = ticketPrice;
		}

		public String getEventStatus() {
			return eventStatus;
		}

		public void setEventStatus(String eventStatus) {
			this.eventStatus = eventStatus;
		}

	  
	}
