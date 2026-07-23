package com.justcatering.superadmin.constants;

/**
 * Application-wide constant values.
 * <p>
 * Prefer referencing these constants over hard-coded string literals.
 * </p>
 */
public final class AppConstants {

    private AppConstants() {
        // Utility class
    }

    /** API base path prefix. */
    public static final String API_BASE = "/api";

    /** Authentication API base path. */
    public static final String AUTH_API = API_BASE + "/auth";

    /** User management API base path. */
    public static final String USER_API = API_BASE + "/users";

    /** Role management API base path. */
    public static final String ROLE_API = API_BASE + "/roles";

    /** Permission API base path. */
    public static final String PERMISSION_API = API_BASE + "/permissions";

    /** Department management API base path. */
    public static final String DEPARTMENT_API = API_BASE + "/departments";

    /** Product management API base path. */
    public static final String PRODUCT_API = API_BASE + "/products";

    /** Client management API base path. */
    public static final String CLIENT_API = API_BASE + "/clients";

    /** Meeting lead management API base path. */
    public static final String LEAD_API = API_BASE + "/leads";

    /** Follow-up management API base path. */
    public static final String FOLLOW_UP_API = API_BASE + "/follow-ups";

    /** Client query / requirement API base path. */
    public static final String QUERY_API = API_BASE + "/queries";

    /** On-site requirement API base path. */
    public static final String REQUIREMENT_API = API_BASE + "/requirements";

    /** On-site manager task plan API base path. */
    public static final String ONSITE_TASK_PLAN_API = API_BASE + "/onsite-task-plans";

    /** Expense management API base path. */
    public static final String EXPENSE_API = API_BASE + "/expenses";

    /** Payment management API base path. */
    public static final String PAYMENT_API = API_BASE + "/payments";

    /** Project operations API base path. */
    public static final String PROJECT_OPS_API = API_BASE + "/project-ops";

    /** File upload and download API base path. */
    public static final String FILE_API = API_BASE + "/files";

    /** Dashboard API base path. */
    public static final String DASHBOARD_API = API_BASE + "/dashboard";

    /** Calendar API base path. */
    public static final String CALENDAR_API = API_BASE + "/calendar";

    /** Bearer token prefix. */
    public static final String BEARER_PREFIX = "Bearer ";

    /** Authorization header name. */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** Default page number for pagination. */
    public static final int DEFAULT_PAGE = 0;

    /** Default page size for pagination. */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** Maximum allowed page size. */
    public static final int MAX_PAGE_SIZE = 100;

    /** Super Admin role code. */
    public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";

    /** Admin role code. */
    public static final String ROLE_ADMIN = "ADMIN";

    /** UTC timezone identifier. */
    public static final String TIMEZONE_UTC = "UTC";
}
