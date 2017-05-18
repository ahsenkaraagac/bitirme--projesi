package jsouptests;

import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VeriCek 
{
	public static void uludag(String firmaAdi)
	{
		// Boşluk için - kullan
		System.out.println("Uludağ Sözlük "+firmaAdi+" !");
		
		int i = 1;
		String toplananlar = "";
		int mesajSayaci = 0;
		try
		{		
			Elements elementsSayfa,veriler;
			Document doc;
			
				do 
				{
					doc = Jsoup.connect("http://www.uludagsozluk.com/k/"+firmaAdi+"/"+i).timeout(0).userAgent("Chrome").get();
				    elementsSayfa = doc.select("#main > div.entry-block.clearfix > div.pagination > ul > li");
				    veriler = doc.getElementsByClass("entry-p");

					for(Element element : veriler)
					{
						toplananlar = toplananlar + element.text()+"\n";
						mesajSayaci++;
						System.out.println(element.text());
					}
					
					i++;		
				}while(elementsSayfa.last().text().equals("»"));
				
				//Yorumlar temizleniyor
				toplananlar = toplananlar.replaceAll("\'", " ");
				toplananlar = toplananlar.replaceAll("\"", " ");
				
				System.out.println("BİTTİ");
				dosyayaYaz(toplananlar, firmaAdi.toUpperCase(), "ULUDAĞ SÖZLÜK", mesajSayaci);
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}

		
	}

	public static void donanimHaber(String id,String firmaAdi)
	{
		// https://forum.donanimhaber.com/m_118127418/mpage_499/tm.htm
		System.out.println("DonanımHaber !");
		int i = 1;
		String toplananlar = "";
		int mesajSayaci = 0;
		try
		{
			Elements elements;
			do
			{
				Document doc = Jsoup.connect("https://forum.donanimhaber.com/"+id+"/mpage_"+i+"/tm.htm").timeout(10000).userAgent("Chrome").get();
			    elements = doc.getElementsByClass("msg");
			    if(elements.size()<=0)
			    	break;
			    
					for(Element element : elements)
					{
						toplananlar = toplananlar + element.text()+"\n";
						mesajSayaci++;
						System.out.println(element.text());
					}
				//Diğer sayfa için i arttırıldı.
				i++;
				
			}while(!elements.equals(null));
			
			//Yorumlar temizleniyor
			toplananlar = toplananlar.replaceAll("\'", " ");
			toplananlar = toplananlar.replaceAll("\"", " ");
			
			System.out.println("Başarıyla sonlandı!");
			dosyayaYaz(toplananlar,firmaAdi.toUpperCase(),"DONANIMHABER",mesajSayaci);

		}
		catch(Exception ex)
		{
			System.out.println(ex.toString()+"\n"+"Hata ile sonuçlandı ! ");
			dosyayaYaz(toplananlar,firmaAdi.toUpperCase(),"DONANIMHABER",mesajSayaci);
		}
		
	}
	
	public static void eksiSozluk(String firmaAdi,String firmaLink)
	{
		
		//firmaLink ='e kadar, = dahil.
		//firmaLink = "https://eksisozluk.com/imkan-olsa-yazarlarin-oynamak-isteyecegi-dizi--5240356?a=popular&p=" gibi
		System.out.println("Ekşisözlük !");
		int i = 1;
		String toplananlar = "";
		int mesajSayaci = 0;
		try
		{
			Elements elements;
			do
			{
				Document doc = Jsoup.connect(firmaLink+i).timeout(0).userAgent("Chrome").get();
			    elements = doc.getElementsByClass("content");
				if(elements != null)
					for(Element element : elements)
					{
						toplananlar = toplananlar + element.text()+"\n";
						mesajSayaci++;
						System.out.println(element.text());
					}
				//Diğer sayfa için i arttırıldı.
				i++;
				
			}while(elements != null);
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			//Yorumlar temizleniyor
			toplananlar = toplananlar.replaceAll("\'", " ");
			toplananlar = toplananlar.replaceAll("\"", " ");
			dosyayaYaz(toplananlar,firmaAdi.toUpperCase(),"EKŞİ SÖZLÜK",mesajSayaci);
		}
	}
	
	public static void sikayetvar(String firmaAdi)
	{
		// firma adında bosluk varsa yerine - kullanılmalı örnek: vatan bilgisayar için vatan-bilgisayar.
		
		System.out.println("Şikayetvar "+firmaAdi);
		
		//Sikayet varda sayfalar 0'dan basliyor. Bu yüzden i = 0 değeriyle baslamali.
		int i = 0;
		String toplananlar = "",detaySayfasiLinki="https://www.sikayetvar.com",temp,etiket="neg";
		int mesajSayaci = 0;
		int j = 0; // 
		try
		{		
			Elements elementsBasliklar,elementsOzet,elementsDetayLinki,elementDetay;
			Document doc,docDetay;
			
				do 
				{
					doc = Jsoup.connect("https://www.sikayetvar.com/"+firmaAdi+"?page="+i).timeout(0).userAgent("Chrome").get();
				    elementsBasliklar = doc.select("#sikayetListesi > li > div > div > h3 > a");
				    elementsOzet = doc.select("#sikayetListesi > li > p");
				    
					for(Element element : elementsBasliklar)
					{
						//Sorun cozulmusse href attribute'u yoktur, donguyu burada bitirir, diger basliga bakar
						if(element.attr("href").charAt(0)=='j')
							continue;
					
						mesajSayaci++;
						System.out.println(String.valueOf(mesajSayaci)+". "+element.text()+"\n");
						
						//Detay sayfasi aciliyor
						docDetay = Jsoup.connect(detaySayfasiLinki+element.attr("href")).timeout(0).userAgent("Chrome").get();
						elementDetay = docDetay.select("body > div.mainContainer > div.container.clearfix > div > div.leftCol > div:nth-child(1) > article > section:nth-child(2) > p");
						
						for(Element elementOFDetay : elementDetay)
						{
							temp = element.text();
							
							//Tırnaklar temizleniyor.
							temp = temp.replaceAll("\'", " ");
							temp = temp.replaceAll("\"", " ");
							
							temp = "'"+ element.text() + "',"+etiket+"\n";
							
							System.out.println("Detay: "+elementOFDetay.text()+temp);
							toplananlar = toplananlar + elementOFDetay.text()+temp;
						}
						
						j++;
					}// Her başlık için 1 kez for çalışır.
					
					j=0;
					i++;		
				}while(elementsBasliklar.size()>0);
				
				System.out.println("BİTTİ");
				dosyayaYaz(toplananlar, firmaAdi.toUpperCase(), "Şikayetvar ", mesajSayaci);
			
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			dosyayaYaz(toplananlar, firmaAdi.toUpperCase(), "Şikayetvar ", mesajSayaci);

		}
	}
	
	public static void hepsiburada(String link,String etiket,String yildiz,int kacSayfa)
	{
		// Gerekli link
		// http://www.hepsiburada.com/samsung-galaxy-ace-s5830i-2-gb-hafiza-karti-hediye-p-TELCEPSAMS5830-B-yorumlari?filtre=
		System.out.println("Hepsiburada.com !");
		int i = 1;
		String toplananlar = "",temp;
		int mesajSayaci = 0;
		try
		{
			Elements elements;
			do
			{
				if(i == kacSayfa+1)
					break;
				
				Document doc = Jsoup.connect(link+yildiz+"&sayfa="+i).timeout(0).userAgent("Chrome").get();
			    elements = doc.getElementsByClass("review-text");
				if(elements != null)
					for(Element element : elements)
					{	
						temp = element.text();
						
						//Tırnaklar temizleniyor.
						temp = temp.replaceAll("\'", " ");
						temp = temp.replaceAll("\"", " ");
						
						temp = "'"+ temp + "',"+etiket+"\n";
						toplananlar = toplananlar + temp;
						mesajSayaci++;
						System.out.println(mesajSayaci+temp);
					}
				//Diğer sayfa için i arttırıldı.
				i++;
				
			}while(elements != null);
			
			dosyayaYaz(toplananlar,"Hepsiburada",yildiz+" yıldız",mesajSayaci);

		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			dosyayaYaz(toplananlar,"Hepsiburada",yildiz +" yıldız",mesajSayaci);
		}
	}
	
	public static void sikayetCom(String firma)
	{
		int i = 1,numberOfPage=9;
		String temp,toplananlar = "";
		Elements gelenler;
		try {
			Document doc = Jsoup.connect("http://www.sikayet.com/firma/"+firma).get();

			Element maxSayfa = doc.select(".page-text > strong:nth-child(1)").first();
			numberOfPage = Integer.valueOf(maxSayfa.text());
		    System.out.println(numberOfPage);

			for(i=1;i<=numberOfPage;i++)
			{
				Document doc1 = Jsoup.connect("http://www.sikayet.com/firma/"+firma+"/"+String.valueOf(i))
						.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:54.0) Gecko/20100101 Firefox/54.0")
						.get();
				
				gelenler = doc1.select("a.title");
				
				for (Element gelen : gelenler)
				{
					String detayUrl;
					detayUrl=gelen.attr("href");
					
					Document docDetaySayfasi = Jsoup.connect(detayUrl).get();
					
					String sikayet = "";
				    sikayet= docDetaySayfasi.select(".has-text > p").first().text();
				    
				    temp = sikayet;
					
					//Tırnaklar temizleniyor.
					temp = temp.replaceAll("\'", " ");
					temp = temp.replaceAll("\"", " ");
					
					temp = "'"+ temp + "',"+"neg"+"\n";
					toplananlar = toplananlar + temp;
				    
				    System.out.println(sikayet);
				}
				
				
			}
			
			dosyayaYaz(toplananlar, firma, "sikayet.com", 1);
			System.out.println("Bitti");
			
		} catch (Exception ex) {
			System.out.println(ex.toString());
			dosyayaYaz(toplananlar, firma, "sikayet.com", 1);

		}

	}
	

	public static void sikayetComSoruIsaretiEkle(String firma)
	{
		int i = 1,numberOfPage=9;
		String temp,toplananlar = "";
		Elements gelenler;
		try {
			Document doc = Jsoup.connect("http://www.sikayet.com/firma/"+firma).get();

			Element maxSayfa = doc.select(".page-text > strong:nth-child(1)").first();
			numberOfPage = Integer.valueOf(maxSayfa.text());
		    System.out.println(numberOfPage);

			for(i=1;i<=numberOfPage;i++)
			{
				Document doc1 = Jsoup.connect("http://www.sikayet.com/firma/"+firma+"/"+String.valueOf(i))
						.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:54.0) Gecko/20100101 Firefox/54.0")
						.get();
				
				gelenler = doc1.select("a.title");
				
				for (Element gelen : gelenler)
				{
					String detayUrl;
					detayUrl=gelen.attr("href");
					
					Document docDetaySayfasi = Jsoup.connect(detayUrl).get();
					
					String sikayet = "";
				    sikayet= docDetaySayfasi.select(".has-text > p").first().text();
				    
				    temp = sikayet;
					
					//Tırnaklar temizleniyor.
					temp = temp.replaceAll("\'", " ");
					temp = temp.replaceAll("\"", " ");
					
					temp = "'"+ temp + "',"+"?"+"\n";
					toplananlar = toplananlar + temp;
				    
				    System.out.println(sikayet);
				}
				
			}
			
			dosyayaYaz(toplananlar, firma, "sikayet.com", 1);
			System.out.println("Bitti");
			
		} 
		catch (Exception ex) 
		{
			System.out.println(ex.toString());
			dosyayaYaz(toplananlar, firma, "sikayet.com", 1);
		}

	}
	
	
	public static void dosyayaYaz(String veriler,String firma, String kaynak,int adet)
	{
		try
		{
		    PrintWriter writer = new PrintWriter(kaynak+" "+firma+" "+adet+".txt", "UTF-8");
		    writer.println(veriler);
		    writer.close();
		    System.out.println("Dosyaya yazıldı. !");
		} catch (Exception e) {
		   // do something
		}
		
	}
	
	
}
