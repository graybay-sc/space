package com.space.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ShipUtils {
    private ShipUtils() {}

    public static void copyNonNullProperties(Ship source, Ship target) {
        if (source.getId() != null) target.setId(source.getId());
        if (source.getName() != null) target.setName(source.getName());
        if (source.getPlanet() != null) target.setPlanet(source.getPlanet());
        if (source.getShipType() != null) target.setShipType(source.getShipType());
        if (source.getProdDate() != null) target.setProdDate(source.getProdDate());
        if (source.getUsed() != null) target.setUsed(source.getUsed());
        if (source.getSpeed() != null) target.setSpeed(source.getSpeed());
        if (source.getCrewSize() != null) target.setCrewSize(source.getCrewSize());
        if (source.getRating() != null) target.setRating(source.getRating());
        //BeanUtils.copyProperties(source, target, String[] ignoreProperties);
    }

    public static boolean validateShip(Ship ship) {
        String name = ship.getName();
        if (name == null || name.isEmpty() || name.length() > 50) {
            return false;
        }

        String planet = ship.getPlanet();
        if (planet == null || planet.isEmpty() || planet.length() > 50) {
            return false;
        }

        ShipType shipType = ship.getShipType();
        if (shipType == null) {
            return false;
        }

        Date prodDate = ship.getProdDate();
        if (prodDate == null || prodDate.getTime() < 0) {
            return false;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(prodDate);
        int year = calendar.get(Calendar.YEAR);
        if (year < 2800 || year > 3019) {
            return false;
        }

        if (ship.getUsed() == null) {
            return false;
        }

        Double speed = ship.getSpeed();
        if (speed == null || speed < 0.01 || speed > 0.99) {
            return false;
        }

        Integer crewSize = ship.getCrewSize();
        if (crewSize == null || crewSize < 1 || crewSize > 9999) {
            return false;
        }

        return true;
    }

    public static Long stringIdToLong(String id) {
        Long idLong = null;
        try {
            idLong = Long.valueOf(id);
            if (idLong <= 0) {
                idLong = null;
            }
        } catch (NumberFormatException ignored) {}

        return idLong;
    }

    public static double calcRating(Ship ship) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        double result = (80.0 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1.0)) / (double) (3019 - year + 1);
        result = Math.round(result * 100.0) / 100.0;

        return result;
    }
}
