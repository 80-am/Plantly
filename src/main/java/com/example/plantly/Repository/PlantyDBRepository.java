package com.example.plantly.Repository;

import com.example.plantly.Domain.Plant;
import com.example.plantly.Domain.User;

public interface PlantyDBRepository {

    boolean addUser(String firstname, String lastname, String email, String password);
    Plant getPlantByPlantSpecies (String plantSpecies);
    String addPlantToUserPlants(String nickName, String photo, int userId, String plantSpecies);
    User getCurrentUser(String email, String password);
    boolean userExists(String email, String password);

    // här kommer vi skriva metod som implementeras i DB Repository
}
