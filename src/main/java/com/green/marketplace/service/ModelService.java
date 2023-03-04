package com.green.marketplace.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.marketplace.model.Model;
import com.green.marketplace.order.CartItem;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelService {

    private final String dbURL = "jdbc:mysql://127.0.0.1:3306/marketplace";
    private final String dbUser = "webdev";
    private final String dbPassword = "ai_marketplace";

    public void addModel(Model model) {
        String query = "insert into models (modelid, name, trainedprice, untrainedprice, tags)" +
                " values (?, ?, ?, ?, ?); ";
        String idQuery = "select max(modelid) + 1 as id from models;";
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            PreparedStatement preparedStatement = conn.prepareStatement(idQuery);
            ResultSet idResSet = preparedStatement.executeQuery();
            int newId = idResSet.getInt(1);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, newId);
            preparedStatement.setString(2, model.getName());
            preparedStatement.setDouble(3, model.getTrainedPrice());
            preparedStatement.setDouble(4, model.getUntrainedPrice());
            preparedStatement.setString(5, model.getTags());
            preparedStatement.executeQuery();
            model.setModelId(newId);
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }
}

