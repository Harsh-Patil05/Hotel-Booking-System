package com.hoteldev.HarshHotel.service.impl;

import com.hoteldev.HarshHotel.dto.Response;
import com.hoteldev.HarshHotel.dto.RoomDTO;
import com.hoteldev.HarshHotel.entity.Room;
import com.hoteldev.HarshHotel.exception.OurException;
import com.hoteldev.HarshHotel.repo.BookingRepository;
import com.hoteldev.HarshHotel.repo.RoomRepository;
import com.hoteldev.HarshHotel.service.interfac.IRoomService;
import com.hoteldev.HarshHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;




    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response=new Response();

        try {
            String imageUrl = saveFileLocally(photo);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntitytoRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response=new Response();

        try {
            List<Room> roomList=roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> roomDTOList=Utils.mapRoomListEnityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response=new Response();

        try {
            roomRepository.findById(roomId).orElseThrow(()->new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }catch(OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomType,String description, BigDecimal roomPrice, MultipartFile photo) {
        Response response=new Response();

        try {
            String ImageUrl=null;
            if (photo != null && !photo.isEmpty()) {
                ImageUrl = saveFileLocally(photo);
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room Not Found"));

            if(roomType!=null)
            {
                room.setRoomType(roomType);
            }
            if(description!=null)
            {
                room.setRoomDescription(description);
            }
            if(ImageUrl!=null)
            {
                room.setRoomPhotoUrl(ImageUrl);
            }
            if(roomPrice!=null)
            {
                room.setRoomPrice(roomPrice);
            }

            Room updatedRoom=roomRepository.save(room);
            RoomDTO roomDTO=Utils.mapRoomEntitytoRoomDTO(updatedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch(OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response=new Response();

        try {
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room Not Found"));
            RoomDTO roomDTO=Utils.mapRoomEntitytoRoomDTOplusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }catch(OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response=new Response();

        try {
            List<Room> availableRooms=roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate,roomType);
            List<RoomDTO> roomDTOList=Utils.mapRoomListEnityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }
         catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting a room"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response=new Response();

        try {
            List<Room> roomList=roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList=Utils.mapRoomListEnityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }catch(OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting rooms"+e.getMessage());
        }
        return response;
    }

    private String saveFileLocally(MultipartFile file) throws IOException {
        String uploadsDir = "uploads/";
        File uploadsFolder = new File(uploadsDir);
        if (!uploadsFolder.exists()) {
            uploadsFolder.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadsDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + fileName;
    }
}
