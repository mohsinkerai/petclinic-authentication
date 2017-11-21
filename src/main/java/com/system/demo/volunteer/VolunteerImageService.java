package com.system.demo.volunteer;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Component
public class VolunteerImageService {

    private String path;

    public VolunteerImageService() throws IOException {
        path = new File(".").getCanonicalPath() + "\\datahouse" + "\\uploadedImages";
    }

    //        byte[] imageBytes = Files.toByteArray(new File(filePath));
//        return "data:image/png;base64, " + Base64.encodeBase64URLSafeString(imageBytes);
    public String read(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        String imageString = "helloWorld";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            if(image != null) {
                ImageIO.write(image, String.valueOf(image.getType()), bos);
                byte[] imageBytes = bos.toByteArray();

                BASE64Encoder encoder = new BASE64Encoder();
                imageString = encoder.encode(imageBytes);

                bos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public String write(String image, String cnic) throws IOException {
        image = image.substring(image.indexOf("base64")+7).trim();
        System.out.println(image.substring(image.indexOf("base64")+7).trim());
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = decoder.decodeBuffer(image);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//        bis.mark(256);
        BufferedImage bi = ImageIO.read(bis);
        bis.close();

        File outputFile = new File(path + "\\" + cnic + ".png");
        ImageIO.write(bi, "png", outputFile);
//
//        byte[] data = Base64.decodeBase64(image);
//        try (OutputStream stream = new FileOutputStream(path + "\\" + cnic + ".jpg")) {
//            stream.write(data);
//        }
        return path + "\\" + cnic + ".png";
    }
}
