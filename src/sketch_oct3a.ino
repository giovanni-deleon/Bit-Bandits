// Left joystick  -> Grove port A0 (X = A0, Y = A1)
// Right joystick -> Grove port A2 (X = A2, Y = A3)
const int J1_X = A0, J1_Y = A1;
const int J2_X = A2, J2_Y = A3;
const int BUZZER = 4;   // Grove buzzer on port D4

void setup() {
  Serial.begin(115200);
  pinMode(BUZZER, OUTPUT);
}

void loop() {
  int x1 = analogRead(J1_X), y1 = analogRead(J1_Y);
  int x2 = analogRead(J2_X), y2 = analogRead(J2_Y);

  Serial.print("J1: "); Serial.print(x1); Serial.print(", "); Serial.print(y1);
  Serial.print("   J2: "); Serial.print(x2); Serial.print(", "); Serial.println(y2);

  if (abs(x1 - 512) > 100) {
    tone(BUZZER, 70);   // 1000 Hz beep
    delay(300);
    noTone(BUZZER);
    delay(700);
  } else {
    noTone(BUZZER);
  }
  delay(100);
}