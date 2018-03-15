import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class ParseXML
{
     static private String endpoint = "http://81.214.73.178/TahsilatService/TahsilatService.asmx";
     static private String namespace = "http://tempuri.org/";
     static private String parameter = "referansNo";

     private String refValue; // Kişilere özel referans numarası
      private String operation; // İşlem tipi, BorcSorgu ve ya TahsilatSorgu
     private String name; // Kişinin ismi
     private String[] columns; // Sorguya göre tablo sütunlarının ismi
     private Object[][] tableData; // Sorgu sonuçları
     private Document xmlDocument; // Sorguya göre web servisinden dönen xml

     // Sorgu için kullanılacak referans ve sorgu işlemini atayan constructor
     public ParseXML(String refValue, String operation)
     {
          this.refValue = refValue;
          this.operation = operation;
     }

     // Web servisinden dönen cevabı parse eden metod
     public void parse()
     {

          try
          {
              // Kişiye özel referans no ve işlem türü ile oluşturulan nesne
              SOAPClient client =
                 new SOAPClient(endpoint, namespace, operation, parameter, refValue);

              // Web servisinden dönen cevabı edinme ve parse işlemleri
              DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                  .newDocumentBuilder();
              xmlDocument = builder.parse(client.callSoapWebService());
              xmlDocument.getDocumentElement().normalize();

              // Xml dökümanının içeriğine göre parse işlemi yapılır
              if(operation == "BorcSorgu")
                 borcSorgu();
              else
                 tahsilatSorgu();
          }
          catch(Exception ex)
          {
              ex.printStackTrace();
          }

     }

     public void borcSorgu()
     {
           // xml de bulunan kişiye ait isim bilgisinin çekilmesi
           name = ((Element)xmlDocument.getElementsByTagName("AdSoyad").item(0))
                .getTextContent();

           // xml de bulunan kişiye ait borç bilgilerinin çekilmesi
           NodeList nList = xmlDocument.getElementsByTagName("BorcDetay");

           // Verilerin tutulacağı dizi ve tablo sütun değerlerinin oluşturulması
           tableData = new Object[nList.getLength()][8];
           String[] columns = {"Borc Referans No", "Gelir ID", "Borc Turu",
              "Donem Taksit","Son Odeme Tarihi", "Tutar", "Gecikme", "Toplam"};
           this.columns = columns;

           try
           {
               /* Xml den çekilen BorcDetay etiketleri üzerinde iterasyon
                  yapılarak ilgili sütunlar elde edilir */
               for(int current = 0; current < nList.getLength(); current++)
               {
                    Node nNode = nList.item(current);
                    Element element = (Element) nNode;

                    tableData[current][0] =
                       element.getElementsByTagName("BorcReferansNo")
                       .item(0).getTextContent();

                    tableData[current][1] =
                       element.getElementsByTagName("GelirID")
                       .item(0).getTextContent();

                    tableData[current][2] =
                       element.getElementsByTagName("BorcTur")
                       .item(0).getTextContent();

                    tableData[current][3] =
                       element.getElementsByTagName("DonemTaksit")
                       .item(0).getTextContent();

                    tableData[current][4] =
                       element.getElementsByTagName("SonOdemeTarih")
                      .item(0).getTextContent();

                    tableData[current][5] =
                       element.getElementsByTagName("Tutar")
                       .item(0).getTextContent();

                    tableData[current][6] =
                       element.getElementsByTagName("Gecikme")
                       .item(0).getTextContent();

                    tableData[current][7] =
                       element.getElementsByTagName("Toplam")
                       .item(0).getTextContent();
               }
           }
           catch(Exception e)
           {
               e.printStackTrace();
           }
     }

     public void tahsilatSorgu()
     {
          // xml de bulunan kişiye ait isim bilgisinin çekilmesi
          name = ((Element)xmlDocument.getElementsByTagName("AdSoyad").item(0))
             .getTextContent();

          // xml de bulunan kişiye ait tahsilat bilgilerinin çekilmesi
          NodeList nList = xmlDocument.getElementsByTagName("TahsilatDetay");

          // Verilerin tutulacağı dizi ve tablo sütun değerlerinin oluşturulması
          tableData = new Object[nList.getLength()][5];
          String[] columns = {"TahsilatReferansNo", "BorcTur", "IslemTarih",
             "BankaReferansNo", "Tutar"};
          this.columns = columns;

          try
          {
              /* Xml den çekilen TahsilatDetay etiketleri üzerinde iterasyon
                 yapılarak ilgili sütunlar elde edilir */
              for(int current = 0; current < nList.getLength(); current++)
              {
                   Node nNode = nList.item(current);
                   Element element = (Element)nNode;

                   tableData[current][0] =
                      element.getElementsByTagName("TahsilatReferansNo")
                      .item(0).getTextContent();

                   tableData[current][1] =
                      element.getElementsByTagName("BorcTur")
                      .item(0).getTextContent();

                   tableData[current][2] =
                      element.getElementsByTagName("IslemTarih")
                      .item(0).getTextContent();

                   tableData[current][3] =
                      element.getElementsByTagName("BankaReferansNo")
                      .item(0).getTextContent();

                   tableData[current][4] =
                      element.getElementsByTagName("Tutar")
                      .item(0).getTextContent();
              }
        }
        catch(Exception e)
        {
             e.printStackTrace();
        }
     }

     // Verileri döndüren method
     public Object[][] getTableData()
     {
          return tableData;
     }

     // Sütun isimlerini döndüren method
     public String[] getColumns()
     {
          return columns;
     }

     // Kişiye ait isim bilgisini döndüren method
     public String getName()
     {
          return name;
     }

}
