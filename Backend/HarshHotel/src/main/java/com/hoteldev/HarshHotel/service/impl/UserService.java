package com.hoteldev.HarshHotel.service.impl;


import com.hoteldev.HarshHotel.dto.LoginRequest;
import com.hoteldev.HarshHotel.dto.Response;
import com.hoteldev.HarshHotel.dto.UserDTO;
import com.hoteldev.HarshHotel.entity.User;
import com.hoteldev.HarshHotel.exception.OurException;
import com.hoteldev.HarshHotel.repo.UserRepository;
import com.hoteldev.HarshHotel.service.interfac.IUserService;
import com.hoteldev.HarshHotel.utils.JWTUtils;
import com.hoteldev.HarshHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response=new Response();
        try{
            if(user.getRole()==null || user.getRole().isBlank())
            {
                user.setRole("USER");
            }
            if(userRepository.existsByEmail((user.getEmail())))
            {
                throw new OurException((user.getEmail())+"Already Exist!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser=userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntitytoUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registeration"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response=new Response();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("User Not Found"));
            var token=jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful!");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response=new Response();
        try{
            List<User> userList=userRepository.findAll();
            List<UserDTO> userDTOList=Utils.mapUserListEnityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful!");
            response.setUserList(userDTOList);
        }catch(OurException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During fetching Users"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();
        try {
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntitytoUserDTOPlusUserBookingAndRoom(user);

            response.setStatusCode(200);
            response.setMessage("Successful!");
            response.setUser(userDTO);
        } catch (OurException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During fetching Booking History"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response=new Response();
        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatusCode(200);
            response.setMessage("Successful!");
        } catch (OurException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During deleting User"+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response=new Response();
        try {
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntitytoUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful!");
            response.setUser(userDTO);
        } catch (OurException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During fetching user Id"+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response=new Response();
        try {
            User user=userRepository.findByEmail(email).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntitytoUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Successful!");
            response.setUser(userDTO);
        } catch (OurException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch(Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Occurred During fetching user Id"+e.getMessage());
        }
        return response;
    }
}
