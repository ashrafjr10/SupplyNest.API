package SupplyNest.Common.enums;

public class ModelEnums {

    public enum RoleTypes{
        SUPER_ADMIN,
        ADMIN,
        BUSINESS_OWNER,
        BUSINESS_STAFF,
        CUSTOMER;

        public static RoleTypes[] getAllValues() {
            return values();
        }
    }
}
