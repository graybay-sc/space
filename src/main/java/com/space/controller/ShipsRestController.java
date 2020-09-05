package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.model.ShipUtils;
import com.space.repository.CustomShipRepositoryImpl;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/ships")
public class ShipsRestController {
    private final ShipService shipService;

    @GetMapping()
    public List<Ship> getShips(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String planet,
                               @RequestParam(required = false) ShipType shipType,
                               @RequestParam(required = false) Long after,
                               @RequestParam(required = false) Long before,
                               @RequestParam(required = false) Boolean isUsed,
                               @RequestParam(required = false) Double minSpeed,
                               @RequestParam(required = false) Double maxSpeed,
                               @RequestParam(required = false) Integer minCrewSize,
                               @RequestParam(required = false) Integer maxCrewSize,
                               @RequestParam(required = false) Double minRating,
                               @RequestParam(required = false) Double maxRating,
                               @RequestParam(required = false) ShipOrder order,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                               @RequestParam(required = false, defaultValue = "3") Integer pageSize) {

        CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters = new CustomShipRepositoryImpl.ShipQueryParameters(
                null,
                name,
                planet,
                shipType,
                after,
                before,
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                order,
                pageNumber,
                pageSize);

        return shipService.findShips(shipQueryParameters);
    }

    @GetMapping(value = "/count")
    public Integer getShipsCount(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String planet,
                                 @RequestParam(required = false) ShipType shipType,
                                 @RequestParam(required = false) Long after,
                                 @RequestParam(required = false) Long before,
                                 @RequestParam(required = false) Boolean isUsed,
                                 @RequestParam(required = false) Double minSpeed,
                                 @RequestParam(required = false) Double maxSpeed,
                                 @RequestParam(required = false) Integer minCrewSize,
                                 @RequestParam(required = false) Integer maxCrewSize,
                                 @RequestParam(required = false) Double minRating,
                                 @RequestParam(required = false) Double maxRating) {

        CustomShipRepositoryImpl.ShipQueryParameters shipQueryParameters = new CustomShipRepositoryImpl.ShipQueryParameters(
                null,
                name,
                planet,
                shipType,
                after,
                before,
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                null,
                null,
                null);

        return shipService.getCount(shipQueryParameters);
    }

    @PostMapping()
    public Ship createShip(@RequestBody Ship ship,
                           HttpServletResponse response) {

        ship.setId(null);
        if (ship.getUsed() == null) ship.setUsed(false);
        Ship shipResult = shipService.saveShip(ship);
        if (shipResult == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return shipResult;
    }

    @GetMapping(value = "/{id}")
    public Ship findShip(@PathVariable String id, HttpServletResponse response) {
        Long idLong = ShipUtils.stringIdToLong(id);
        if (idLong == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        Ship ship = shipService.findById(idLong);
        if (ship == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return ship;
    }

    @PostMapping(value = "/{id}")
    public Ship updateShip(@PathVariable String id,
                           @RequestBody Ship ship,
                           HttpServletResponse response) {

        //checking id with response
        Long idLong = ShipUtils.stringIdToLong(id);
        if (idLong == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        ship.id = idLong;

        //finding existing ship with response
        Ship currentShip = shipService.findById(idLong);
        if (currentShip == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        ShipUtils.copyNonNullProperties(ship, currentShip);

        Ship shipResult = shipService.saveShip(currentShip);
        if (shipResult == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return shipResult;
    }

    @DeleteMapping(value = "/{id}")
    public void updateShip(@PathVariable String id,
                           HttpServletResponse response) {

        //checking id with response
        Long idLong = ShipUtils.stringIdToLong(id);
        if (idLong == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //finding existing ship with response
        if (!shipService.shipExists(idLong)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        shipService.deleteById(idLong);
    }

    @Autowired
    public ShipsRestController(ShipService shipService) {
        this.shipService = shipService;
    }
}
