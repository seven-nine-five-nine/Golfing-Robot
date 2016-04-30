package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Mary on 1/8/16, edited on 4/29/16.
 */
public class OpModeCougarWoods3 extends OpMode{

  //To create a proper variable(?) just program it to actually do something.
  //Then it'll turn purple like the rest.


  private double swingPower = 0.5;
  private double frontRightPowerPre = 0;
  private double frontLeftPowerPre = 0;
  private double backRightPowerPre = 0;
  private double backLeftPowerPre = 0;
  private double frontRightPowerPost = 0;
  private double frontLeftPowerPost = 0;
  private double backRightPowerPost = 0;
  private double backLeftPowerPost = 0;
  private boolean overrideWheels = false;
  private double wheelSpeedMod = 0;
  private boolean wheelOverrideSpeedChange = false;
  private double frontRightPowerOverridePre = 0;
  private double frontLeftPowerOverridePre = 0;
  private double backRightPowerOverridePre = 0;
  private double backLeftPowerOverridePre = 0;
  private double frontRightPowerOverridePost = 0;
  private double frontLeftPowerOverridePost = 0;
  private double backRightPowerOverridePost = 0;
  private double backLeftPowerOverridePost = 0;
  private double movementSpeed = 0;
  //movement variables
  private DcMotor backLeft;
  private DcMotor backRight;
  private DcMotor frontLeft;
  private DcMotor frontRight;
  private DcMotor golfClub;
  //motor variables
  private boolean isIncreasingSwing = false;
  private boolean isDecreasingSwing = false;
  //misc. golfing variables

  @Override
  public void init() {
    backLeft = hardwareMap.dcMotor.get("backLeft");
    backRight = hardwareMap.dcMotor.get("backRight");
    frontLeft = hardwareMap.dcMotor.get("frontLeft");
    frontRight = hardwareMap.dcMotor.get("frontRight");
    golfClub = hardwareMap.dcMotor.get("golfClub");

    golfClub.setDirection(DcMotor.Direction.FORWARD);
    backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    golfClub.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    backLeft.setDirection(DcMotor.Direction.REVERSE);
    frontLeft.setDirection(DcMotor.Direction.REVERSE);


    //some of the motors may need to be reversed, if they're spinning the wrong way call:
    //WHATEVER_MOTOR.setDirection(DcMotor.Direction.REVERSE);
  }

  @Override
  public void loop() {
    double frm = 0;
    double flm = 0;
    double brm = 0;
    double blm = 0;
    double frt = 0;
    double flt = 0;
    double brt = 0;
    double blt = 0;
    // integers for movement
    double lx = gamepad1.left_stick_x;
    double ly = gamepad1.left_stick_y;
    double lr = Math.sqrt((ly * ly) + (lx * lx));

    if ((gamepad1.left_stick_y >= 0.05) || (gamepad1.left_stick_y <= -0.05)) {
      frm += gamepad1.left_stick_y;
      flm += gamepad1.left_stick_y;
      brm += gamepad1.left_stick_y;
      blm += gamepad1.left_stick_y;
    }

    if ((gamepad1.left_stick_x >= 0.05) || (gamepad1.left_stick_x <= -0.05)) {
      frm += gamepad1.left_stick_x;
      flm -= gamepad1.left_stick_x;
      brm -= gamepad1.left_stick_x;
      blm += gamepad1.left_stick_x;
    }

    if ((gamepad1.left_bumper) && (gamepad1.right_bumper)) {
      frontRightPowerOverridePre = 1;
      frontLeftPowerOverridePre = 1;
      backRightPowerOverridePre = 1;
      backLeftPowerOverridePre = 1;
      overrideWheels = true;
    }

    if (gamepad1.left_bumper && !gamepad1.right_bumper) {
      frontRightPowerOverridePre = 1;
      frontLeftPowerOverridePre = 0;
      backRightPowerOverridePre = 0;
      backLeftPowerOverridePre = 1;
      overrideWheels = true;
    }

    if (gamepad1.right_bumper && !gamepad1.left_bumper) {
      frontRightPowerOverridePre = 0;
      frontLeftPowerOverridePre = 1;
      backRightPowerOverridePre = 1;
      backLeftPowerOverridePre = 0;
      overrideWheels = true;
    }

    if (gamepad1.y && !(gamepad1.right_bumper) && !(gamepad1.left_bumper)) {
      frontRightPowerOverridePre = -1;
      frontLeftPowerOverridePre = -1;
      backRightPowerOverridePre = -1;
      backRightPowerOverridePre = -1;
      overrideWheels = true;
    }
    
    if (!(gamepad1.y) && !(gamepad1.right_bumper) && !(gamepad1.left_bumper)) {
      overrideWheels = false;
    }
    
    if (gamepad1.dpad_left && !wheelOverrideSpeedChange) {
      wheelSpeedMod -= 0.05;
      wheelOverrideSpeedChange = true;
    } else {
      wheelOverrideSpeedChange = false;
    }
    
    if (gamepad1.dpad_right && !wheelOverrideSpeedChange) {
      wheelSpeedMod += 0.05;
      wheelOverrideSpeedChange = true;
    } else {
      wheelOverrideSpeedChange = false;
    }

    if (gamepad1.left_trigger >= 0.1){
      frt += gamepad1.left_trigger;
      flt -= gamepad1.left_trigger;
      brt += gamepad1.left_trigger;
      blt -= gamepad1.left_trigger;
    }

    if (gamepad1.right_trigger >= 0.1) {
      frt -= gamepad1.right_trigger;
      flt += gamepad1.right_trigger;
      brt -= gamepad1.right_trigger;
      blt += gamepad1.right_trigger;
    }

    movementSpeed = lr;

    //telemetry.addData("SPEED", movementSpeed);

    frontRightPowerPre = frm + frt;
    frontLeftPowerPre = flm + flt;
    backRightPowerPre = brm + brt;
    backLeftPowerPre = blm + blt;

    trimWheelPower(); //keeps wheels below 1 and above -1 power
    
    trimOverridePower();
    if (overrideWheels) {
      frontRight.setPower(frontRightPowerOverridePost);
      frontLeft.setPower(frontLeftPowerOverridePost);
      backRight.setPower(backRightPowerOverridePost);
      backLeft.setPower(backLeftPowerOverridePost);
    } else {
      frontRight.setPower(frontRightPowerPost);
      frontLeft.setPower(frontLeftPowerPost);
      backRight.setPower(backRightPowerPost);
      backLeft.setPower(backLeftPowerPost);
    }

    // f/b refer to front/back, l/r refer to left/right, m/t refer to direction/turning


    if (gamepad1.dpad_up && !isIncreasingSwing) {
      isIncreasingSwing = true;
      swingPower += 0.01;
    } else {
      isIncreasingSwing = false;
    }

    if (gamepad1.dpad_down && !isDecreasingSwing) {
      isDecreasingSwing = true;
      swingPower -= 0.01;
    } else {
      isDecreasingSwing = false;
    }
    trimSwingPower(); //make sure the power is an acceptable value

    telemetry.addData("SWING POWER", swingPower);

    if (gamepad1.a) swing();


  }

  private void swing() {
    golfClub.setPower(-1 * swingPower);

    sleep(2000);

    golfClub.setPower(swingPower);

    sleep(3000);

    golfClub.setPower(0);
  }

  private void sleep(int ms) {
    try{
      Thread.sleep(ms);
    } catch (InterruptedException e){

    }

  }

  private void trimOverridePower() {
    if (frontRightPowerOverridePre + wheelSpeedMod > 1) {
      frontRightPowerOverridePost = 1;
    } else if (frontRightPowerOverridePre + wheelSpeedMod < 0.05) {
      frontRightPowerOverridePost = 0.05;
    } else {
      frontRightPowerOverridePost = frontRightPowerOverridePre + wheelSpeedMod;
    }

    if (frontLeftPowerOverridePre + wheelSpeedMod > 1) {
      frontLeftPowerOverridePost = 1;
    } else if (frontLeftPowerOverridePre + wheelSpeedMod < 0.05) {
      frontLeftPowerOverridePost = 0.05;
    } else {
      frontLeftPowerOverridePost = frontLeftPowerOverridePre + wheelSpeedMod;
    }

    if (backRightPowerOverridePre + wheelSpeedMod > 1) {
      backRightPowerOverridePost = 1;
    } else if (backRightPowerOverridePre + wheelSpeedMod < 0.05) {
      backRightPowerOverridePost = 0.05;
    } else {
      backRightPowerOverridePost = backRightPowerOverridePre + wheelSpeedMod;
    }

    if (backLeftPowerOverridePre + wheelSpeedMod > 1) {
      backLeftPowerOverridePost = 1;
    } else if (backLeftPowerOverridePre + wheelSpeedMod < 0.05) {
      backLeftPowerOverridePost = 0.05;
    } else {
      backLeftPowerOverridePost = backLeftPowerOverridePre + wheelSpeedMod;
    }
  }
  
  private void trimWheelPower() {
    if (frontRightPowerPre > 1) {
      frontRightPowerPost = 1;
    } else if (frontRightPowerPre < -1) {
      frontRightPowerPost = -1;
    } else {
      frontRightPowerPost = frontRightPowerPre;
    }

    if (frontLeftPowerPre > 1) {
      frontLeftPowerPost = 1;
    } else if (frontLeftPowerPre < -1) {
      frontLeftPowerPost = -1;
    } else {
      frontLeftPowerPost = frontLeftPowerPre;
    }

    if (backRightPowerPre > 1) {
      backRightPowerPost = 1;
    } else if (backLeftPowerPre < -1) {
      backRightPowerPost = -1;
    } else {
      backRightPowerPost = backRightPowerPre;
    }

    if (backLeftPowerPre > 1) {
      backLeftPowerPost = 1;
    } else if (backLeftPowerPre < -1) {
      backLeftPowerPost = -1;
    } else {
      backLeftPowerPost = backLeftPowerPre;
    }
  }

  public void trimSwingPower() {
    if (swingPower > 1) {
      swingPower = 1;
    } else if (swingPower < 0) {
      swingPower = 0;
    }
  }


  public double scaleInput(double dVal) {
    double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
            0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

    // get the corresponding index for the scaleInput array.
    int index = (int) (dVal * 16.0);

    // index should be positive.
    if (index < 0) {
      index = -index;
    }

    // index cannot exceed size of array minus 1.
    if (index > 16) {
      index = 16;
    }

    // get value from the array.
    double dScale = 0.0;
    if (dVal < 0) {
      dScale = -scaleArray[index];
    } else {
      dScale = scaleArray[index];
    }

    // return scaled value.
    return dScale;

}

}
