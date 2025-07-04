#include <Keyboard.h>
  
void setup() {
   pinMode(2, INPUT_PULLUP);
  //  put your setup code here, to run once:
   Keyboard.begin();
 } 
  
void loop() {
  // put your main code here, to run repeatedly:
  int sensorValueX = analogRead(A0);
  int sensorValueY = analogRead(A1);
  int buttonState = digitalRead(2);                           
  
  // based on direction of joystick, press and release keyboard arrows
  if(sensorValueX > 675){
    Keyboard.press(KEY_RIGHT_ARROW);
    Keyboard.release(KEY_RIGHT_ARROW);
  }
  if (sensorValueX < 325){
    Keyboard.press(KEY_LEFT_ARROW);
    delay(1);
    Keyboard.release(KEY_LEFT_ARROW);
  }
  if (sensorValueY > 645){
    Keyboard.press(KEY_UP_ARROW);
    delay(1);
    Keyboard.release(KEY_UP_ARROW);
  }
  if(sensorValueY < 385){
    Keyboard.press(KEY_DOWN_ARROW);
    delay(1);
    Keyboard.release(KEY_DOWN_ARROW);
  }                                             
                   
   // if joystick is pressed down, simulate spacebar click
  if(buttonState == LOW){
    Keyboard.press(0X20);  
    Keyboard.release(0x20);                      
  }

  delay(150);
}
