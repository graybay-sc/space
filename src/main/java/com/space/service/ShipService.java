package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipUtils;
import com.space.repository.CustomShipRepositoryImpl;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public Ship findById(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    public List<Ship> findShips(CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters) {
        return shipRepository.getShips(shipQueryParameters);
    }

    public Ship saveShip(Ship ship) {
        if (!ShipUtils.validateShip(ship)) {
            return null;
        } else {
            ship.setRating(ShipUtils.calcRating(ship));
            return shipRepository.save(ship);
        }
    }

    public boolean shipExists (Long id) {
        return shipRepository.existsById(id);
    }

    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    public Integer getCount(CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters) {
        return shipRepository.getShipsCount(shipQueryParameters);
    }
}
