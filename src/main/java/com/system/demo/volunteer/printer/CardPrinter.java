package com.system.demo.volunteer.printer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.system.demo.volunteer.Volunteer;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Random;

import com.system.demo.volunteer.VolunteerCategory;
import org.apache.commons.text.RandomStringGenerator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CardPrinter {

    int pageWidth = 580;
    int pageHeight = 850;
    int titlefontSize = 11;
    int namefontSize = 12;
    int smallfontSize = 10;
    int zonefontSize = 15;
    Random rand = new Random();

    String PDF_PW = Integer.toString(rand.nextInt(9999) + 1000);// "1423";

    public Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, smallfontSize, Font.BOLD,
        BaseColor.BLACK);
    public Font namefont = new Font(Font.FontFamily.TIMES_ROMAN, namefontSize, Font.BOLD,
        BaseColor.BLACK);
    public Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, titlefontSize, Font.BOLD,
        BaseColor.BLACK);
    public Font zoneFont = new Font(Font.FontFamily.TIMES_ROMAN, zonefontSize, Font.BOLD,
        BaseColor.BLACK);


    /**
     * @param volunteer volunteer record
     * @return Location of PDF File
     */
    public String print(List<Volunteer> volunteer) throws Exception {
        //Step - 1 :Create Document object that will hold the code
        Document qr_code_Example = new Document(new Rectangle(pageWidth, pageHeight), 10, 10, 10,
            10);

        String rootPath= new File(".").getCanonicalPath();
        String fileName = rootPath + "\\pdf"+ PDF_PW + ".pdf";
        // Step-2: Create PdfWriter object for the document
        PdfWriter writer = PdfWriter
            .getInstance(qr_code_Example, new FileOutputStream(fileName));

      //  String userPassword = "123";
      //  String ownerPassword = "123";

//        writer.setEncryption(userPassword.getBytes(),
//            ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING,
//            PdfWriter.ENCRYPTION_AES_128);

        // Step -3: Open document for editing

        qr_code_Example.open();

        float cardStartX = 20;
        float cardStartY = pageHeight - 20;

        float tmpCardStartY = cardStartY;

        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

        for (int i = 0; i < volunteer.size(); i++) {
            if (i % 6 == 0) {
                tmpCardStartY = cardStartY;
                qr_code_Example.newPage();
            } else if (i % 2 == 0) {
                tmpCardStartY = tmpCardStartY - (pageHeight / 3) + 10;
            }

            String jsonMsg = getJsonForQR(volunteer.get(i));

            String EncryptMsg = ac.encryptText(jsonMsg, privateKey);
            //String DycryptMsg = ac.decryptText(EncryptMsg, publicKey);

            try {
                CreateCard(qr_code_Example,
                    EncryptMsg,
                    writer,
                    cardStartX + ((i % 2 == 0) ? 0 : (pageWidth / 2)),
                    tmpCardStartY,
                    24f,
                    volunteer.get(i));
            } catch (Exception ex) {

            }
        }

        qr_code_Example.close();
        return fileName;
    }

    public void generateKeyFiles() {
        GenerateKeys gk;
        try {
            gk = new GenerateKeys(1024);
            gk.createKeys();
            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static int getSiteForQR(String site) {
        if (site.equalsIgnoreCase("Booni")) {
            return 1;
        } else if (site.equalsIgnoreCase("Garam Chashma")) {
            return 2;
        } else if (site.equalsIgnoreCase("Tause")) {
            return 3;
        } else if (site.equalsIgnoreCase("Alyabad")) {
            return 4;
        } else {
            return -1;
        }
    }

    public static int getCommitteeForQR(String committee) {
        if (committee.equalsIgnoreCase("Security")) {
            return 0;
        } else if (committee.equalsIgnoreCase("Darbar")) {
            return 1;
        } else {
            return -1;
        }
    }

    public static int getZoneForQR(String zone) {
        if (zone.equalsIgnoreCase("MHI entourage")) {
            return 1;
        } else if (zone.equalsIgnoreCase("Pandal")) {
            return 2;
        } else if (zone.equalsIgnoreCase("Main Gate")) {
            return 3;
        } else if (zone.equalsIgnoreCase("Inner Cordon")) {
            return 4;
        } else if (zone.equalsIgnoreCase("Outer Cordon")) {
            return 5;
        } else if (zone.equalsIgnoreCase("All Zone")) {
            return 6;
        } else if (zone.equalsIgnoreCase("Sacrifice Duty")) {
            return 7;
        } else {
            return -1;
        }
    }

    public String getBackgroundImage(String zone) {
        if (zone.equalsIgnoreCase("MHI entourage")) {
            return "cardLayouts/MHI Entourage.jpg";
        } else if (zone.equalsIgnoreCase("Pandal")) {
            return "cardLayouts/Pandol.jpg";
        } else if (zone.equalsIgnoreCase("Main Gate")) {
            return "cardLayouts/Main Gate.jpg";
        } else if (zone.equalsIgnoreCase("Inner Cordon")) {
            return "cardLayouts/inner cordon.jpg";
        } else if (zone.equalsIgnoreCase("Outer Cordon")) {
            return "cardLayouts/Outer cordon.jpg";
        } else if (zone.equalsIgnoreCase("All Zone")) {
            return "cardLayouts/RTF.jpg";
        } else if (zone.equalsIgnoreCase("Sacrifice Duty")) {
            return "cardLayouts/Sacrifice Duty.jpg";
        } else {
            return "";
        }
    }

    public static String getJsonForQR(Volunteer volunteer) {
        String message;

        JSONObject item = new JSONObject();

		/*S = Site
        C = Committee
		Z = Zone
		I = Cnic
		L = Local Council
		N = Name*/

        item.put("S", getSiteForQR(volunteer.getVolunteerSite()));
        /*1 = Booni
		2 = Garam Chashma
		3 = Tause
		4 = Alyabad*/
        item.put("C", getCommitteeForQR(volunteer.getVolunteerCommittee()));
		/*0 = Security
		1 = Darbar*/
        item.put("Z", getZoneForQR(volunteer.getDutyZone()));
		/*1 = MHI entourage
		2 = Pandal
		3 = Main Gate
		4 = Inner Cordon
		5 = Outer Cordon
		6 = NTF/RTF Team
		7 = Sacrifice Duty*/
        item.put("I", volunteer.getVolunteerCnic());
        item.put("L", volunteer.getLocalCouncil());
        item.put("N", volunteer.getVolunteerName());

        message = item.toString();

        return message;
    }

    public void CreateCard(Document qr_code_Example, String EncryptMsg, PdfWriter writer,
        float cardStartX, float cardStartY, float ratio, Volunteer volunteer) throws Exception {
        String imageUrl = getBackgroundImage(volunteer.getDutyZone()); // "D://inner cordon.jpg";
        Image jpg = Image.getInstance(imageUrl);
        jpg.scalePercent(ratio);
        cardStartY = cardStartY - jpg.getScaledHeight();

        jpg.setAbsolutePosition(cardStartX, cardStartY);
        jpg.setBorder(Rectangle.BOX);
        jpg.setBorderColor(BaseColor.BLACK);
        jpg.setBorderWidth(2f);

        qr_code_Example.add(jpg);

        Image qr_image = getQRImage(EncryptMsg, writer);
        qr_image.setAbsolutePosition(
            cardStartX + 10,
            cardStartY + 90);
        qr_image.scalePercent(65);
        qr_code_Example.add(qr_image);

        imageUrl = volunteer.getVolunteerImage();//"D://person.jpg";
        jpg = Image.getInstance(imageUrl);
        jpg.scaleAbsolute(64f, 64f);
        jpg.setAbsolutePosition(cardStartX + 104, cardStartY + 100);
        qr_code_Example.add(jpg);

        PdfContentByte cb = writer.getDirectContent();
        ColumnText ct = new ColumnText(cb);
        Phrase myText = new Phrase( (volunteer.getVolunteerName()!=null)?volunteer.getVolunteerName().toUpperCase() : "" , namefont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 66,
            cardStartX + (pageWidth / 2), cardStartY + 82, 6, Element.ALIGN_CENTER);
        ct.go();

        myText = new Phrase( (volunteer.getVolunteerCommittee()!=null)?volunteer.getVolunteerCommittee() : "" , smallfont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 56,
            cardStartX + (pageWidth / 2), cardStartY + 66, 6, Element.ALIGN_CENTER);
        ct.go();

        myText = new Phrase( (volunteer.getVolunteerCnic()!=null)?volunteer.getVolunteerCnic(): "" , smallfont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 43,
            cardStartX + (pageWidth / 2), cardStartY + 53, 6, Element.ALIGN_CENTER);
        ct.go();

        //local council
        myText = new Phrase( (volunteer.getLocalCouncil()!=null)?volunteer.getLocalCouncil(): "" , smallfont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 30,
            cardStartX + (pageWidth / 2), cardStartY + 40, 6, Element.ALIGN_CENTER);
        ct.go();

        //Zone
        myText = new Phrase( (volunteer.getDutyZone()!=null)?volunteer.getDutyZone().toUpperCase(): "" , zoneFont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY - 10,
            cardStartX + (pageWidth / 2), cardStartY + 22, 6, Element.ALIGN_CENTER);
        ct.go();

        /*//Header title
        myText = new Phrase( (volunteer.getVolunteerSite()!=null)?volunteer.getVolunteerSite().toUpperCase(): "" , titleFont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 162, cardStartX + (pageWidth / 2),
            cardStartY + 175, 6, Element.ALIGN_CENTER);
        ct.go();*/
    }

    public Image getQRImage(String EncryptMsg, PdfWriter writer) throws Exception {
        BarcodeQRCode my_code = new BarcodeQRCode(EncryptMsg, 150, 150, null);
        //Step-6: Get Image corresponding to the input string
        Image qr_image = my_code.getImage();

        qr_image = cropImage(writer, qr_image, 10, 10, 10, 10);

        return qr_image;
    }

    public Image cropImage(PdfWriter writer, Image image, float leftReduction, float rightReduction,
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
