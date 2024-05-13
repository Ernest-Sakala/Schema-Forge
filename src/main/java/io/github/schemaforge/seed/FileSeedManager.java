package io.github.schemaforge.seed;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.schemaforge.config.SchemaForgeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class FileSeedManager<T>{

    private static final Logger log = LoggerFactory.getLogger(FileSeedManager.class);

    private final List<FileDataSeeder<T>> seedContainerList = new ArrayList<>();

    private final SeedExecutor seedExecutor;

    public FileSeedManager(SeedExecutor seedExecutor) {
        this.seedExecutor = seedExecutor;
    }


    public void addSeed(List<FileDataSeeder<T>> seedContainerListData) {
        seedContainerList.addAll(seedContainerListData);
    }

    public void runSeeds() {

        for(FileDataSeeder<T> seedContainer : seedContainerList) {

            FileDataContainer<T> jsonDataContainer = seedContainer.seed();

            if(jsonDataContainer != null) {


                if(jsonDataContainer.getFileType().equals(SchemaForgeConstants.JSON_EXTENSION)){

                    Gson gson = new Gson();


                    List<T> data =  gson.fromJson(jsonDataContainer.getFileReader(), new TypeToken<List<T>>(){}.getType());

                    for(T entity : data) {

                        String input = entity.toString();

                        // Remove curly braces from the string
                        input = input.substring(1, input.length() - 1);

                        // Split the string by comma to get key-value pairs
                        String[] keyValuePairs = input.split(", ");

                        // Create a HashMap to store key-value pairs
                        Map<String, String> keyValueMap = new HashMap<>();

                        // Iterate over key-value pairs and store them in the map
                        for (String pair : keyValuePairs) {
                            String[] keyValue = pair.split("=");
                            if (keyValue.length == 2) {
                                String key = keyValue[0].trim();
                                String value = keyValue[1].trim();
                                keyValueMap.put(key, value);
                            } else {
                                // Handle invalid key-value pairs
                                System.err.println("Invalid key-value pair: " + pair);
                            }
                        }

                        String query = constructInsertQuery(jsonDataContainer.getTableName(), keyValueMap);

                        seedExecutor.execute(query);

                        // Print the HashMap

                        log.info("JSON Data Query : {}", query);


                    }


                }else if (jsonDataContainer.getFileType().equals(SchemaForgeConstants.CSV_EXTENSION)){
                    BufferedReader br = new BufferedReader(jsonDataContainer.getFileReader());


                    try {


                        String headerLine = br.readLine();

                        String[] columns = headerLine.split(",");

                        String[] newColumns = new String[columns.length];

                        for (int i = 0; i < columns.length; i++) {
                            newColumns[i] = columns[i].trim().trim();
                        }

                        // Create a HashMap to store key-value pairs
                        Map<String, String> queryData = new HashMap<>();


                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] data = line.split(","); // Assuming CSV is comma-separated



                            for (int i = 0; i < newColumns.length; i++) {


                                String columnName = newColumns[i].trim().toLowerCase();

                                if(columnName.startsWith("\"")){
                                    columnName = columnName.substring(1, columnName.length() - 1);
                                }

                                if(columnName.endsWith("\"")){
                                    columnName = columnName.substring(0, columnName.length() - 2);
                                }

                                String dataValues = data[i].trim();

                                if(dataValues.startsWith("\"")){
                                    dataValues = dataValues.substring(1, dataValues.length() - 1);
                                }

                                if(dataValues.endsWith("\"")){
                                    dataValues = dataValues.substring(0, dataValues.length() - 2);
                                }





                                queryData.put(columnName,dataValues);
                            }

                            String query = constructInsertQuery(jsonDataContainer.getTableName(), queryData);

                            log.info("CSV Query  >>>>>> " + query);

                            seedExecutor.execute(query);

                        }



                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }



            }else {
                log.info("JSON Data Seeder Entity Data is Null");
            }

        }

    }

    private String checkType(T entity, String fieldName){

        Field[] field = entity.getClass().getDeclaredFields();

        for(Field fieldInArray : field){
             if(fieldInArray.getType().equals(String.class)){

                 return "String";

             }else if(fieldInArray.getType().equals(Integer.class)){
                 return "Integer";

             }else if(fieldInArray.getType().equals(Double.class)){

                 return "Double";

             }else if(fieldInArray.getType().equals(Float.class)){

                 return "Float";

             }else if(fieldInArray.getType().equals(Long.class)){

                 return "Long";

             }else if(fieldInArray.getType().equals(Boolean.class)){

                 return "Boolean";

             }else if(fieldInArray.getType().equals(BigDecimal.class)){

                 return "BigDecimal";
             }
        }

        return "";
    }


    private String constructInsertQuery(String tableName, Map<String, String> keyValueMap) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName);
        queryBuilder.append(" (");

        // Append column names
        for (String key : keyValueMap.keySet()) {
            queryBuilder.append(key);
            queryBuilder.append(", ");
        }
        // Remove the trailing comma and space
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(") VALUES (");

        // Append values
        for (String value : keyValueMap.values()) {

            queryBuilder.append("'");
            queryBuilder.append(value);
            queryBuilder.append("', ");
        }
        // Remove the trailing comma and space
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(");");

        return queryBuilder.toString();
    }



    public List<T> readJsonFile(FileReader fileReader, Type typeOfT) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(fileReader, typeOfT);
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String buildInsertQuery(Object object, String tableName) {
        Class<?> currentClass = object.getClass();
        Field[] fields = currentClass.getDeclaredFields();

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName).append(" (");

        // Append column names excluding fields with @Id annotation
        boolean isFirst = true;
        for (Field field : fields) {
            if (!isIdField(field)) {
                if (!isFirst) {
                    queryBuilder.append(", ");
                }
                queryBuilder.append(field.getName());
                isFirst = false;
            }
        }

        queryBuilder.append(") VALUES (");

        // Append values
        isFirst = true;
        for (Field field : fields) {
            if (!isIdField(field)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    if (!isFirst) {
                        queryBuilder.append(", ");
                    }
                    if (value != null) {
                        queryBuilder.append("'").append(value).append("'");
                    } else {
                        queryBuilder.append("null");
                    }
                    isFirst = false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        queryBuilder.append(");");

        return queryBuilder.toString();
    }

    private boolean isIdField(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Id")) {
                return true;
            }
        }
        return false;
    }


}
