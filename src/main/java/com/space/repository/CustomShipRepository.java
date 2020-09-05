package com.space.repository;

import com.space.model.Ship;
import java.util.List;

public interface CustomShipRepository {
    List<Ship> getShips(CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters);
    Integer getShipsCount(CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters);
}