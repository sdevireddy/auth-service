-- STEP 1: Copy Modules
INSERT INTO modules (id, name, module_key, description)
SELECT id, name, module_key, description
FROM common.modules;

-- STEP 2: Copy Features
INSERT INTO features (id, name, description, module_id)
SELECT f.id, f.name, f.description, f.module_id
FROM common.features f;

-- STEP 3: Copy Permissions
INSERT INTO permissions (id, name)
SELECT id, name FROM common.permissions;

-- STEP 4: Insert Admin Role
INSERT INTO roles (id, name)
VALUES (1, 'Admin');

-- STEP 5: Create Feature-Permission combinations (view, edit, create, delete for each feature)
INSERT INTO feature_permissions (feature_id, permission_id)
SELECT f.id, p.id
FROM tenant_xyz.features f
JOIN tenant_xyz.permissions p;

-- STEP 6: Assign all feature_permissions to Admin role
INSERT INTO role_feature_permissions (role_id, feature_id, permission_id)
SELECT 1 AS role_id, fp.feature_id, fp.permission_id
FROM tenant_xyz.feature_permissions fp;

-- STEP 7 (Optional): Assign Admin role to user (if user exists)
-- Replace :user_id with the actual user ID
-- INSERT INTO tenant_xyz.user_roles (user_id, role_id) VALUES (:user_id, 1);
