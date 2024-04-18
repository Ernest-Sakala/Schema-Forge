package io.github.schemaforge.schema;


import io.github.schemaforge.database.DatabaseConstants;
import io.github.schemaforge.database.DatabaseDataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class TableBuilder implements TableSchema {

    private static final Logger log = LoggerFactory.getLogger(SchemaBuilder.class);

    protected Map<String, String> columnDefinitions;

    private Stack<String> columnNamesStack;

    protected List<String> columnNames;

    private ColumnBuilder columnBuilder;

    public TableBuilder() {
       this.columnDefinitions = new HashMap<>();
       this.columnNames = new ArrayList<>();
       this.columnNamesStack = new Stack<>();
        this.columnBuilder = new ColumnBuilder();
    }

    public ColumnBuilder addDoubleColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addDoubleColumn(columnName);
        return columnBuilder;
    }

    public ColumnBuilder addDecimalColumn(String columnName, int  precision) {
        checkColumnValidity(columnName);
        columnBuilder.addDecimalColumn(columnName,precision);
        return columnBuilder;
    }


    public ColumnBuilder addDecimalColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addDecimalColumn(columnName);
        return columnBuilder;
    }

    public ColumnBuilder addDecimalColumn(String columnName, int  precision, int scale) {
        checkColumnValidity(columnName);
        columnBuilder.addDecimalColumn(columnName,precision,scale);
        return columnBuilder;
    }


    public ColumnBuilder addStringColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addStringColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addStringColumn(String columnName, int length) {
        checkColumnValidity(columnName);
        columnBuilder.addStringColumn(columnName, length);
        return this.columnBuilder;
    }


    @Override
    public ColumnBuilder addEnumColumn(String columnName,String [] enumNames) {
        checkColumnValidity(columnName);
        columnBuilder.addEnumColumn(columnName,enumNames);
        return this.columnBuilder;
    }


    @Override
    public ColumnBuilder addJsonColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addJsonColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addIntegerColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addIntegerColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addBigIntegerColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addBigIntegerColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addDateColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addDateColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addTimeColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addTimeColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addDateTimeColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addDateTimeColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public ColumnBuilder addTimestampColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addTimeStampColumn(columnName);
        return this.columnBuilder;
    }


    @Override
    public ColumnBuilder addBinaryColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addBinaryColumn(columnName);
        return this.columnBuilder;
    }


    @Override
    public ColumnBuilder addJsonbColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addJsonbColumn(columnName);
        return this.columnBuilder;
    }


    @Override
    public ColumnBuilder addUuidColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addUuidColumn(columnName);
        return this.columnBuilder;
    }

    @Override
    public void id() {
        this.columnBuilder.id();
    }

    @Override
    public void id(String columnName) {
        checkColumnValidity(columnName);
        this.columnBuilder.id(columnName);
    }


    @Override
    public ColumnBuilder addForeignId(String columnName) {
        checkColumnValidity(columnName);
        return this.columnBuilder.addForeignId(columnName);
    }

    public ColumnBuilder addCharColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addCharColumn(columnName);
        return this.columnBuilder;
    }

    public ColumnBuilder addCharColumn(String columnName, int size) {
        checkColumnValidity(columnName);
        columnBuilder.addCharColumn(columnName, size);
        return this.columnBuilder;
    }




    public ColumnBuilder addBooleanColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addBooleanColumn(columnName);
        return this.columnBuilder;
    }


    public ColumnBuilder addBigTimeStampColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addTimeStampColumn(columnName);
        return this.columnBuilder;
    }


    public void addTableColumnNames(String[] columnName) {
        columnNames.addAll(Arrays.asList(columnName));
    }


    protected String createTable() {
        StringBuilder columnDefinitions = new StringBuilder();
        for (Map.Entry<String, String> entry : this.columnDefinitions.entrySet()) {
            columnDefinitions.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        if (columnDefinitions.length() > 2) {
            columnDefinitions.setLength(columnDefinitions.length() - 2);
        }
        return columnDefinitions.toString();
    }



    private void checkColumnValidity(String columnName){

        if(columnName == null) {
            throw new NullPointerException("Column Name Cannot be null");
        }

        if (columnName.isEmpty()) {
            throw new IllegalArgumentException("Column Name Cannot be empty");
        }

        if (!columnName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("Column name should only contain letters, digits, or underscores and must start with a letter.");
        }

    }

    /**
     * @param columnName name of the column to add
     * @return ColumnBuilder
     */
    @Override
    public ColumnBuilder addTextColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addTextColumn(columnName);
        return this.columnBuilder;
    }

    /**
     * @param columnName name of the column to add
     * @return ColumnBuilder
     */
    @Override
    public ColumnBuilder addFloatColumn(String columnName) {
        checkColumnValidity(columnName);
        columnBuilder.addFloatColumn(columnName);
        return this.columnBuilder;
    }


    public class ColumnBuilder extends ColumnSchema {
        private final Stack<String> columnOrder;

        private ColumnSchemaBluePrint columnSchemaBluePrint;

        public ColumnBuilder() {
            this.columnOrder = new Stack<>();
            this.getDatabaseType();
        }


        private void checkColumnValidity(String columnName) {
            if (columnName == null || columnName.isEmpty()) {
                throw new IllegalArgumentException("Column Name Cannot be null or empty");
            }
            if (!columnName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                throw new IllegalArgumentException("Column name should only contain letters, digits, or underscores and must start with a letter.");
            }
        }


        public ColumnBuilder defaultValue(String value) {

            // perfoming validation to accept other data types
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition = columnDefinitions.get(columnName).concat(" DEFAULT ").concat("'").concat(value).concat("'");

            columnDefinitions.put(columnName,columnDefinition);

            return this;
        }


        public ColumnBuilder constraint(String tableName) {

            // perfoming validation to accept other data types
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition =  " FOREIGN KEY (" + columnDefinitions.get(columnName) +")"
                    .concat(" REFERENCES ").concat(tableName).concat("(").concat(DatabaseConstants.ID).concat(")");

            columnDefinitions.put(columnName,columnDefinition);

            return this;
        }


        public ColumnBuilder constraint(String tableName, String tablePrimaryKey) {

            // perfoming validation to accept other data types
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition =  " FOREIGN KEY (" + columnDefinitions.get(columnName) +")"
                    .concat(" REFERENCES ").concat(tableName).concat("(").concat(tablePrimaryKey).concat(")");

            columnDefinitions.put(columnName,columnDefinition);

            return this;
        }

        public ColumnBuilder onDelete(String cascadeType) {


            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition;
            if(cascadeType.equalsIgnoreCase("null")) {

              columnDefinition =  columnDefinitions.get(columnName).concat(" ON DELETE SET ").concat(cascadeType);

            }else {

               columnDefinition = columnDefinitions.get(columnName).concat(" ON DELETE ").concat(cascadeType);

            }

            columnDefinitions.put(columnName, columnDefinition);

            return this;
        }


        public ColumnBuilder nullable(boolean nullable) {
            String columnName = columnOrder.isEmpty() ? null : columnOrder.peek();
            if (columnName == null) {
                throw new IllegalStateException("No column has been added yet.");
            }

            String columnDefinition = nullable ?  columnDefinitions.get(columnName).concat(" NULL") :  columnDefinitions.get(columnName).concat(" NOT NULL ");
            columnDefinitions.put(columnName,columnDefinition);

            return this;
        }

        @Override
        public ColumnBuilder unique(boolean unique) {
            return null;
        }


        @Override
        protected void addDoubleColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL);
            columnOrder.push(columnName);
        }

        @Override
        protected void addStringColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.VARCHAR);
            columnOrder.push(columnName);
        }


        private void addStringColumn(String columnName, int size) {
            columnDefinitions.put(columnName, DatabaseDataTypes.VARCHAR + " (" + size + ") " );
            columnOrder.push(columnName);
        }

        private void addCharColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.CHAR);
            columnOrder.push(columnName);
        }

        private void addCharColumn(String columnName, int size) {
            columnDefinitions.put(columnName, DatabaseDataTypes.CHAR + " (" + size + ") " );
            columnOrder.push(columnName);
        }

        @Override
        protected void addBigIntegerColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.BIGINT);
            columnOrder.push(columnName);
        }

        protected void addBooleanColumn(String columnName) {
            checkColumnValidity(columnName);
            columnDefinitions.put(columnName, DatabaseDataTypes.BOOLEAN);
            columnOrder.push(columnName);
        }

        @Override
        protected void addBlobColumn(String columnName) {

        }

        @Override
        protected void addIntegerColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.INTEGER);
            columnOrder.push(columnName);
        }


        private void addTimeStampColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.TIMESTAMP);
            columnOrder.push(columnName);
        }

        private void addBinaryColumn(String columnName) {
            String columnDefinition = columnSchemaBluePrint.binaryDefinition();
            columnDefinitions.put(columnName, columnDefinition);
            columnOrder.push(columnName);
        }


        private void addDecimalColumn(String columnName, int precision, int scale) {
            if(precision == 0 || scale == 0){
                throw new IllegalStateException("Precision or Scale cannot be zero");
            }
            columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL.concat("(" + precision + "," + scale + ")"));
            columnOrder.push(columnName);
        }


        private void addDecimalColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL);
            columnOrder.push(columnName);
        }


        private void addDecimalColumn(String columnName,int precision) {
            if(precision == 0){
                throw new IllegalStateException("Precision cannot be zero");
            }
            columnDefinitions.put(columnName, DatabaseDataTypes.DECIMAL.concat("("+precision +")"));
            columnOrder.push(columnName);
        }


        /**
         * @param columnName name of the column to add
         */
        @Override
        protected void addTextColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.TEXT);
            columnOrder.push(columnName);
        }

        /**
         * @param columnName name of the column to add
         */
        @Override
        protected void addFloatColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.FLOAT);
            columnOrder.push(columnName);
        }


        @Override
        protected void addEnumColumn(String columnName,  String [] enumNames) {

            StringBuilder name = new StringBuilder(DatabaseDataTypes.ENUM + "(");

            for(int x = 0; x < enumNames.length; x++){
               name.append("\'").append(enumNames[x]).append("\'").append(",");
            }

            if (name.length() > 1) {
                name.setLength(name.length() - 1);
            }

            name.append(")");

            columnDefinitions.put(columnName, name.toString());
            columnOrder.push(columnName);
        }


        @Override
        protected void addJsonColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.JSON);
            columnOrder.push(columnName);
        }


        @Override
        protected void addJsonbColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.JSONB);
            columnOrder.push(columnName);
        }

        @Override
        protected void addUuidColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.UUID);
            columnOrder.push(columnName);
        }

        @Override
        protected void addDateColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.DATE);
            columnOrder.push(columnName);
        }

        @Override
        protected void addTimeColumn(String columnName) {
            columnDefinitions.put(columnName, DatabaseDataTypes.TIME);
            columnOrder.push(columnName);
        }

        @Override
        protected void addDateTimeColumn(String columnName) {
            String columnDefinition = this.columnSchemaBluePrint.dateTimeDefinition();
            columnDefinitions.put(columnName, columnDefinition);
            columnOrder.push(columnName);
        }


        private ColumnSchemaBluePrint getDatabaseType() {
            try {
                StringBuilder jsonContent = new StringBuilder();

                FileReader reader = new FileReader("src/main/resources/forge.json");

                int character;
                while ((character = reader.read()) != -1) {
                    jsonContent.append((char) character);
                }
                reader.close();

                String jsonString = jsonContent.toString();

                String database = extractValue(jsonString, "database");

                if(database.isEmpty()){
                    throw new IllegalArgumentException("Schema forge database type should not be empty");
                }


                if(database.equalsIgnoreCase("MYSQL")){
                    log.info("MYSQL SCHEMA TO BE USED");
                    columnSchemaBluePrint = new ColumnSchemaBluePrintMySQL();
                }else if(database.equalsIgnoreCase("POSTGRESQL")){
                    log.info("POSTGRESQL SCHEMA TO BE USED");
                    columnSchemaBluePrint = new ColumnSchemaBluePrintPostgresSQL();
                }

                return columnSchemaBluePrint;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        private String extractValue(String jsonString, String key) {
            int startIndex = jsonString.indexOf("\"" + key + "\"") + key.length() + 4;
            int endIndex = jsonString.indexOf("\"", startIndex);
            return jsonString.substring(startIndex, endIndex);
        }


        @Override
        protected void id() {
            this.id("id");
        }

        @Override
        protected void id(String name) {
            String columnDefinition = this.columnSchemaBluePrint.idDefinition();
            columnDefinitions.put(name, columnDefinition);
            columnOrder.push(name);
        }


        @Override
        protected ColumnBuilder addForeignId(String columnName) {
            if(columnName.equalsIgnoreCase("id")){
                throw new IllegalArgumentException("Foreign key cannot be id");
            }

            columnDefinitions.put(columnName,DatabaseDataTypes.BIGINT);

            columnDefinitions.put(DatabaseConstants.CONSTRAINT+" fk_"+ columnName, columnName);
            columnOrder.push(DatabaseConstants.CONSTRAINT+" fk_" + columnName);
            return this;
        }
    }



}
