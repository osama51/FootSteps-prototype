#include <dummy.h>

//This example code is in the Public Domain (or CC0 licensed, at your option.)
//By Evandro Copercini - 2018
//
//This example creates a bridge between Serial and Classical Bluetooth (SPP)
//and also demonstrate that SerialBT have the same functionalities of a normal Serial

#include "BluetoothSerial.h"


#include <MPU6050_tockn.h>
#include <Wire.h>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

MPU6050 mpu6050(Wire);
BluetoothSerial SerialBT;
int counter0 = 0;
int counter1 = 0;
int counter2 = 0;
int counter3 = 0;
int counter4 = 0;
int counter5 = 0;

int acc1 = 0;
int acc2 = 0;
int acc3 = 0;


int id = 1;
String str= "";
void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");

  Wire.begin();
  mpu6050.begin();
  mpu6050.calcGyroOffsets(true);
}

void loop() {
  mpu6050.update();

  acc1 = mpu6050.getAngleX() + 180;
  acc2 = mpu6050.getAngleY() + 180;
  acc3 = mpu6050.getAngleZ() + 180;
  
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
//if(counter0 >= 3930){
  if(id == 1){
  id = 2;
  } else {
    id = 1;  
  }
//}

counter0 = (counter0 + 40) % 4000;
counter1 = (counter1 + 15) % 1500;
counter2 = (counter2 + 25) % 2500;
counter3 = (counter3 + 30) % 3000;
counter4 = (counter4 + 35) % 3500;
counter5 = (counter5 + 35) % 3500;

str = id;
str += "i";
str += String(counter0);
str += "s";
str += String(counter1);
str += "t";
str += String(counter2);
str += "u";
str += String(counter3);
str += "v";
str += String(counter4);
str += "w";
str += String(counter5);
str += "x";

str += String(acc1);
str += "a";
str += String(acc2);
str += "b";
str += String(acc3);
str += "c";
str += "1543d1543e1543f#";

    SerialBT.println(str);
//    SerialBT.println(str);
//    delay(100);
//  }
  delay(25);
}


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
