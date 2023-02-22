//#include <dummy.h>

//This example code is in the Public Domain (or CC0 licensed, at your option.)
//By Evandro Copercini - 2018
//
//This example creates a bridge between Serial and Classical Bluetooth (SPP)
//and also demonstrate that SerialBT have the same functionalities of a normal Serial

#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
int counter = 0;
String str= "";
int id = 0;
int s = 0;
int t = 0;
int u = 0;
int v = 0;
int w = 0;
int x = 0;
unsigned long currentMillis;
#define interval 25
unsigned long previousMillis = 0;


void setup() {
  Serial.begin(9600);
  SerialBT.begin("ESP32_Osama"); //Bluetooth device name
  // Serial.println("The device started, now you can pair it with bluetooth!");
}

void loop() {
  currentMillis = millis();
  if(currentMillis - previousMillis >= interval) {
    
    str = "";
    id = 0;
    s = 0;
    t = 0;
    u = 0;
    v = 0;
    w = 0;
    x = 0;
    // if (Serial.available()) {
    //   SerialBT.write(Serial.read());
    // }
    // if (SerialBT.available()) {
    //   Serial.write(SerialBT.read());
    // }
    //  if (SerialBT.available()) {

    //SerialBT.print("1i");
    //SerialBT.print("231f0");
    //SerialBT.print("1233f1");
    //SerialBT.print("1233a0");
    //SerialBT.print("1543a1");
    //SerialBT.print("1543a2");
    //SerialBT.print("1543g0");
    //SerialBT.print("1543g1");
    //SerialBT.print("1543g2");
    //SerialBT.println("#");

    counter = (counter + 68) % 4095;
    //SerialBT.print("1i");
    //SerialBT.print(counter+"s");
    //SerialBT.print("1233t");
    //SerialBT.print("1233a");
    //SerialBT.print("1543b");
    //SerialBT.print("1543c");
    //SerialBT.print("1543d");
    //SerialBT.print("1543e");
    //SerialBT.print("1543f");
    //SerialBT.print("#");

//    str = "1i";
//    str += String(counter);
//    str += "s";
//    str += String(counter - (counter * 0.2));
//    str += "t";
//    str += String(counter - (counter * 0.2));
//    str += "u";
//    str += String(counter - (counter * 0.6));
//    str += "v";
//    str += String(counter - (counter * 0.8));
//    str += "w";
//    str += String(counter - (counter * 1));
//    str += "x1233a1543b1543c1543d1543e1543f#";

    
    str = "1i";
    str += String(counter);
    str += "s";
    str += String(counter);
    str += "t";
    str += String(counter);
    str += "u";
    str += String(counter);
    str += "v";
    str += String(counter);
    str += "w";
    str += String(counter);
    str += "x1233a1543b1543c1543d1543e1543f#";
    
    SerialBT.println(str);
    Serial.println(str);

    previousMillis = currentMillis;

    //    delay(100);
  }
}
