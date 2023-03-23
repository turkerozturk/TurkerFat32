/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/


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
