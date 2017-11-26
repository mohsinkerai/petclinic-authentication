package com.darbar;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.simple.JSONObject;

import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import java.util.Scanner;

import static javafx.application.Platform.exit;

public class Main {


    public static void main(String[] args) {
	// write your code here
        try {
        int pageWidth = 150;
        int pageHeight = 150;


        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Name:");
        String name = scanner.nextLine();

        System.out.print("Enter CNIC:");
        String cnic = scanner.nextLine();

        System.out.print("Enter Local Council:");
        String localCouncil = scanner.nextLine();

        System.out.println("Enter Site symbol as per list below):");
        System.out.println("1 = Booni");
        System.out.println("2 = Garam Chashma");
        System.out.println("3 = Tause");
        System.out.println("4 = Alyabad");
        /*1 = Booni
		2 = Garam Chashma
		3 = Tause
		4 = Alyabad*/
        String site = scanner.nextLine();


        System.out.println("Enter Committee symbol as per list below):");
        System.out.println("0 = Security");
        System.out.println("1 = Darbar");
        /*0 = Security
		1 = Darbar*/
        String committee = scanner.nextLine();


        System.out.println("Enter Zone symbol as per list below):");
        System.out.println("1 = MHI entourage");
        System.out.println("2 = Pandal");
        System.out.println("3 = Main Gate");
        System.out.println("4 = Inner Cordon");
        System.out.println("5 = Outer Cordon");
        System.out.println("6 = NTF/RTF Team");
        System.out.println("7 = Security Team");
        /*1 = MHI entourage
		2 = Pandal
		3 = Main Gate
		4 = Inner Cordon
		5 = Outer Cordon
		6 = NTF/RTF Team*/
        String zone = scanner.nextLine();

        Document qr_code_Example = new Document(new Rectangle(pageWidth, pageHeight), 0, 0, 0,
                0);

        String fileName = "123.pdf";
        // Step-2: Create PdfWriter object for the document
        PdfWriter writer = PdfWriter
                .getInstance(qr_code_Example, new FileOutputStream(fileName));



            AsymmetricCryptography ac = new AsymmetricCryptography();
            PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
            PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

            //System.out.println(name+" "+cnic+" "+localCouncil+" "+site+" "+committee);
            String jsonMsg = getJsonForQR(name, cnic, localCouncil, site, committee, zone);

            String EncryptMsg = ac.encryptText(jsonMsg, privateKey);
            //String DycryptMsg = ac.decryptText(EncryptMsg, publicKey);

            qr_code_Example.open();

            Image qr_image = getQRImage(EncryptMsg, writer);
            qr_image.scalePercent(73.1f);
            qr_code_Example.add(qr_image);
            qr_code_Example.close();


        } catch (Exception e) {
            e.printStackTrace();
            {System.out.println("****************ERROR************");exit();}
        }


    }



    public static String getJsonForQR(String name, String nic, String localcouncil, String site, String committee, String zone) throws Exception {
        String message;

        JSONObject item = new JSONObject();

		/*S = Site
        C = Committee
		Z = Zone
		I = Cnic
		L = Local Council
		N = Name*/

        if(Integer.parseInt(site) > 0)
            item.put("S", Integer.parseInt(site));
        else
            throw new Exception();

		/*1 = Booni
		2 = Garam Chashma
		3 = Tause
		4 = Alyabad*/

        item.put("C", Integer.parseInt(committee));
        /*0 = Security
		1 = Darbar*/

		if(Integer.parseInt(zone) > 0)
            item.put("Z", Integer.parseInt(zone));
		else
            throw new Exception();
		/*1 = MHI entourage
		2 = Pandal
		3 = Main Gate
		4 = Inner Cordon
		5 = Outer Cordon
		6 = NTF/RTF Team*/
        item.put("I", nic);
        item.put("L", localcouncil);
        item.put("N", name);

        message = item.toString();

        return message;
    }

    public static Image getQRImage(String EncryptMsg, PdfWriter writer) throws Exception {
        BarcodeQRCode my_code = new BarcodeQRCode(EncryptMsg, 250, 250, null);
        //Step-6: Get Image corresponding to the input string
        Image qr_image = my_code.getImage();

        qr_image = cropImage(writer, qr_image, 22, 22, 22, 22);

        return qr_image;
    }

    public static Image cropImage(PdfWriter writer, Image image, float leftReduction, float rightReduction,
                           float topReduction, float bottomReduction) throws DocumentException {
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        PdfTemplate template = writer.getDirectContent().createTemplate(
                width - leftReduction - rightReduction,
                height - topReduction - bottomReduction);
        template.addImage(image,
                width, 0, 0,
                height, -leftReduction, -bottomReduction);
        return Image.getInstance(template);
    }


}
