package com.zen.auth.config;



import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModulePermissionRegistry {

    public static final Map<String, List<String>> MODULE_PERMISSIONS = Map.of(
        "crm", List.of(
            "create", "view", "edit", "delete",
            "assign", "reassign", "convert_lead", "send_email", "send_sms",
            "import", "export", "comment", "follow", "archive", "restore",
            "share", "duplicate", "download", "upload", "print",
            "configure_module", "create_reports", "view_reports", "schedule_reports"
        ),
        "hr", List.of(
            "create", "view", "edit", "delete",
            "approve", "reject", "assign", "upload", "download", "import", "export",
            "comment", "manage_users", "manage_roles", "manage_permissions",
            "configure_module", "view_audit_logs", "create_reports", "view_reports"
        ),
        "campaigns", List.of(
            "create", "view", "edit", "delete",
            "send_email", "send_sms", "import", "export",
            "comment", "duplicate", "schedule_reports", "create_reports", "view_reports",
            "configure_module", "manage_workflows"
        ),
        "learning", List.of(
            "create", "view", "edit", "delete",
            "upload", "download", "comment", "rate_course",
            "assign", "configure_module", "view_reports", "issue_certificate",
            "create_reports", "schedule_reports", "manage_users", "manage_roles"
        ),
        "projects", List.of(
            "create", "view", "edit", "delete",
            "assign", "track_time", "close_task", "reopen_task",
            "move_stage", "comment", "upload", "download", "share",
            "configure_module", "create_reports", "view_reports", "manage_workflows"
        ),
        "support", List.of(
            "view", "edit", "delete",
            "assign_ticket", "reassign", "resolve_ticket", "escalate_ticket",
            "rate_ticket", "comment", "upload", "download",
            "view_audit_logs", "configure_module", "manage_workflows",
            "view_reports", "create_reports", "schedule_reports"
        ),
        "accounting", List.of(
            "generate_invoice", "record_payment", "issue_refund",
            "create", "view", "edit", "delete",
            "import", "export", "approve", "reject",
            "upload", "download", "print", "share",
            "configure_module", "view_reports", "create_reports", "schedule_reports",
            "manage_integrations", "view_audit_logs"
        )
    );

    public static List<String> getPermissionsForModule(String moduleKey) {
        return MODULE_PERMISSIONS.getOrDefault(moduleKey, Collections.emptyList());
    }

    public static boolean isPermissionAllowed(String moduleKey, String permission) {
        return MODULE_PERMISSIONS.getOrDefault(moduleKey, List.of()).contains(permission);
    }
}
