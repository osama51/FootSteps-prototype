#include <dummy.h>

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
int id = 1;
String str= "";
void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
}

void loop() {
  str = "";
  if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  if (SerialBT.available()) {
    Serial.write(SerialBT.read());
  }
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


//  counter = (counter + 68) % 4095;

//counter = (counter + 1) % 60;
if(id == 1){
  id = 2;
  } else {
    id = 1;  
  }
counter = (counter + 100) % 4095;


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

str = id;
str += "i";
str += String(counter);
str += "s1233t";
str += String(counter);
str += "u";
str += String(counter);
str += "v";
str += String(counter);
str += "w";
str += String(counter);
str += "x";
str += "1233a1543b1543c1543d1543e1543f#";
    SerialBT.println(str);
//    SerialBT.println(str);
//    delay(100);
//  }
  delay(25);
}
