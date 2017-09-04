package com.example.plantly.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

// här vi kommer skriva alla som gäller koppling med databas och

@Component
public class DBRepository implements PlantyDBRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")

    @Autowired
    private DataSource dataSource;


}
