<?php
/**
 * Used to set up and fix common variables and include
 * the WordPress procedural and class library.
 *
 * Allows for some configuration in wp-config.php (see default-constants.php)
 *
 * @package WordPress
 */

/**
 * Stores the location of the WordPress directory of functions, classes, and core content.
 *
 * @since 1.0.0
 */
define( 'WPINC', 'wp-includes' );

/**
 * Version information for the current WordPress release.
 *
 * These can't be directly globalized in version.php. When updating,
 * we're including version.php from another installation and don't want
 * these values to be overridden if already set.
 *
 * @global string $wp_version             The WordPress version string.
 * @global int    $wp_db_version          WordPress database version.
 * @global string $tinymce_version        TinyMCE version.
 * @global string $required_php_version   The required PHP version string.
 * @global string $required_mysql_version The required MySQL version string.
 * @global string $wp_local_package       Locale code of the package.
 */
global $wp_version, $wp_db_version, $tinymce_version, $required_php_version, $required_mysql_version, $wp_local_package;
require ABSPATH . WPINC . '/version.php';

require ABSPATH . WPINC . '/load.php';

// Check for the required PHP version and for the MySQL extension or a database drop-in.
wp_check_php_mysql_versions();

// Include files required for initialization.
//require ABSPATH . WPINC . '/class-wp-paused-extensions-storage.php';
//require ABSPATH . WPINC . '/class-wp-fatal-error-handler.php';
//require ABSPATH . WPINC . '/class-wp-recovery-mode-cookie-service.php';
//require ABSPATH . WPINC . '/class-wp-recovery-mode-key-service.php';
//require ABSPATH . WPINC . '/class-wp-recovery-mode-link-service.php';
//require ABSPATH . WPINC . '/class-wp-recovery-mode-email-service.php';
//require ABSPATH . WPINC . '/class-wp-recovery-mode.php';
//require ABSPATH . WPINC . '/error-protection.php';
require ABSPATH . WPINC . '/default-constants.php';
require_once ABSPATH . WPINC . '/plugin.php';

/**
 * If not already configured, `$blog_id` will default to 1 in a single site
 * configuration. In multisite, it will be overridden by default in ms-settings.php.
 *
 * @global int $blog_id
 * @since 2.0.0
 */
global $blog_id;

// Set initial default constants including WP_MEMORY_LIMIT, WP_MAX_MEMORY_LIMIT, WP_DEBUG, SCRIPT_DEBUG, WP_CONTENT_DIR and WP_CACHE.
wp_initial_constants();

// Make sure we register the shutdown handler for fatal errors as soon as possible.
//wp_register_fatal_error_handler();

// WordPress calculates offsets from UTC.
date_default_timezone_set( 'UTC' );

// Turn register_globals off.
//wp_unregister_GLOBALS();

// Standardize $_SERVER variables across setups.
//wp_fix_server_vars();

// Check if we're in maintenance mode.
//wp_maintenance();

// Start loading timer.
//timer_start();

// Check if we're in WP_DEBUG mode.
//wp_debug_mode();

// Define WP_LANG_DIR if not set.
wp_set_lang_dir();

// Load early WordPress files.
require ABSPATH . WPINC . '/compat.php';
require ABSPATH . WPINC . '/class-wp-list-util.php';
require ABSPATH . WPINC . '/formatting.php';
require ABSPATH . WPINC . '/meta.php';
require ABSPATH . WPINC . '/functions.php';
require ABSPATH . WPINC . '/class-wp-meta-query.php';
require ABSPATH . WPINC . '/class-wp-matchesmapregex.php';
require ABSPATH . WPINC . '/class-wp.php';
require ABSPATH . WPINC . '/class-wp-error.php';
require ABSPATH . WPINC . '/pomo/mo.php';

/**
 * @global wpdb $wpdb WordPress database abstraction object.
 * @since 0.71
 */
global $wpdb;
// Include the wpdb class and, if present, a db.php database drop-in.
require_wp_db();

// Set the database table prefix and the format specifiers for database table columns.
$GLOBALS['table_prefix'] = $table_prefix;
wp_set_wpdb_vars();

// Define constants which affect functionality if not already defined.
wp_functionality_constants();

?>
