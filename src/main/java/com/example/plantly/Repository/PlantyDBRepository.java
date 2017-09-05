package com.example.plantly.Repository;

public interface PlantyDBRepository {

    boolean addUser(String firstname, String lastname, String email, String password);

    // här kommer vi skriva metod som implementeras i DB Repository
}
