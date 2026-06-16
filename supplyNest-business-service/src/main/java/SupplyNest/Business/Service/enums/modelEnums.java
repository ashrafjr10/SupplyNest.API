package SupplyNest.Business.Service.enums;

public class modelEnums {

    public enum BusinessGroupStatus{
        ACTIVE,
        INACTIVE,
        SUSPENDED;

        public static BusinessGroupStatus[] getAllValues() {
            return values();
        }
    }

    public enum BusinessStatus{
        ACTIVE,
        INACTIVE,
        SUSPENDED;

        public static BusinessStatus[] getAllValues() {
            return values();
        }
    }

    public enum BusinessType {
        DISTRIBUTOR,
        WHOLESALER,
        MANUFACTURER,
        RETAILER;

        public static BusinessType[] getAllValues(){
            return values();
        }
    }
}
