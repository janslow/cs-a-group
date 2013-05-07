#define LmotorA             3  // Left  motor H bridge, input A
#define LmotorB            11  // Left  motor H bridge, input B
#define RmotorA             5  // Right motor H bridge, input A
#define RmotorB             6  // Right motor H bridge, input B

#define Battery             0  // Analog input 00
#define RmotorC             6  // Analog input 06
#define LmotorC             7  // Analog input 07
#define Charger            13  // Low=ON High=OFF

#define LeftPWM           128
#define RightPWM          128

#define Leftmaxamps        600     // set overload current for left motor 
#define Rightmaxamps       600     // set overload current for right motor 
#define overloadtime       100     // time in mS before motor is re-enabled after overload occurs

#define batvolt            487     // This is the nominal battery voltage reading. Peak charge can only occur above this voltage.
#define lowvolt            410     // This is the voltage at which the speed controller goes into recharge mode.
#define chargetimeout   300000     // If the battery voltage does not change in this number of milliseconds then stop charging.

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
    Serial.write(v);
  }
  
  if (inCommand && millis() > commandExpire) {
    analogWrite(LmotorA,0);
    analogWrite(LmotorB,0);
    analogWrite(RmotorA,0);
    analogWrite(RmotorB,0);
    inCommand = false;
  }
  
  if (!inCommand && Serial.available() > 0) {
    int c = Serial.read();
    inCommand = true;
    switch (c) {
    case 0: //Forward
      analogWrite(LmotorA,0);
      analogWrite(LmotorB,LeftPWM);
      analogWrite(RmotorA,0);
      analogWrite(RmotorB,RightPWM);
      commandExpire = millis() + 5000;
    case 1: //Reverse
      analogWrite(LmotorA,LeftPWM);
      analogWrite(LmotorB,0);
      analogWrite(RmotorA,RightPWM);
      analogWrite(RmotorB,0);
      commandExpire = millis() + 5000;
    case 2:
      analogWrite(LmotorA,LeftPWM);
      analogWrite(LmotorB,0);
      analogWrite(RmotorA,0);
      analogWrite(RmotorB,RightPWM);
      commandExpire = millis() + 5000;
    case 3:
      analogWrite(LmotorA,0);
      analogWrite(LmotorB,LeftPWM);
      analogWrite(RmotorA,RightPWM);
      analogWrite(RmotorB,0);
      commandExpire = millis() + 5000;
    case 4:
      analogWrite(LmotorA,0);
      analogWrite(LmotorB,0);
      analogWrite(RmotorA,0);
      analogWrite(RmotorB,0);
      inCommand = false;
      commandExpire = millis();
    }
  }
}
