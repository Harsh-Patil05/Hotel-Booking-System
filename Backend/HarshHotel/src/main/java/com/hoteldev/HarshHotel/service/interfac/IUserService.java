package com.hoteldev.HarshHotel.service.interfac;

import com.hoteldev.HarshHotel.dto.LoginRequest;
import com.hoteldev.HarshHotel.dto.Response;
import com.hoteldev.HarshHotel.entity.User;
import org.springframework.stereotype.Service;



public interface IUserService {
    Response register(User loginRequest);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
