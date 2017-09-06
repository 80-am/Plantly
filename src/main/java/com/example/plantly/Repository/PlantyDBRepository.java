package com.example.plantly.Repository;

import com.example.plantly.Domain.Plant;

public interface PlantyDBRepository {

    boolean addUser(String firstname, String lastname, String email, String password);
    Plant getPlantByPlantSpecies (String plantSpecies);
    String addPlantToUserPlants(String nickName, String photo, int userId, String plantSpecies);

    // här kommer vi skriva metod som implementeras i DB Repository
}
