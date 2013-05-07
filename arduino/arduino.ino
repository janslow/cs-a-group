#define LmotorA             3  // Left  motor H bridge, input A
#define LmotorB            11  // Left  motor H bridge, input B
#define RmotorA             5  // Right motor H bridge, input A
#define RmotorB             6  // Right motor H bridge, input B

#define Battery             0  // Analog input 00
#define RmotorC             6  // Analog input 06
#define LmotorC             7  // Analog input 07
#define Charger            13  // Low=ON High=OFF

#define LeftPWM           230
#define RightPWM          170

#define Leftmaxamps        600     // set overload current for left motor 
#define Rightmaxamps       600     // set overload current for right motor 
#define overloadtime       100     // time in mS before motor is re-enabled after overload occurs

#define batvolt            487     // This is the nominal battery voltage reading. Peak charge can only occur above this voltage.
#define lowvolt            410     // This is the voltage at which the speed controller goes into recharge mode.
#define chargetimeout   300000     // If the battery voltage does not change in this number of milliseconds then stop charging.

#define DEBUG             false

unsigned long lastUpdate = 0;
unsigned long commandExpire = 0;
boolean inCommand = false;

void setup() {
  pinMode (Charger,OUTPUT);                                   // change Charger pin to output
  digitalWrite (Charger,0);                                   // disable current regulator to charge battery

  Serial.begin(9600);
}

void loop() {
  if (millis() - lastUpdate > 1000) {
    unsigned int v = analogRead(Battery) >> 2;
    lastUpdate = millis();
    if (DEBUG) Serial.println(v);
    else Serial.write(v & 0xFF);
  }
  
  if (inCommand && millis() > commandExpire) {
    analogWrite(LmotorA,0);
    analogWrite(LmotorB,0);
    analogWrite(RmotorA,0);
    analogWrite(RmotorB,0);
    if (DEBUG) Serial.println("Done");
    inCommand = false;
  }
  
  if (!inCommand && Serial.available() > 0) {
    int c = Serial.read();
    inCommand = true;
    switch (c) {
    case 0:
    case '0': //Forward
      analogWrite(LmotorA,0);
      analogWrite(RmotorA,0);
      analogWrite(LmotorB,LeftPWM);
      analogWrite(RmotorB,RightPWM);
      commandExpire = millis() + 80;
      if (DEBUG) Serial.println("Forward");
      break;
    case 1:
    case '1': //Reverse
      analogWrite(LmotorB,0);
      analogWrite(RmotorB,0);
      analogWrite(RmotorA,RightPWM);
      analogWrite(LmotorA,LeftPWM);
      commandExpire = millis() + 81;
      if (DEBUG) Serial.println("Backward");
      break;
    case 4:
    case '4':
      analogWrite(LmotorA,0);
      analogWrite(LmotorB,0);
      analogWrite(RmotorA,0);
      analogWrite(RmotorB,0);
      inCommand = false;
      commandExpire = millis();
      if (DEBUG) Serial.println("Emergency Stop");
      break;
    }
  }
}
