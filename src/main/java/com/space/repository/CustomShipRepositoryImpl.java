package com.space.repository;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class CustomShipRepositoryImpl implements CustomShipRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Ship> getShips(ShipQueryParameters shipQueryParameters) {

        TypedQuery<Ship> query = entityManager.createQuery(shipQueryParameters.generateSelectQuery(), Ship.class);
        shipQueryParameters.setQueryParams(query);

        return query.getResultList();
    }

    @Override
    public Integer getShipsCount(ShipQueryParameters shipQueryParameters) {

        TypedQuery<Long> query = entityManager.createQuery(shipQueryParameters.generateSelectQueryCount(), Long.class);
        shipQueryParameters.setQueryParams(query);
        return query.getSingleResult().intValue();
    }

    public static class ShipQueryParameters {
        private final Long id;
        private final String name;
        private final String planet;
        private final ShipType shipType;
        private final Date after;
        private final Date before;
        private final Boolean isUsed;
        private final Double minSpeed;
        private final Double maxSpeed;
        private final Integer minCrewSize;
        private final Integer maxCrewSize;
        private final Double minRating;
        private final Double maxRating;
        private final ShipOrder order;
        private final Integer pageNumber;
        private final Integer pageSize;

        private String query;

        public ShipQueryParameters(Long id,
                                   String name,
                                   String planet,
                                   ShipType shipType,
                                   Long after,
                                   Long before,
                                   Boolean isUsed,
                                   Double minSpeed,
                                   Double maxSpeed,
                                   Integer minCrewSize,
                                   Integer maxCrewSize,
                                   Double minRating,
                                   Double maxRating,
                                   ShipOrder order,
                                   Integer pageNumber,
                                   Integer pageSize) {

            this.id = id;
            this.name = name;
            this.planet = planet;
            this.shipType = shipType;

            if (after != null) {
                this.after = new Date(after);
            } else {
                this.after = null;
            }

            if (before != null) {
                this.before = new Date(before);
            } else {
                this.before = null;
            }

            this.isUsed = isUsed;
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            this.minCrewSize = minCrewSize;
            this.maxCrewSize = maxCrewSize;
            this.minRating = minRating;
            this.maxRating = maxRating;
            this.order = order;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public String generateSelectQueryCount() {
            return generateSelectQuery(true);
        }

        public String generateSelectQuery() {
            return generateSelectQuery(false);
        }

        public String generateSelectQuery(boolean onlyCount) {
            StringBuilder textOfQuery = new StringBuilder();
            if (onlyCount) {
                textOfQuery.append("SELECT COUNT(S.id) ");
            }
            textOfQuery.append("FROM Ship S WHERE");
            textOfQuery.append(id != null ? String.format(" AND S.%1$s = :%1$s", "id") : "");
            textOfQuery.append(name != null ? String.format(" AND S.%1$s LIKE :%1$s", "name") : "");
            textOfQuery.append(planet != null ? String.format(" AND S.%1$s LIKE :%1$s", "planet") : "");
            textOfQuery.append(shipType != null ? String.format(" AND S.%1$s = :%1$s", "shipType") : "");
            textOfQuery.append(after != null ? String.format(" AND S.%s >= :%s", "prodDate", "after") : "");
            textOfQuery.append(before != null ? String.format(" AND S.%s <= :%s", "prodDate", "before") : "");
            textOfQuery.append(isUsed != null ? String.format(" AND S.%1$s = :%1$s", "isUsed") : "");
            textOfQuery.append(minSpeed != null ? String.format(" AND S.%s >= :%s", "speed", "minSpeed") : "");
            textOfQuery.append(maxSpeed != null ? String.format(" AND S.%s <= :%s", "speed", "maxSpeed") : "");
            textOfQuery.append(minCrewSize != null ? String.format(" AND S.%s >= :%s", "crewSize", "minCrewSize") : "");
            textOfQuery.append(maxCrewSize != null ? String.format(" AND S.%s <= :%s", "crewSize", "maxCrewSize") : "");
            textOfQuery.append(minRating != null ? String.format(" AND S.%s >= :%s", "rating", "minRating") : "");
            textOfQuery.append(maxRating != null ? String.format(" AND S.%s <= :%s", "rating", "maxRating") : "");
            if (!onlyCount) {
                textOfQuery.append(order != null ? String.format(" ORDER BY S.%1s ASC", order.getFieldName()) : "");
            }

            query = textOfQuery.toString();
            query = query.replaceFirst(" WHERE$", "");
            query = query.replaceFirst("WHERE AND", "WHERE ");
            query = query.replaceFirst("WHERE ORDER", " ORDER");
            return query;
        }

        //public <T> void setQueryParams(TypedQuery<T> query) {
        public void setQueryParams(Query query) {
            if (pageNumber != null && pageSize != null) {
                query.setFirstResult(pageNumber * pageSize);
                query.setMaxResults(pageSize);
            }

            for (Parameter<?> param : query.getParameters()) {
                String paramName = param.getName();
                switch (paramName) {
                    case "name":
                        query.setParameter(paramName, "%" + name + "%");
                        break;
                    case "planet":
                        query.setParameter(paramName, "%" + planet + "%");
                        break;
                    case "shipType":
                        query.setParameter(paramName, shipType);
                        break;
                    case "after":
                        query.setParameter(paramName, after);
                        break;
                    case "before":
                        query.setParameter(paramName, before);
                        break;
                    case "isUsed":
                        query.setParameter(paramName, isUsed);
                        break;
                    case "minSpeed":
                        query.setParameter(paramName, minSpeed);
                        break;
                    case "maxSpeed":
                        query.setParameter(paramName, maxSpeed);
                        break;
                    case "minCrewSize":
                        query.setParameter(paramName, minCrewSize);
                        break;
                    case "maxCrewSize":
                        query.setParameter(paramName, maxCrewSize);
                        break;
                    case "minRating":
                        query.setParameter(paramName, minRating);
                        break;
                    case "maxRating":
                        query.setParameter(paramName, maxRating);
                        break;
                }
            }
        }
    }
}
