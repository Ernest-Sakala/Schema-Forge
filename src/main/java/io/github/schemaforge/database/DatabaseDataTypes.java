package io.github.schemaforge.database;

/**
 * Interface defining SQL data types commonly used in database migrations.
 * @author Ernest Sakala
 */
public interface DatabaseDataTypes {

    /**
     * Represents a fixed-point decimal number.
     */
    String DECIMAL = "DECIMAL";

    /**
     * Represents a variable-length character string.
     */
    String VARCHAR = "VARCHAR";

    /**
     * Represents a large integer.
     */
    String BIGINT = "BIGINT";

    /**
     * Represents a boolean value (true or false).
     */
    String BOOLEAN = "BOOLEAN";

    /**
     * Represents a timestamp with timezone information.
     */
    String TIMESTAMP = "TIMESTAMP";

    /**
     * Represents a fixed-length character string.
     */
    String CHAR = "CHAR";

    /**
     * Represents a text string with variable length.
     */
    String TEXT = "TEXT";

    /**
     * Represents a single-precision floating point number.
     */
    String FLOAT = "FLOAT";

    /**
     * Represents an enumeration type.
     */
    String ENUM = "ENUM";

    /**
     * Represents a JSON (JavaScript Object Notation) data type.
     */
    String JSON = "JSON";

    /**
     * Represents a JSONB (binary JSON) data type.
     */
    String JSONB = "JSONB";

    /**
     * Represents a universally unique identifier.
     */
    String UUID = "UUID";

    /**
     * Represents a 4-byte integer.
     */
    String INTEGER = "INTEGER";

    /**
     * Represents a date without time information.
     */
    String DATE = "DATE";

    /**
     * Represents a time without timezone information.
     */
    String TIME = "TIME";

    /**
     * Represents a date and time.
     */
    String DATETIME = "DATETIME";

    /**
     * Represents a variable-length binary string.
     */
    String VARBINARY = "VARBINARY";

    /**
     * Represents a fixed-length binary string.
     */
    String BINARY = "BINARY";

    /**
     * Represents a variable-length binary string.
     */
    String BYTEA = "BYTEA";
}
