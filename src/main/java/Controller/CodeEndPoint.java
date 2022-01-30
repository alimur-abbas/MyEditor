package Controller;

import Model.FrontEnd;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/output")
public class CodeEndPoint extends FrontEnd {
    //extracting code from frontend
    String code = null;
    String id = null;
    String fileName = null;

    @PostMapping
    public void extract(@RequestBody FrontEnd frontEnd) throws IOException {
        code = frontEnd.getCode();
        id = frontEnd.getId();
        fileName = "/resources/temp/App" + "." + id;
        createAndWrite();
    }

    public void createAndWrite() throws IOException {
        Files.createFile(Paths.get(fileName));
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(code);
    }

    public void execute() throws IOException {
        String byteCode = compileCode(fileName);
    }

    private String compileCode(String fileName) throws IOException {
        String byteCode = null;
        ProcessBuilder processBuilder = new ProcessBuilder("javac", fileName);
        Process process = processBuilder.start();
        try (BufferedReader input =
                     new BufferedReader(new
                             InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = input.readLine()) != null) {
                byteCode = byteCode + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteCode;
    }

}


//    @PostMapping("/code")
//    public byte[] run (@RequestBody FrontEnd frontEnd) throws IOException {
//        Path path = Paths.get("C:\\Users\\Alimur\\Desktop\\SpringBoot\\MyEditor\\src\\main\\resources\\temp\\App.java");
//        Files.createFile(path);
//        FileWriter fileWriter = new FileWriter("App.java", true);
//        fileWriter.write(this.getCode());
//        Process process = Runtime.getRuntime().exec("javac App.java");
//        Path path1 = Paths.get("C:\\Users\\Alimur\\Desktop\\SpringBoot\\MyEditor\\src\\main\\resources\\temp\\App.class");
//        Files.createFile(path1);
//        FileWriter fileWriter1 = new FileWriter("App.class", true);
//        fileWriter1.write(process.getInputStream().read());
//        Process process1 = Runtime.getRuntime().exec("java App.class");
//        byte[] bytes;
//        try (InputStream in = process.getInputStream();) {
//            bytes = new byte[2048];
//            int len;
//            while ((len = in.read(bytes)) != -1) {
//                System.out.write(bytes, 0, len);
//            }
//        }
//        return bytes;
//    }
//}
