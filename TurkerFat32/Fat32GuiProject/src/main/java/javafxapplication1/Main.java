package javafxapplication1;

import com.sun.javafx.application.PlatformImpl;
import javafx.stage.Stage;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

        initFx();
    }

    // Su iki link yardimci oldu jar dosyasi olarak javafx uygulamasi calistiramadim hata veriyordu asagidaki gibi:
    // https://stackoverflow.com/questions/53754275/trying-to-run-executable-jar-of-javafx-app-error-javafx-runtime-components-ar
    // linkin biri disaridan cagir demis main sinifi olusturup, ben de Main sinifi
    // olusturup Fat32Gui sinifini disaridan cagirdim baska hata verdi.
    // Onu da asagidaki gibi verdigi initFx metodunu aynen yapistirip oradaki
    // run metodunun icinde cagirdim Fat32Gui sinifimin start metodunu.
    // https://www.jensd.de/wordpress/?p=1843
    //
    // JFXPanel and FX Platform Thread pitfalls
    // Initialize FX runtime when the JFXPanel instance is constructed
    private synchronized static void initFx() {
        // Note that calling PlatformImpl.startup more than once is OK
        PlatformImpl.startup(new Runnable() {
            @Override public void run() {
                // No need to do anything here
                Fat32Gui fat32Gui = new Fat32Gui();
                try {
                    fat32Gui.start(new Stage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

}
