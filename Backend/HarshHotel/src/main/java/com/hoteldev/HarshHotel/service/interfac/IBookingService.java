package com.hoteldev.HarshHotel.service.interfac;

import com.hoteldev.HarshHotel.dto.Response;
import com.hoteldev.HarshHotel.entity.Booking;
import org.springframework.stereotype.Service;


public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
