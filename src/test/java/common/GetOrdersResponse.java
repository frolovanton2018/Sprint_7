package common;

import java.util.List;

public class GetOrdersResponse {
    public List<OrderInfo> orders;
    public PageInfo pageInfo;
    public List<Station> availableStations;

    public static class OrderInfo {
        public int id;
        public String courierId;
        public String firstName;
        public String lastName;
        public String address;
        public String metroStation;
        public String phone;
        public int rentTime;
        public String deliveryDate;
        public int track;
        public String[] color;
        public String comment;
        public String createdAt;
        public String updatedAt;
        public int status;
    }

    public static class PageInfo {
        public int page;
        public int total;
        public int limit;
    }

    public static class Station {
        public String name;
        public String number;
        public String color;
    }
}
