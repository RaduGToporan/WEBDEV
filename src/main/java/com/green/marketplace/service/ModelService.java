package com.green.marketplace.service;

import com.green.marketplace.ModelBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelService {

    private final String dbURL = "jdbc:mysql://127.0.0.1:3306/marketplace";
    private final String dbUser = "webdev";
    private final String dbPassword = "ai_marketplace";

    public void addModel(ModelBean modelItem) {
        String query = "insert into models (modelid, name, trainedprice, untrainedprice, tags)" +
                " values (?, ?, ?, ?, ?); ";
        String idQuery = "select max(modelid) + 1 as id from models;";
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            PreparedStatement preparedStatement = conn.prepareStatement(idQuery);
            ResultSet idResSet = preparedStatement.executeQuery();
            if (idResSet.next()) {
                int newId = idResSet.getInt(1);
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setLong(1, newId);
                preparedStatement.setString(2, modelItem.getName());
                preparedStatement.setDouble(3, modelItem.getTrainedPrice());
                preparedStatement.setDouble(4, modelItem.getUntrainedPrice());
                preparedStatement.setString(5, String.join(",", modelItem.getTags()));
                preparedStatement.execute();
                modelItem.setId(newId);
            }
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    public List<ModelBean> getAllModels(String sortColumn, String sortOrder) {
        List<ModelBean> result = new ArrayList<>();
        String sortString = "";
        if ((sortColumn != null) && (sortOrder != null)) {
            sortString = String.format("order by %s %s", sortColumn, sortOrder);
        }
        String query = String.format("select modelid, name, trainedprice, untrainedprice, tags from models %s;", sortString);
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                ModelBean modelItem = new ModelBean();
                modelItem.setId(res.getInt("modelid"));
                modelItem.setName(res.getString("name"));
                modelItem.setTrainedPrice(res.getDouble("trainedprice"));
                modelItem.setUntrainedPrice(res.getDouble("untrainedprice"));
                modelItem.setTags(Arrays.asList(res.getString("tags").split(",")));
                result.add(modelItem);
            }
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
        return result;
    }

    public void saveModel(ModelBean modelItem) {
        String query = "update models set " +
                "name=?, " +
                "trainedprice=?, " +
                "untrainedprice=?, " +
                "tags=? " +
                "where modelid=?; ";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, modelItem.getName());
            preparedStatement.setDouble(2, modelItem.getTrainedPrice());
            preparedStatement.setDouble(3, modelItem.getUntrainedPrice());
            preparedStatement.setString(4, String.join(",", modelItem.getTags()));
            preparedStatement.setLong(5, modelItem.getId());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }

    public void deleteModel(ModelBean modelItem) {
        String query = "delete from models " +
                "where modelid=?; ";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setLong(1, modelItem.getId());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.err.format("SQL State: %s\n%s", ex.getSQLState(), ex.getMessage());
        }
    }
}