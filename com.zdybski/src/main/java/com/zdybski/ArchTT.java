package com.zdybski;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class ArchTT {

    public static final String JPG_EXTENSION = ".jpg";
    private static final String PDF_EXTENSION = ".pdf";
    boolean notFirstRun = false;

    private class RunnableImpl implements Runnable {

        public void run()
        {
            System.out.println("New Thread is running with Komunikat Lawinowy");
            while (true) {
                try {

                    if(notFirstRun){
                        Thread.sleep(86400000);
                    }
                    notFirstRun = true;

                    saveResourceFromLink("https://lawiny.topr.pl/viewpdf", "C:/ArchTT/lawinowy/komunikat-", PDF_EXTENSION);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){

       /* Map<String, List<String>> paxIDTypeMap = new HashMap<>();
        List<String> listADT = new ArrayList<>();
        listADT.add("pierwotny 1");
        listADT.add("pierwotny 2");

        paxIDTypeMap.put("ADT", listADT);
        paxIDTypeMap.put("CHD", new ArrayList<>());
        paxIDTypeMap.put("INF", new ArrayList<>());


       boolean value =  paxIDTypeMap.computeIfAbsent("ADT", k -> new ArrayList<>()).add("jakis idik w stringu");

       System.out.println(value);

       System.out.println(paxIDTypeMap);

       System.out.println("END OF FILE");

        System.out.println(IdType.SERVICE);
        System.out.println(IdType.SERVICE.getPrefix());

        System.out.println("=====================================");

        List<String> ptcList = Arrays.asList("ADT", "CHD", "INF");

        System.out.println(ptcList);*/

        Runnable komunikatThread = new Thread(new ArchTT().new RunnableImpl());

        Thread t1 = new Thread(komunikatThread);

        Runnable camerasThread = () -> {
            System.out.println("New Thread is running with Saving photos from Tatras Cameras");

            savePhotoFromDifferentCameras();
        };

        Thread t2 = new Thread(camerasThread);

        t1.start();
        t2.start();

    }

    private static void savePhotoFromDifferentCameras() {
        try {
            for (int i = 0; true ; i++) {

                Thread.sleep(300000);

                saveResourceFromLink("http://kamery.topr.pl/moko/moko_01.jpg", "C:/ArchTT/mokoRysy/rysy", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/moko_TPN/moko_02.jpg", "C:/ArchTT/mokoMnich/mnich", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/stawy2/stawy2.jpg", "C:/ArchTT/5stawow/5stawow", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/hala/hala.jpg", "C:/ArchTT/gasienicowa/stawgasienicowy", JPG_EXTENSION);


                saveResourceFromLink("http://kamery.topr.pl/stawy1/stawy1.jpg", "C:/ArchTT/buczynowa/buczynowa", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/gasienicowa/gasie.jpg", "C:/ArchTT/swinica/swinica", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/goryczkowa/gorycz.jpg", "C:/ArchTT/goryczkowa/goryczkowa", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/czwierchy/czwierchy.jpg", "C:/ArchTT/giewont/giewont", JPG_EXTENSION);
                saveResourceFromLink("http://kamery.topr.pl/chocholowska/chocholow.jpg", "C:/ArchTT/chocholowska/chocholowska", JPG_EXTENSION);

                saveResourceFromLink("https://pogoda.topr.pl/download/current/hgkw.jpeg", "C:/ArchTT/swinicakasprowy/swinicakasprowy", JPG_EXTENSION);

                //https://www.w3schools.com/w3js/tryit.asp?filename=tryw3js_slideshow   TODO dodac pokaz slajdow tego typu na stronie
                //TODO dodac katalogi na tygodnie moze albo dni? bardziej tygodnie

                //TODO zmienic sposob logowania zeby szlo tez do pliku
                //TODO zmienic fomart zapisu daty i godziny w plikach zdjeciowych na jakis czytelny

                //TODO dodac sekundy w nazwie pliku zeby nie bylo duplikatow - obsluzyc co zrobic jesli plik juz istnieje
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            savePhotoFromDifferentCameras();

        } catch (InterruptedException e) {
            e.printStackTrace();
            savePhotoFromDifferentCameras();

        } catch (Exception e){
            e.printStackTrace();
            savePhotoFromDifferentCameras();
        }
    }

    private static void saveResourceFromLink(String cameraLink, String destinationFolder, String extension) throws Exception{

        int responseCode = ((HttpURLConnection)new URL(cameraLink).openConnection()).getResponseCode();

        if(responseCode == 200){
            InputStream in = new URL(cameraLink).openStream();
            LocalDateTime time = LocalDateTime.now();

            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int weekNumber = time.get(woy);

            String destinationFolderPlusWeek = destinationFolder + weekNumber;
            String fileName = destinationFolderPlusWeek + "/" + time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + "-+-" + time.getHour() + "-" + time.getMinute() + extension;

            if(Files.notExists(Paths.get(destinationFolderPlusWeek))){
                File dir = new File(destinationFolderPlusWeek);
                boolean mkdir = dir.mkdir();
                System.out.println(mkdir);
            }

            Files.copy(in, Paths.get(fileName));
            System.out.println(time);
            System.out.println("saving file: "+fileName);
            System.out.println("-----------------------------------------------");
        } else {
            System.out.println(cameraLink + " is bad resource, link returned wrong error code: " + responseCode);
        }


    }
}
