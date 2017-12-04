package com.system.demo.volunteer.printer;

import com.google.common.collect.Lists;
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
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class CardPrinter {

    int pageWidth = 580;
    int pageHeight = 850;
    int titlefontSize = 15;
    int namefontSize = 12;
    int smallfontSize = 11;
    int zonefontSize = 15;
    Random rand = new Random();

    String PDF_PW = Integer.toString(rand.nextInt(9999) + 1000);// "1423";

    public Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, smallfontSize, Font.BOLD,
        new BaseColor(86, 86, 86));
    public Font namefont = new Font(Font.FontFamily.TIMES_ROMAN, namefontSize, Font.BOLD,
        BaseColor.BLACK);
    public Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, titlefontSize, Font.BOLD,
        new BaseColor(88, 88, 88));
    public Font zoneFont = new Font(Font.FontFamily.TIMES_ROMAN, zonefontSize, Font.BOLD,
        BaseColor.BLACK);


    /**
     * @param volunteer volunteer record
     * @return Location of PDF File
     */
    public PrintingResult print(List<Volunteer> volunteer) throws Exception {
        List<Volunteer> printedVolunteers = Lists.newArrayList();

        //Step - 1 :Create Document object that will hold the code
        Document qr_code_Example = new Document(new Rectangle(pageWidth, pageHeight), 10, 10, 10,
            10);

        String rootPath = new File(".").getCanonicalPath();
        String fileName = rootPath + File.separator +"pdf" + PDF_PW + ".pdf";
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

      try {
        String jsonMsg = getJsonForQR(volunteer.get(i));

        String EncryptMsg = ac.encryptText(jsonMsg, privateKey);
        //String DycryptMsg = ac.decryptText(EncryptMsg, publicKey);

        CreateCard(
            qr_code_Example,
            EncryptMsg,
            writer,
            cardStartX + ((i % 2 == 0) ? 0 : (pageWidth / 2)),
            tmpCardStartY,
            24f,
            volunteer.get(i));
        printedVolunteers.add(volunteer.get(i));
      } catch (IllegalBlockSizeException exception) {
        log.info("QR Code Failed for record {}", volunteer);
        exception.printStackTrace();
        throw exception;
      } catch (Exception ex) {
        ex.printStackTrace();
        log.error("An Error has been occoured while printing card");
      }
    }

    try {
      qr_code_Example.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(
          "Card not printed, requested card to print were "
              + volunteer
              + ", Individual card exception is available in logs",
          ex);
    }

    return PrintingResult.builder().fileName(fileName).printedVolunteers(printedVolunteers).build();
    //        return fileName;
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
        } else if (site.equalsIgnoreCase("Central")) {
            return 5;
        } else if (site.equalsIgnoreCase("Southern")) {
            return 6;
        } else if (site.equalsIgnoreCase("All Pakistan")) {
            return 7;
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
		4 = Alyabad
		5 = Central
		6 = Southern
		*/
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
        item.put("I", getTrimedData(volunteer.getVolunteerCnic()));
        item.put("L", getTrimedData(volunteer.getLocalCouncil()));
        item.put("N", getTrimedData(volunteer.getVolunteerName()));

        if (volunteer.getDutyShift() != null) {
            item.put("H", volunteer.getDutyShift());
        }

        if (volunteer.getDutyDay() != null) {
            item.put("U", volunteer.getDutyDay());
        }

        message = item.toString();
        return message;
    }

    private static String getTrimedData(String data) {
        if (data.length() > 29) {
            return data.substring(0, 29);
        } else {
            return data;
        }
    }

    public String truncateText(String inputString, int maxLength) {
        if (inputString.length() > maxLength) {
            inputString = inputString.substring(0, maxLength);
        }
        return inputString;
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
            cardStartX + 122.2f,
            cardStartY + 32.6f);
        qr_image.scalePercent(73.1f);
        qr_code_Example.add(qr_image);

        imageUrl = volunteer.getVolunteerImage();//"D://person.jpg";
        jpg = Image.getInstance(imageUrl);
        jpg.scaleAbsolute(92.8f, 98.4f);
        jpg.setAbsolutePosition(cardStartX + 28.83f, cardStartY + 115.5f);
        qr_code_Example.add(jpg);

        PdfContentByte cb = writer.getDirectContent();
        ColumnText ct = new ColumnText(cb);

        String VolFirstName = "";
        String VolSecondName = "";
        if (volunteer.getVolunteerName() != null) {
            String VolName[] = volunteer.getVolunteerName().toUpperCase().split(" ");
            VolFirstName = VolName[0];
            if (VolName.length > 1) {
                VolSecondName = VolName[1];
            }
        }
        Phrase myText = new Phrase(truncateText(VolFirstName, 10), namefont);
        ct.setSimpleColumn(myText, cardStartX + 29, cardStartY + 92,
            cardStartX + (pageWidth / 2), cardStartY + 102, 6,
            Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase(truncateText(VolSecondName, 10), namefont);
        ct.setSimpleColumn(myText, cardStartX + 29, cardStartY + 78.5f,
            cardStartX + (pageWidth / 2), cardStartY + 88.5f, 6, Element.ALIGN_LEFT);
        ct.go();

//        if((volunteer.getVolunteerSite()!=null) && ((volunteer.getVolunteerSite().equalsIgnoreCase("Central"))) || (volunteer.getVolunteerSite().equalsIgnoreCase("Southern")))
//            myText = new Phrase( (volunteer.getDutyDay()!=null && volunteer.getDutyShift()!=null)? "Day-"+volunteer.getDutyDay()+" Shift-"+volunteer.getDutyShift() : "" , smallfont);
//        else
        myText = new Phrase((volunteer.getVolunteerCommittee() != null) ? truncateText(
            volunteer.getVolunteerCommittee().toUpperCase(), 12) : "", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 29, cardStartY + 62,
            cardStartX + (pageWidth / 2), cardStartY + 72, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase(
            (volunteer.getVolunteerCnic() != null) ? truncateText(volunteer.getVolunteerCnic(), 16)
                : "", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 29, cardStartY + 46.5f,
            cardStartX + (pageWidth / 2), cardStartY + 56.5f, 6, Element.ALIGN_LEFT);
        ct.go();

    //local council
    myText =
        new Phrase(
            (volunteer.getLocalCouncil() != null)
                ? truncateText(volunteer.getLocalCouncil().toUpperCase(), 12)
                : "",
            smallfont);
    ct.setSimpleColumn(
        myText,
        cardStartX + 29,
        cardStartY + 33,
        cardStartX + (pageWidth / 2),
        cardStartY + 43,
        6,
        Element.ALIGN_LEFT);
    ct.go();

       /* //Zone
        myText = new Phrase( (volunteer.getDutyZone()!=null)?volunteer.getDutyZone().toUpperCase(): "" , zoneFont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY - 10,
            cardStartX + (pageWidth / 2), cardStartY + 22, 6, Element.ALIGN_CENTER);
        ct.go();*/

        //Header title
        myText = new Phrase((volunteer.getVolunteerSite() != null) ? truncateText(
            volunteer.getVolunteerSite().toUpperCase(), 14) : "", titleFont);
        ct.setSimpleColumn(myText, cardStartX + 29, cardStartY + 225, cardStartX + (pageWidth / 2),
            cardStartY + 235, 6, Element.ALIGN_LEFT
        );
        ct.go();

        if ((volunteer.getVolunteerSite() != null) && ((volunteer.getVolunteerSite()
            .equalsIgnoreCase("Central"))) || (volunteer.getVolunteerSite()
            .equalsIgnoreCase("Southern"))) {
            myText = new Phrase(
                (volunteer.getDutyDay() != null) ? "Day-"
                    + volunteer.getDutyDay() : "",
                smallfont);
            ct.setSimpleColumn(myText, cardStartX + 145, cardStartY + 120,
                cardStartX + (pageWidth / 2),
                cardStartY + 130, 6, Element.ALIGN_LEFT
            );
            ct.go();

            myText = new Phrase(
                (volunteer.getDutyShift() != null) ? "Shift-" + volunteer.getDutyShift() : "",
                smallfont);
            ct.setSimpleColumn(myText, cardStartX + 142, cardStartY + 134,
                cardStartX + (pageWidth / 2),
                cardStartY + 144, 6, Element.ALIGN_LEFT
            );
            ct.go();
        }
    }

    public Image getQRImage(String EncryptMsg, PdfWriter writer) throws Exception {
        BarcodeQRCode my_code = new BarcodeQRCode(EncryptMsg, 150, 150, null);
        //Step-6: Get Image corresponding to the input string
        Image qr_image = my_code.getImage();

        qr_image = cropImage(writer, qr_image, 26, 26, 26, 26);

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
