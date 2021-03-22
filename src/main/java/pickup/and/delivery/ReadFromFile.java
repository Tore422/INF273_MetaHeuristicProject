package pickup.and.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFile {

    private static final Logger log = LoggerFactory.getLogger(ReadFromFile.class);

    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Could not read the file: {}", filePath);
        }
        return lines;
    }
}
