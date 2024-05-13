package io.github.schemaforge.seed;

import io.github.schemaforge.config.SchemaForgeConstants;
import io.github.schemaforge.exception.UnsupportedFileFormatException;
import org.springframework.stereotype.Component;
import java.io.FileReader;

/**
 * @author Ernest Sakala
 * **/
@Component
public class FileTableDataBuilder<T> {

    public FileDataContainer<T> insertData(String tableName, String filename, T entity) {


            try {


                FileDataContainer<T> dataContainer = new FileDataContainer<>();
                dataContainer.setEntity(entity);
                dataContainer.setTableName(tableName);

                String fileName = SchemaForgeConstants.JSON_SEEDER_FILE_LOCATION.concat(filename.trim());

                if(!filename.endsWith(SchemaForgeConstants.CSV_EXTENSION) && !filename.endsWith(SchemaForgeConstants.JSON_EXTENSION)){
                    throw new UnsupportedFileFormatException("File format not supported Only support JSON and CSV formats" );
                }


                if(filename.endsWith(SchemaForgeConstants.JSON_EXTENSION)) {

                    FileReader fileReader = new FileReader(fileName);
                    dataContainer.setFileReader(fileReader);
                    dataContainer.setFileType(SchemaForgeConstants.JSON_EXTENSION);

                }

                if(filename.endsWith(SchemaForgeConstants.CSV_EXTENSION)){

                    FileReader fileReader = new FileReader(fileName);
                    dataContainer.setFileReader(fileReader);
                    dataContainer.setFileType(SchemaForgeConstants.CSV_EXTENSION);
                }


                return dataContainer;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


    }
}
