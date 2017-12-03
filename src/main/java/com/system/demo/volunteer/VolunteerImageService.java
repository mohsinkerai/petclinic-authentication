package com.system.demo.volunteer;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Component
public class VolunteerImageService {

    private String path;

    public VolunteerImageService() throws IOException {
        path = new File(".").getCanonicalPath() + File.separator + "datahouse" + File.separator + "uploadedImages";
    }

    //        byte[] imageBytes = Files.toByteArray(new File(filePath));
//        return "data:image/png;base64, " + Base64.encodeBase64URLSafeString(imageBytes);
    public String read(String filePath) throws IOException {
        if (filePath != null) {
            BufferedImage image = ImageIO.read(new File(filePath));
            String imageString = "helloWorld";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                if (image != null) {
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
        } else {
            return "";
        }
    }

    public String write(String image, String cnic) throws IOException {
        if (image != null && image != "") {
            image = image.substring(image.indexOf("base64") + 7).trim();
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] imageByte = decoder.decodeBuffer(image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//        bis.mark(256);
            BufferedImage bi = ImageIO.read(bis);
            bis.close();

            File outputFile = new File(path + File.separator + cnic + ".png");
            if(!outputFile.exists()) {
                FileUtils.touch(outputFile);
            }
            ImageIO.write(bi, "png", outputFile);

//        byte[] data = Base64.decodeBase64(image);
//        try (OutputStream stream = new FileOutputStream(path + "\\" + cnic + ".jpg")) {
//            stream.write(data);
//        }
            return path + File.separator + cnic + ".png";
        } else {
            return null;
        }
    }
}
