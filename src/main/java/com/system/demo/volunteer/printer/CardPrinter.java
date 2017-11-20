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
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import org.json.JSONObject;

public class CardPrinter {

    int pageWidth = 530;
    int pageHeight = 800;

    int SecurityPassfontSize = 17;
    int titlefontSize = 11;
    int smallfontSize = 9;

    String PDF_PW = "123";

    public Font smallfont = new Font(Font.FontFamily.TIMES_ROMAN, smallfontSize, -1,
        BaseColor.BLACK);
    public Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, titlefontSize, -1,
        BaseColor.BLACK);

    /**
     * @param volunteer volunteer record
     * @return Location of PDF File
     */
    public String print(List<Volunteer> volunteer) throws Exception {
        //Step - 1 :Create Document object that will hold the code
        Document qr_code_Example = new Document(new Rectangle(pageWidth, pageHeight), 10, 10, 10,
            10);
        // Step-2: Create PdfWriter object for the document
        PdfWriter writer = PdfWriter
            .getInstance(qr_code_Example, new FileOutputStream("D:/QR_PDF_Output.pdf"));

        String userPassword = PDF_PW;
        String ownerPassword = PDF_PW;

        writer.setEncryption(userPassword.getBytes(),
            ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING,
            PdfWriter.ENCRYPTION_AES_128);

        // Step -3: Open document for editing
        qr_code_Example.open();

        //generateKeyFiles(); //--> For generating Keys
        String jsonMsg = getJsonForQR("string is passed here");

        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

        String EncryptMsg = ac.encryptText(jsonMsg, privateKey);

        String DycryptMsg = ac.decryptText(EncryptMsg, publicKey);

        //Step-7: Stamp the QR image into the PDF document
        float cardStartX = 34;
        float cardStartY = pageHeight - 20;

        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX, cardStartY, 46.5f);

        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX + (pageWidth / 2), cardStartY,
            46.5f);

        cardStartY = cardStartY - (pageHeight / 3);
        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX, cardStartY, 46.5f);

        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX + (pageWidth / 2), cardStartY,
            46.5f);

        cardStartY = cardStartY - (pageHeight / 3);
        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX, cardStartY, 46.5f);

        CreateCard(qr_code_Example, EncryptMsg, writer, cardStartX + (pageWidth / 2), cardStartY,
            46.5f);

        return "Path De Dena Koi Bhi";
    }

    public static String getJsonForQR(String str) {
        String message;

        JSONObject item = new JSONObject();

		/*S = Site
        C = Committee
		Z = Zone
		I = Cnic
		L = Local Council
		N = Name*/

        item.put("S", 2);
		/*1 = Booni
		2 = Garam Chashma
		3 = Tause
		4 = Alyabad*/
        item.put("C", 0);
		/*0 = Security
		1 = Darbar*/
        item.put("Z", 1);
		/*1 = MHI entourage
		2 = Pandal
		3 = Main Gate
		4 = Inner Cordon
		5 = Outer Cordon
		6 = NTF/RTF Team*/
        item.put("I", "42000-7528952-9");
        item.put("L", "Karimabad");
        item.put("N", "Alishah Sayani");

        message = item.toString();

        return message;
    }

    public void CreateCard(Document qr_code_Example, String EncryptMsg, PdfWriter writer,
        float cardStartX, float cardStartY, float ratio) throws Exception {
        String imageUrl = "D://inner cordon.jpg";
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
            cardStartX + 20,
            cardStartY + 20);
        qr_image.scalePercent(40);
        qr_code_Example.add(qr_image);

        imageUrl = "D://person.jpg";
        jpg = Image.getInstance(imageUrl);
        jpg.scaleAbsolute(64f, 68f);
        jpg.setAbsolutePosition(cardStartX + 64, cardStartY + 90);
        qr_code_Example.add(jpg);

        PdfContentByte cb = writer.getDirectContent();
        ColumnText ct = new ColumnText(cb);
        Phrase myText = new Phrase("Alishah is here", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 88, cardStartY + 63,
            cardStartX + (pageWidth / 2) - 10, cardStartY + 73, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase("Second line to be here", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 88, cardStartY + 52.5f,
            cardStartX + (pageWidth / 2) - 10, cardStartY + 62, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase("Third line to be here", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 88, cardStartY + 42,
            cardStartX + (pageWidth / 2) - 10, cardStartY + 52, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase("Forth line to be here", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 88, cardStartY + 31.5f,
            cardStartX + (pageWidth / 2) - 10, cardStartY + 41, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase("Fifth line to be here", smallfont);
        ct.setSimpleColumn(myText, cardStartX + 88, cardStartY + 21,
            cardStartX + (pageWidth / 2) - 10, cardStartY + 31, 6, Element.ALIGN_LEFT);
        ct.go();

        myText = new Phrase("HUNZA", titleFont);
        ct.setSimpleColumn(myText, cardStartX - 73, cardStartY + 160, cardStartX + (pageWidth / 2),
            cardStartY + 170, 6, Element.ALIGN_CENTER);
        ct.go();
    }

    public Image getQRImage(String EncryptMsg, PdfWriter writer) throws Exception {
        BarcodeQRCode my_code = new BarcodeQRCode(EncryptMsg, 150, 150, null);
        //Step-6: Get Image corresponding to the input string
        Image qr_image = my_code.getImage();
        //Image mask = my_code.getImage();
        //mask.makeMask();
        //qr_image.setImageMask(mask);

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
