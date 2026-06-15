package Supplynest.Auth.Service.enums;

public class modelEnums {

    public enum GENDER {
        MALE,
        FEMALE,
        OTHER;

        public static GENDER[] getAllValues() {
            return values();
        }
    }

    public enum AuditAction {
        CREATE,
        UPDATE,
        DELETE,
        LOGIN,
        LOGOUT,
        APPROVE,
        REJECT,
        DOWNLOAD
    }

    public enum ScopeType{
        PLATFORM,
        USER;

        public static ScopeType[] getAllValues() {
            return values();
        }
    }

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
